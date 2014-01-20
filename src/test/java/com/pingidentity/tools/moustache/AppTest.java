package com.pingidentity.tools.moustache;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.IOUtils;

import com.pingidentity.tools.moustache.xml.Script;
import com.pingidentity.tools.moustache.xml.Entry;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	// String path = ""
	private static final String validOutput = "-- Account Name: Ping Identity Demo \n-- Account Full Address: '1001 17th St. Suite 100' \n-- Account City: \"Denver\"\n-- Account Region: &CO&\n-- Account Country: US\n-- Account Postal: 80202;\n-- Account Domain: pingidentity.com\n";

	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 * 
	 * @throws Exception
	 */
	public void testApp() throws Exception {
		InputStream template = getClass().getClassLoader().getResourceAsStream(
				"test.template");
		InputStream props = getClass().getClassLoader().getResourceAsStream(
				"test.properties");
		assertTrue("Template file not found", template != null);
		assertTrue("Properties file not found", props != null);

		MoustacheCompiler mc = new MoustacheCompiler();
		mc.setPropertiesFile(props);
		mc.setTemplateFile(template);
		String out = mc.compile();
		assertNotNull("Compiler produced no output", out);
		assertTrue("Compiler produced empty output file", out.length() > 0);

		assertEquals("Output does not match expected output", validOutput, out);
	}

	/**
	 * Test the use of an xml script as the input to MoustacheCompiler.
	 * @throws Exception
	 */
	public void testScript() throws Exception {
		// String path = String.format("%s/test", getRelativePath());
		String propertiesFile = getAbsoluteResource("/test.properties");
		String path = getAbsoluteResourcePath(propertiesFile);

		String templateFile = getAbsoluteResource("/test.template");
		String scriptFile = String.format("%s/test.xml", path);
		String outputFile = String.format("%s/test.output", path);
		List<Entry> entryList = new ArrayList<Entry>();
		Entry entry = new Entry();
		entry.setTemplate(templateFile);
		entry.setProperties(propertiesFile);		
		entry.setOutput(outputFile);
		entryList.add(entry);
		Script entries = new Script();
		entries.setEntries(entryList);
		marshall(entries, scriptFile);

		InputStream scriptStream = new FileInputStream(scriptFile);
		MoustacheScript script = new MoustacheScript(scriptStream);
		script.execute();
		
		File compiledFile = new File(outputFile);
		assertTrue("The compiled output file was not found",compiledFile.exists());
		InputStream compiledStream = new FileInputStream(compiledFile);
		String compiledText = IOUtils.toString(compiledStream, "UTF-8");
		assertEquals("Output does not match expected output", validOutput, compiledText);

	}

	/**
	 * Get the path of the test.properties file.
	 * 
	 * @return
	 */
	private String getAbsoluteResource(String resource) {
		URL location = this.getClass().getResource(resource);
		String fullPath = location.getPath();
		return fullPath;
	}

	private String getAbsoluteResourcePath(String fullPath) {
		int idx = fullPath.lastIndexOf('/');
		String path = fullPath.substring(0, idx);
		return path;

	}

	private static void marshall(Script entries, String path)
			throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Script.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		// Marshal the employees list in file
		File outFile = new File(path);
		jaxbMarshaller.marshal(entries, outFile);

	}
}
