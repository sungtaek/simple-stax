package org.lima.parser.sstax.element;

import javax.xml.stream.XMLStreamException;

import org.lima.parser.sstax.XMLParser;

public interface XMLElement<E> {
	public E parse(XMLParser parser) throws XMLStreamException;
}
