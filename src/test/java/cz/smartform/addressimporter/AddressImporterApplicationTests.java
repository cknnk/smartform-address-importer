package cz.smartform.addressimporter;

import cz.smartform.addressimporter.entity.CastObce;
import cz.smartform.addressimporter.entity.Obec;
import cz.smartform.addressimporter.repository.CastObceRepository;
import cz.smartform.addressimporter.repository.ObecRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AddressImporterApplicationTests {

	@Autowired
	private ObecRepository obecRepository;

	@Autowired
	private CastObceRepository castObceRepository;

	@Test
	void testDataImportedCorrectly() {
		Optional<Obec> obec = obecRepository.findById(573060);
		assertTrue(obec.isPresent(), "Obec Kopidlno must exist");
		assertEquals("Kopidlno", obec.get().getNazev());

		List<CastObce> casti = castObceRepository.findAll();
		assertEquals(5, casti.size(), "There should be exactly 5 CastObce records");

		assertTrue(casti.stream().allMatch(c -> c.getObecKod().equals(573060)));
	}
}