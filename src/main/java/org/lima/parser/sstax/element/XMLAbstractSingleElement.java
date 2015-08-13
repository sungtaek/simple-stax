package org.lima.parser.sstax.element;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.lima.parser.sstax.XMLParser;

public abstract class XMLAbstractSingleElement<E> implements XMLElement<E> {

	public E parse(XMLParser parser) throws XMLStreamException {
		XMLStreamReader xr = parser.getReader();
		E obj, new_obj;
		int curDepth = parser.getDepth();

		obj = init();
		new_obj = null;
		
		for(int i=0; i<xr.getAttributeCount(); i++) {
			new_obj = attribute(obj, xr.getAttributeLocalName(i), xr.getAttributeValue(i));
			if(new_obj != null) {
				obj = new_obj;
			}
		}

		while(xr.hasNext()) {
			switch(xr.next()) {
			case XMLStreamConstants.CHARACTERS:
				new_obj = content(obj, xr.getText());
				if(new_obj != null) {
					obj = new_obj;
				}
				break;
			case XMLStreamConstants.START_ELEMENT:
				parser.incDepth();
				new_obj = childElement(obj, xr.getLocalName(), parser);
				if(new_obj != null) {
					obj = new_obj;
				}
				break;
			case XMLStreamConstants.END_ELEMENT:
				parser.decDepth();
				if(parser.getDepth() < curDepth) {
					return obj;
				}
			}
		}

		return obj;
	}

	abstract protected E init();
	abstract protected E attribute(E obj, String key, String value) throws XMLStreamException;
	abstract protected E content(E obj, String content) throws XMLStreamException;
	abstract protected E childElement(E obj, String key, XMLParser parser) throws XMLStreamException;
}