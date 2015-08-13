package org.lima.parser.sstax.element;

import javax.xml.stream.XMLStreamException;

import org.lima.parser.sstax.XMLParser;

public class XMLStringElement extends XMLAbstractSingleElement<String> {
	@Override
	protected String init() {
		return null;
	}

	@Override
	protected String attribute(String obj, String key, String value)
			throws XMLStreamException {
		return null;
	}

	@Override
	protected String content(String obj, String content)
			throws XMLStreamException {
		return content;
	}

	@Override
	protected String childElement(String obj, String key, XMLParser parser)
			throws XMLStreamException {
		return null;
	}
}
