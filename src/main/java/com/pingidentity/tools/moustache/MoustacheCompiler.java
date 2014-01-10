package com.pingidentity.tools.moustache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jodd.props.Props;

import org.apache.commons.io.IOUtils;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class MoustacheCompiler {
	private boolean verbose = false;
    private InputStream templateFile;
    private InputStream propertiesFile;
	

	public MoustacheCompiler() {
		
	}
	
	public MoustacheCompiler(boolean verbose, InputStream templateFile,
			InputStream propertiesFile) {
		super();
		this.verbose = verbose;
		this.templateFile = templateFile;
		this.propertiesFile = propertiesFile;
	}


	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public InputStream getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(InputStream templateFile) {
		this.templateFile = templateFile;
	}

	public InputStream getPropertiesFile() {
		return propertiesFile;
	}

	public void setPropertiesFile(InputStream propertiesFile) {
		this.propertiesFile = propertiesFile;
	}

	public String compile() throws Exception {
		// Open and read the properties file
		Props props = null;

		try {
			props = loadConfig(propertiesFile);
		} catch (IOException e) {
			throw new Exception("Error reading properties file "
					+ propertiesFile, e);
		}

		// Generate a multi-map from the properties file
		Map<String, Object> valueMap = multiMap(propsToMap(props));
		if (valueMap == null) {
			throw new Exception(
					"Error creating multi-map from properties");
		}

		// Open and read the template file
		String template = null;
		try {
			template = loadTemplate(templateFile);
		} catch (IOException e) {
			throw new Exception("Error reading template file ", e);
		}

		// Compile the template file with the properties
		String compiled = compileTemplate(template, valueMap);
		if (compiled == null) {
			throw new Exception("Error compiling template");
		}
		return compiled;
	}
	
	/**
	 * Load the config file
	 * 
	 * @throws IOException
	 */
	private Props loadConfig(InputStream configPath) throws IOException {
		Props props = new Props();
		//InputStream configPath = new FileInputStream(configFile);
		if (configPath != null) {
			props.load(configPath);
			log("Instantiated Props instance");
		}
		return props;
	}

	private String loadTemplate(InputStream templatePath) throws Exception {

		//InputStream templatePath = new FileInputStream(templateFile);
		String template = null;
		if (templatePath != null) {
			template = IOUtils.toString(templatePath, "UTF-8");
			log("Loaded template file into string");
		}
		return template;
	}

	private Map<String, Object> propsToMap(Props props) {
		Map<String, Object> map = new HashMap<String, Object>();
		props.extractProps(map);
		for (String key : map.keySet()) {
			log(String.format("Property: [%s] [%s]", key,
					map.get(key)));
		}
		return map;
	}

	private String compileTemplate(String template, Map<String, Object> valueMap) {
		String compiledTemplate = null;
		Writer writer = new StringWriter();
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile(new StringReader(template), null);
		mustache.execute(writer, valueMap);
		compiledTemplate = writer.toString();
		
		return compiledTemplate;
	}

	/**
	 * Convert a flat map with keys that contain dot notation to a multi-level
	 * map. For example, given the map: {"key":"value", "key1.key":"value"}
	 * Generate: {"key":"value", "key1":{"key":value}}
	 * 
	 * @param map
	 *            the flat map to be parsed
	 * @return the multi-level map.
	 */
	private Map<String, Object> multiMap(Map<String, Object> map) {
		Map<String, Object> multi = new HashMap<String, Object>();

		// Get the set of keys
		Set<String> keys = map.keySet();
		for (String key : keys) {
			String value = (String) map.get(key);
			putValue(multi, key, value);
		}
		return multi;
	}

	/**
	 * Parse the key for dots, recursively call this routine if dots exist
	 * 
	 * @param map
	 * @param key
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	private void putValue(final Map<String, Object> map, String key,
			String value) {

		// See if the key has any dots
		int idx = key.indexOf(".");
		if (idx == -1) {
			// No dots. Just stuff the value
			map.put(key, value);
			return;
		}
		// Get the leading key
		String subKey = key.substring(idx + 1);
		key = key.substring(0, idx);

		Map<String, Object> subValues = null;
		// See if the key already exists
		if (map.containsKey(key)) {
			subValues = (Map<String, Object>) map.get(key);
		} else {
			subValues = new HashMap<String, Object>();
			map.put(key, subValues);
		}
		putValue(subValues, subKey, value);
	}

	private void log(String str) {
		if(!verbose) 
			return;
		System.out.println(str);
	}

}
