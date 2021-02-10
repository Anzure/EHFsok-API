package no.ehfsok.service;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import no.ehfsok.dao.ElmaDetailRepo;
import no.ehfsok.dao.TaskStateRepo;
import no.ehfsok.model.Company;
import no.ehfsok.model.ElmaDetail;
import no.ehfsok.model.TaskState;
import no.ehfsok.type.TaskStatus;

@Service
@Slf4j
public class ElmaService {

	private String spreadsheetSource = "https://hotell.difi.no/download/difi/elma/participants?download";

	private String taskIdentifier = "elma-update-5";

	@Value("${elma.refresh_rate_in_hours}")
	private Integer elmaRefreshRate;

	@Autowired
	private ElmaDetailRepo elmaDetailRepo;

	@Autowired
	private TaskStateRepo taskStateRepo;

	@Autowired
	private CompanyService companyService;

	@Scheduled(initialDelay = 60*1000, fixedDelay = 15*60*1000)
	public void update() {

		TaskState taskState = taskStateRepo.findById(taskIdentifier).orElseGet(() -> {
			return taskStateRepo.save(TaskState.builder()
					.id(taskIdentifier)
					.status(TaskStatus.PENDING)
					.lastRun(LocalDateTime.now().minusHours(elmaRefreshRate))
					.nextRun(LocalDateTime.now())
					.build());
		});

		if (taskState.getStatus() == TaskStatus.RUNNING) {
			log.debug("ELMA EHF update is currently running.");
			return;
		}

		if (LocalDateTime.now().isBefore(taskState.getNextRun())) {
			log.debug("ELMA EHF update is scheduled to run: ");
			log.debug(taskState.getNextRun().toString());
			return;
		}

		// Initiate task
		log.info("Updating ELMA EHF registry...");
		taskState.setStatus(TaskStatus.RUNNING);
		taskState.setLastRun(LocalDateTime.now());
		taskState.setNextRun(LocalDateTime.now().plusHours(elmaRefreshRate));
		taskStateRepo.save(taskState);

		// Run task
		try {

			// Download CSV file
			log.info("Downloading ELMA EHF spreadsheet...");
			URL website = new URL(spreadsheetSource);
			Path target = Path.of("." + File.separator + "elma.csv");
			try (InputStream in = website.openStream()) {
				Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
			}
			File file = target.toFile();
			file.deleteOnExit();
			log.info("Successfully downloaded CSV.");

			// Process the file
			log.info("Processing spreadsheet...");
			URL localUrl = file.toURI().toURL();
			Reader reader = new InputStreamReader(new BOMInputStream(localUrl.openStream()), StandardCharsets.UTF_8);
			//		CharacterFilterReader filterReader = new CharacterFilterReader();
			CSVFormat format = CSVFormat.DEFAULT.withIgnoreHeaderCase().withAllowMissingColumnNames().withDelimiter(';')
					.withFirstRecordAsHeader().withQuote('"').withNullString("").withIgnoreSurroundingSpaces().withIgnoreEmptyLines()
					.withEscape('\\');
			CSVParser parser = new CSVParser(reader, format);
			List<CSVRecord> records = parser.getRecords();

			// Loop trough all rows
			records.forEach(record -> {
				// Retrieve row variables
				String name = record.get("name");
				String regDate = record.get("regdate");
				Integer icdNumber = Integer.parseInt(record.get("Icd"));
				Long orgNumber = Long.parseLong(record.get("identifier"));
				LocalDate date = LocalDate.parse(regDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

				// Create or get Company from storage
				Company company = companyService.getOrCreate(name, orgNumber);

				// Create and save ElmaDetail if missing
				elmaDetailRepo.findByCompany(company).orElseGet(() -> {

					ElmaDetail elmaDetail = new ElmaDetail();
					elmaDetail.setCompany(company);
					elmaDetail.setIcd(icdNumber);
					elmaDetail.setRegistrationDate(date);

					return elmaDetailRepo.save(elmaDetail);
				});
			});

			// Process complete
			parser.close();
			reader.close();
			FileUtils.forceDelete(file);
			log.info("Successfully processed CSV.");

			// End task
			taskState.setLastRun(LocalDateTime.now());
			taskState.setNextRun(LocalDateTime.now().plusHours(elmaRefreshRate));
			taskState.setStatus(TaskStatus.COMPLETE);
			taskStateRepo.save(taskState);
			log.info("Successfully updated ELMA EHF registry.");

		} catch (Exception ex) {
			// Handle error
			log.error("Failed to update ELMA EHF registry.", ex);
			taskState.setStatus(TaskStatus.FAILED);
			taskState.setLastRun(LocalDateTime.now());
			taskState.setNextRun(LocalDateTime.now().plusHours(elmaRefreshRate));
			taskStateRepo.save(taskState);
		}
	}

}