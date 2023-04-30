package org.springframework.samples.petclinic.testrunner;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

/*
 * @RunWith(Cucumber.class)
 *
 * @SpringBootTest
 *
 * @CucumberOptions(features = "src/test/resources", glue =
 * "org.springframework.samples.petclinic.acceptance", plugin = { "pretty" })
 * public class CucumberFeaturesTestRunner {
 *
 * }
 */

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
public class CucumberTest {

}