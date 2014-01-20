package com.pingidentity.tools.moustache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

/**
 * Hello world!
 *
 */
public class Main 
{
    private Options options = null;
    private boolean verbose = false;
    private File template;
    private File props;
    private File compiled;
    private File script;
    
    public static void main( String[] args ) throws Exception
    {
        Main app = new Main();
        app.run(args);
    }
    
    
    public void run(String[] args) throws Exception {
    	init();
    	processCommands(args);
    }
    
    private void init() {
    	options = initOptions();
    }
    
    private Options initOptions() {
    	// create Options object
    	Options options = new Options();

    	@SuppressWarnings("static-access")
		Option templateFile = OptionBuilder.withArgName( "file" )
                .hasArg()
                .withDescription(  "file with moustache handlebars to process" )
                .create( "template");
    	@SuppressWarnings("static-access")
		Option propsFile = OptionBuilder.withArgName( "file" )
                .hasArg()
                .withDescription(  "Jodd.Props style properties file" )
                .create( "props");
       	@SuppressWarnings("static-access")
		Option compiledFile = OptionBuilder.withArgName( "file" )
                .hasArg()
                .withDescription(  "[optional] output file to create.  If not present, stdout." )
                .create( "out");
       	@SuppressWarnings("Static-access")
       	Option scriptFile = OptionBuilder.withArgName("xml")
       		.hasArg()
       		.withDescription("Execute from an XML scritp, rather than CLI arguments.  ")
       		.create("script");
       	
    	options.addOption("version",false, "Display version and exit");
    	options.addOption("help",false, "Display this message and exit");
    	options.addOption("verbose", false, "Show status messages during processing");
    	options.addOption(templateFile);
    	options.addOption(propsFile);
    	options.addOption(compiledFile);
    	options.addOption(scriptFile);
    	return options;
    }
    
    private void processCommands(String[] args) throws Exception {
    	CommandLineParser parser = new GnuParser();
    	CommandLine cmd = parser.parse( options, args);
    	if(cmd.hasOption("help")) {
    		showHelp();
    		return;
    	}
    	if(cmd.hasOption("version")) {
    		showVersion();
    		return;
    	}
    	if(cmd.hasOption("verbose")) {
    		verbose = true;
    	}
    	if(cmd.hasOption("script")) {
    		// If a script was provided, ignore all other CLI commands,
    		// and just execute the script.
    		script = new File(cmd.getOptionValue("script"));
    		if(!script.exists()) {
    			throw new Exception("Cannot find script file: " + script);
    		}
    		
    		return;
    	}
    	if(cmd.hasOption("template")) {
    		template = new File(cmd.getOptionValue("template"));
    		if(!template.exists()) {
    			throw new Exception("Cannot find template file: " + template);
    		}
    	} else {
    		showHelp();
    		return;
    	}
    	if(cmd.hasOption("props")) {
    		props = new File(cmd.getOptionValue("props"));
    		if(!props.exists()) {
    			throw new Exception("Cannot find properties file: " + props);
    		}
    	} else {
    		showHelp();
    		return;
    	}
    	
    	InputStream templateStream = new FileInputStream(template); 
    	InputStream propsStream = new FileInputStream(props);
    	MoustacheCompiler mc = new MoustacheCompiler(verbose, templateStream, propsStream);
    	String compiledTemplate = mc.compile();
    	
    	if(cmd.hasOption("out")) {
    		compiled = new File(cmd.getOptionValue("out"));
    		MoustacheCompiler.writeFile(compiled, compiledTemplate);
    	} else {
    		System.out.println(compiledTemplate);
    	}
    	
    }
    
    
    private void showHelp() {
    	// automatically generate the help statement
    	HelpFormatter formatter = new HelpFormatter();
    	formatter.printHelp( "java -cp moustache.jar Main", options );
    }
    private void showVersion() throws IOException {
    	
    	InputStream path = getClass().getClassLoader().getResourceAsStream("version.properties");
    	Properties versionProps = new Properties();
    	versionProps.load(path);
    	System.out.println("version " + versionProps.getProperty("version"));
    }

}
