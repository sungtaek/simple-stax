package org.lima.parser.sstax;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class XMLParser {
	private XMLStreamReader xr = null;
	private int depth = 0;
	
	public XMLParser(String file) throws FileNotFoundException, XMLStreamException {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		this.xr = factory.createXMLStreamReader(new FileInputStream(new File(file)));
		this.depth = 0;
	}

	public XMLStreamReader getReader() {
		return xr;
	}
	public int getDepth() {
		return depth;
	}
	public int incDepth() {
		return ++depth;
	}
	public int decDepth() {
		return --depth;
	}

	public boolean seekNext(String key) throws XMLStreamException {
		while(xr.hasNext()) {
			switch(xr.next()) {
			case XMLStreamConstants.START_ELEMENT:
				depth++;
				if(key.equals(xr.getLocalName())) {
					return true;
				}
				break;
			case XMLStreamConstants.END_ELEMENT:
				depth--;
			}
		}
		return false;
	}
}