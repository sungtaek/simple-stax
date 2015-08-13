package org.lima.parser.sstax.element;

import javax.xml.stream.XMLStreamException;

import org.lima.parser.sstax.XMLParser;

public class XMLIntegerElement extends XMLAbstractSingleElement<Integer> {
	@Override
	protected Integer init() {
		return null;
	}

	@Override
	protected Integer attribute(Integer obj, String key, String value)
			throws XMLStreamException {
		return null;
	}

	@Override
	protected Integer content(Integer obj, String content)
			throws XMLStreamException {
		return Integer.parseInt(content);
	}

	@Override
	protected Integer childElement(Integer obj, String key, XMLParser parser)
			throws XMLStreamException {
		return null;
	}

}
