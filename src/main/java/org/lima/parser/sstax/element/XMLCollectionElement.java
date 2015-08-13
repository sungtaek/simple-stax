package org.lima.parser.sstax.element;

import java.util.Collection;

import javax.xml.stream.XMLStreamException;

import org.lima.parser.sstax.XMLParser;

public interface XMLCollectionElement<E> extends XMLElement<Collection<E>> {
	public E parseNext(XMLParser parser) throws XMLStreamException;
}
