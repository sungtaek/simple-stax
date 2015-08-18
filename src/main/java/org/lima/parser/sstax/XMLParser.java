package org.lima.parser.sstax;

import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.lima.parser.sstax.element.XMLListElement;
import org.lima.parser.sstax.element.XMLMapElement;
import org.lima.parser.sstax.element.XMLObjectElement;
import org.lima.parser.sstax.element.XMLPrimitiveElement;
import org.lima.parser.sstax.element.XMLStringElement;
import org.lima.parser.sstax.util.GenericUtil;

public class XMLParser {
	private XMLStreamReader xr = null;
	private int depth = 0;
	
	public XMLParser(InputStream stream) throws Exception {
		this(stream, null);
	}
	public XMLParser(InputStream stream, String encoding) throws Exception {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		if(encoding == null) {
			this.xr = factory.createXMLStreamReader(stream);
		}
		else {
			this.xr = factory.createXMLStreamReader(stream, encoding);
		}
		this.depth = 0;
	}

	public String parseString(String key) throws Exception {
		if(seekNext(key)) {
			return new XMLStringElement().parse(this);
		}
		return null;
	}

	public Integer parseInteger(String key) throws Exception {
		if(seekNext(key)) {
			return new XMLPrimitiveElement<Integer>(Integer.class).parse(this);
		}
		return null;
	}

	public Float parseFloat(String key) throws Exception {
		if(seekNext(key)) {
			return new XMLPrimitiveElement<Float>(Float.class).parse(this);
		}
		return null;
	}

	public Double parseDouble(String key) throws Exception {
		if(seekNext(key)) {
			return new XMLPrimitiveElement<Double>(Double.class).parse(this);
		}
		return null;
	}

	public Long parseLong(String key) throws Exception {
		if(seekNext(key)) {
			return new XMLPrimitiveElement<Long>(Long.class).parse(this);
		}
		return null;
	}

	public Boolean parseBoolean(String key) throws Exception {
		if(seekNext(key)) {
			return new XMLPrimitiveElement<Boolean>(Boolean.class).parse(this);
		}
		return null;
	}
	
	public Map<String, Object> parseMap(String key) throws Exception {
		if(seekNext(key)) {
			return new XMLMapElement(new XMLStringElement()).parse(this);
		}
		return null;
	}
	
	public <E> List<E> parseList(String key, Class<E> clazz) throws Exception {
		XMLListElement<E> listElement = getListElement(key, clazz);
		if(listElement != null) {
			return listElement.parse(this);
		}
		return null;
	}
	
	public <E> E parseObject(String key, Class<E> clazz) throws Exception {
		if(seekNext(key)) {
			return new XMLObjectElement<E>(clazz).parse(this);
		}
		return null;
	}
	
	public class XMLListIterator<E> implements Iterator<E> {
		private XMLParser parser;
		private XMLListElement<E> listElement;
		private E parsedElement;

		private XMLListIterator (XMLParser parser, XMLListElement<E> listElement) {
			this.parser = parser;
			this.listElement = listElement;
			this.parsedElement = null;
		}
		public boolean hasNext() {
			if(parsedElement != null) {
				return true;
			}
			else {
				try {
					parsedElement = listElement.parseNext(parser);
				} catch (XMLStreamException e) {
					e.printStackTrace();
				}
			}
			return (parsedElement!=null)?true:false;
		}
		public E next() {
			E nextElement = null;

			if(parsedElement != null) {
				nextElement = parsedElement;
				parsedElement = null;
			}
			else {
				try {
					nextElement = listElement.parseNext(parser);
				} catch (XMLStreamException e) {
					e.printStackTrace();
				}
			}
			return nextElement;
		}
		public void remove() { }
	}

	public <E> Iterator<E> getListIterator(String key, Class<E> clazz) throws Exception {
		Iterator<E> xmlListIterator = null;
		XMLListElement<E> listElement = getListElement(key, clazz);
		if(listElement != null) {
			xmlListIterator = new XMLListIterator<E>(this, listElement);
		}
		return xmlListIterator;
	}
	
	private <E> XMLListElement<E> getListElement(String key, Class<E> clazz) throws Exception {
		if(seekNext(key)) {
			if(Collection.class.isAssignableFrom(clazz)
					|| Map.class.isAssignableFrom(clazz)) {
				throw new Exception("not support type: " + clazz);
			}
			else if(GenericUtil.isPrimitiveType(clazz)) {
				return new XMLListElement<E>(null, new XMLPrimitiveElement<E>(clazz));
			}
			else {
				return new XMLListElement<E>(null, new XMLObjectElement<E>(clazz));
			}
		}
		return null;
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

	private boolean seekNext(String key) throws Exception {
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
