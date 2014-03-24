package com.pingidentity.tools.moustache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.pingidentity.tools.moustache.xml.Script;
import com.pingidentity.tools.moustache.xml.Entry;

public class MoustacheScript {
	InputStream script=null;
	boolean verbose = false;
	
	public MoustacheScript() {
		
	}
	public MoustacheScript(InputStream scriptFile) {
		this.script = scriptFile;
	}


	public InputStream getScriptFile() {
		return script;
	}


	public void setScriptFile(InputStream script) {
		this.script = script;
	}

	public void execute() throws Exception {
		// Open the file
		try {
		Script entries = unMarshall(script);
			
		for(Entry entry : entries.getEntries()) {
			
			File properties = new File(entry.getProperties());			
			if(!properties.exists()) {
    			throw new Exception("Cannot find properties file: " + properties);
    		}			
			
			File template = new File(entry.getTemplate());
			if(!template.exists()) {
    			throw new Exception("Cannot find template file: " + template);
    		}	
			
    		File output = new File(entry.getOutput());
    		
        	InputStream templateStream = new FileInputStream(template); 
        	InputStream propsStream = new FileInputStream(properties);
        	MoustacheCompiler mc = new MoustacheCompiler(verbose, templateStream, propsStream);
        	String compiledTemplate = mc.compile();
        	MoustacheCompiler.writeFile(output, compiledTemplate);

		}
		
		} catch(JAXBException ex) {
			throw new Exception("Cannot process file: " + script, ex);			
		}
	}

	private Script unMarshall(InputStream script) throws JAXBException
	{
		Script entries = null;
	    JAXBContext jaxbContext = JAXBContext.newInstance(Script.class);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	     
	    //We had written this file in marshalling example
	    entries = (Script) jaxbUnmarshaller.unmarshal( script );
	    return entries;
	}

	
}
