package com.pingidentity.tools.moustache;

import java.io.File;
import java.io.InputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
	//String path = ""
	private static final String validOutput = "-- Account Name: Ping Identity Demo\n -- Account Address: '1001 17th St. Suite 100'\n-- Account City: \"Denver\"\n-- Account Region: &CO&\n-- Account Country: US\n-- Account Postal: 80202;\n-- Account Domain: pingidentity.com\n";
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testApp() throws Exception
    {
    	InputStream template = getClass().getClassLoader().getResourceAsStream("test.template");
    	InputStream props = getClass().getClassLoader().getResourceAsStream("test.properties");
    	assertTrue("Template file not found", template != null);
    	assertTrue("Properties file not found", props != null);
    	
    	MoustacheCompiler mc = new MoustacheCompiler();
    	mc.setPropertiesFile(props);
    	mc.setTemplateFile(template);
    	String out = mc.compile();
    	assertNotNull("Compiler produced no output", out);
    	assertTrue( "Compiler produced empty output file", out.length() > 0 );
    	
    	assertEquals("Output does not match expected output",validOutput, out);
    }
}
