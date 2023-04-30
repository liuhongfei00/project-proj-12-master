package org.springframework.samples.petclinic.performance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@SpringBootTest
@AutoConfigureMockMvc
public class PetPerformanceAddition {

	@Autowired
	MockMvc mvc;

	final MockHttpServletRequestBuilder createBuilder = post("/owners/1/pets/new").param("name", "Miaou")
		.param("birthDate", "2014-10-01").param("type", "cat");

	final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
	final float NS_MS_DIV = 1000000;
	static PrintWriter writer;

	@BeforeAll
	static void setup(@Autowired MockMvc mvc) {
		try {
			// Avoid SpringBoot's knwon issue
			mvc.perform(post("/owners/1/pets/new").param("name", "Miaou").param("birthDate", "2014-10-01").param("type",
				"cat"));

			writer = new PrintWriter("pet_addition_performance.csv");
			writer.println("TimeStamp,Number of Elements,QueryTime (ms)");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterAll
	static void tearDown() {
		writer.close();
	}

	void logTimeForPerformance(MockHttpServletRequestBuilder builder, int numOfInstances) throws Exception {

		LocalDateTime startTime = LocalDateTime.now();

		// Record the start time
		long start = System.nanoTime();
		// Perform the addition or modification
		mvc.perform(builder);
		// Record the end time
		long end = System.nanoTime();

		//
		double queryTime = (end - start) / NS_MS_DIV;

		System.out.println(
			formatter.format(startTime) + ": " + numOfInstances + " entry creation time " + queryTime + "(ms)");
		// Append the recorded time to the output file
		writer.println(formatter.format(startTime) + "," + numOfInstances + "," + queryTime);
	}

	@Test
	@Sql({ "classpath:schema.sql", "classpath:pets_performance/pets_10.sql" })
	void petAdditionPerformanceTest_10entries() throws Exception {
		logTimeForPerformance(createBuilder, 10);
	}

	@Test
	@Sql({ "classpath:schema.sql", "classpath:pets_performance/pets_100.sql" })
	void petAdditionPerformanceTest_100entries() throws Exception {
		logTimeForPerformance(createBuilder, 100);
	}

	@Test
	@Sql({ "classpath:schema.sql", "classpath:pets_performance/pets_500.sql" })
	void petAdditionPerformanceTest_500entries() throws Exception {
		logTimeForPerformance(createBuilder, 500);
	}

	@Test
	@Sql({ "classpath:schema.sql", "classpath:pets_performance/pets_1000.sql" })
	void petAdditionPerformanceTest_1000entries() throws Exception {
		logTimeForPerformance(createBuilder, 1000);
	}
}
