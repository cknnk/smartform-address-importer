package cz.smartform.addressimporter.service;

import cz.smartform.addressimporter.dto.ParsedDataDto;
import cz.smartform.addressimporter.repository.CastObceRepository;
import cz.smartform.addressimporter.repository.ObecRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataImportService {

    private final XmlParserService xmlParserService;
    private final ObecRepository obecRepository;
    private final CastObceRepository castObceRepository;

    @Value("${app.download.url}")
    private String downloadUrl;

    @Transactional
    public void importData() {
        log.info("Starting address data import from: {}", downloadUrl);

        try {
            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(downloadUrl))
                    .GET()
                    .build();

            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            if (response.statusCode() != 200) {
                throw new IllegalStateException("HTTP error code: " + response.statusCode());
            }

            try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(response.body()))) {
                ZipEntry entry;
                boolean xmlFound = false;

                while ((entry = zis.getNextEntry()) != null) {
                    if (entry.getName().endsWith(".xml")) {
                        log.info("Found XML entry inside ZIP: {}", entry.getName());
                        xmlFound = true;

                        byte[] xmlBytes = zis.readAllBytes();

                        ParsedDataDto parsedData = xmlParserService.parse(new ByteArrayInputStream(xmlBytes));

                        log.info("Saving Obec: {} ({})", parsedData.getObec().getNazev(), parsedData.getObec().getKod());
                        obecRepository.save(parsedData.getObec());

                        log.info("Saving {} CastObce records...", parsedData.getCastiObce().size());
                        castObceRepository.saveAll(parsedData.getCastiObce());

                        break;
                    }
                }

                if (!xmlFound) {
                    throw new IllegalStateException("No XML file found inside downloaded ZIP archive");
                }
            }

            log.info("Data import successfully completed!");

        } catch (Exception e) {
            log.error("Failed to import data: {}", e.getMessage(), e);
            throw new RuntimeException("Import failed", e);
        }
    }
}