package com.pingidentity.tools.moustache.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "entry")
public class Entry {
	private String properties;
	private String template;
	private String output;
	
	public Entry() {
		
	}
	
	public Entry(String properties, String template, String output) {
		this.properties = properties;
		this.template = template;
		this.output = output;
	}
	
	public String getProperties() {
		return properties;
	}
	
	public void setProperties(String properties) {
		this.properties = properties;
	}
	
	public String getTemplate() {
		return template;
	}
	
	public void setTemplate(String template) {
		this.template = template;
	}
	
	public String getOutput() {
		return output;
	}
	
	public void setOutput(String output) {
		this.output = output;
	}
		
}
