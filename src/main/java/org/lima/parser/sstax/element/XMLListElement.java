package org.lima.parser.sstax.element;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.lima.parser.sstax.XMLParser;

public class XMLListElement<E> implements XMLCollectionElement<E> {
	private String childKey;
	private XMLElement<E> childElement;
	
	public XMLListElement(String childKey, XMLElement<E> childElement) {
		this.childKey = childKey;
		this.childElement = childElement;
	}

	public List<E> parse(XMLParser parser) throws XMLStreamException {
		List<E> obj = new ArrayList<E>();
		E value = null;
		while((value = parseNext(parser)) != null) {
			obj.add(value);
		}
		return obj;
	}
	
	public E parseNext(XMLParser parser) throws XMLStreamException {
		XMLStreamReader xr = parser.getReader();
		int curDepth = parser.getDepth();
		//System.out.println("XMLListElement parseNext(" + parser.getDepth() + ")");
		
		while(xr.hasNext()) {
			switch(xr.next()) {
			case XMLStreamConstants.START_ELEMENT:
				parser.incDepth();
				//System.out.println("	XMLListElement found Element(" + parser.getDepth() + "," + xr.getLocalName() + ")");
				if(parser.getDepth() == curDepth+1) {
					if(childKey == null || childKey.equals(xr.getLocalName())) {
						//System.out.println("	XMLListElement parse...");
						return childElement.parse(parser);
					}
				}
				else {
					//System.out.println("	XMLListElement skip...");
				}
				break;
			case XMLStreamConstants.END_ELEMENT:
				parser.decDepth();
				if(parser.getDepth() < curDepth) {
					return null;
				}
				break;
			}
		}
		return null;
	}
}