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
public class OwnerPerformanceModification {

	@Autowired
	MockMvc mvc;

	final MockHttpServletRequestBuilder editBuilder = post("/owners/1/edit").param("firstName", "John")
		.param("lastName", "Doe").param("address", "3647 University Street").param("city", "Toronto")
		.param("telephone", "+111111111111");
	final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
	final float NS_MS_DIV = 1000000;
	static PrintWriter writer;

	@BeforeAll
	static void setup(@Autowired MockMvc mvc) {
		try {
			// Avoid SpringBoot's knwon issue
			mvc.perform(post("/owners/new").param("firstName", "firstquery").param("lastName", "firstquery")
				.param("address", "firstquery").param("city", "firstquery").param("telephone", "+100000000"));

			writer = new PrintWriter("owner_modification_performance.csv");
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
	@Sql({ "classpath:schema.sql", "classpath:owners_performance/owners_10.sql" })
	void ownerModificationPerformanceTest_10entries() throws Exception {
		logTimeForPerformance(editBuilder, 10);
	}

	@Test
	@Sql({ "classpath:schema.sql", "classpath:owners_performance/owners_100.sql" })
	void ownerModificationPerformanceTest_100entries() throws Exception {
		logTimeForPerformance(editBuilder, 100);
	}

	@Test
	@Sql({ "classpath:schema.sql", "classpath:owners_performance/owners_500.sql" })
	void ownerModificationPerformanceTest_500entries() throws Exception {
		logTimeForPerformance(editBuilder, 500);
	}

	@Test
	@Sql({ "classpath:schema.sql", "classpath:owners_performance/owners_1000.sql" })
	void ownerModificationPerformanceTest_1000entries() throws Exception {
		logTimeForPerformance(editBuilder, 1000);
	}
}
