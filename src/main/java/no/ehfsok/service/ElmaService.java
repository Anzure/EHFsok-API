package no.ehfsok.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import no.ehfsok.dao.ElmaDetailRepo;
import no.ehfsok.model.Company;
import no.ehfsok.model.ElmaDetail;

@Service
public class ElmaService {

	private static final Logger log = LoggerFactory.getLogger(ElmaService.class);

	private String spreadsheetSource = "https://hotell.difi.no/download/difi/elma/participants?download";

	private boolean updateInProgress = false;

	@Autowired
	private ElmaDetailRepo elmaDetailRepo;

	@Autowired
	private CompanyService companyService;

	@PostConstruct
	public void update() throws IOException {

		if (updateInProgress) {
			return;
		}

		// Download
		updateInProgress = true;
		log.info("Downloading ELMA EHF spreadsheet...");
		URL website = new URL(spreadsheetSource);
		Path target = Path.of("." + File.separator + "elma.csv");
		try (InputStream in = website.openStream()) {
			Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
		}
		File file = target.toFile();
		log.info("Successfully downloaded CSV.");

		// Process
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
			int icdNumber = Integer.parseInt(record.get("Icd"));
			long orgNumber = Long.parseLong(record.get("identifier"));
			LocalDate date = LocalDate.parse(regDate, DateTimeFormatter.ofPattern("yyyy.MM.dd"));

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
		file.delete();
		log.info("Successfully processed CSV.");
		updateInProgress = false;

	}

}