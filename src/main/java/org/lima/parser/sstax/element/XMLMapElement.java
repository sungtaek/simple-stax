package org.lima.parser.sstax.element;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.lima.parser.sstax.XMLParser;

public class XMLMapElement extends XMLAbstractSingleElement<Map<String,Object>> {
	private XMLElement<?> defaultElement = null;
	private Map<String, XMLElement<?>> childElements = new HashMap<String, XMLElement<?>>();
	private boolean enableAttr = false;

	public XMLMapElement setDefaultElement(XMLElement<?> element) {
		this.defaultElement = element;
		return this;
	}
	public XMLMapElement addChildElement(String key, XMLElement<?> element) {
		childElements.put(key, element);
		return this;
	}
	public XMLMapElement enableAttr(boolean enableAttr) {
		this.enableAttr = enableAttr;
		return this;
	}

	@Override
	protected Map<String, Object> init() {
		return new HashMap<String, Object>();
	}

	@Override
	protected Map<String, Object> attribute(Map<String, Object> obj,
			String key, String value) throws XMLStreamException {
		if(enableAttr) {
			obj.put(key, value);
		}
		return obj;
	}

	@Override
	protected Map<String, Object> content(Map<String, Object> obj,
			String content) throws XMLStreamException {
		obj.put("content", content);
		return null;
	}

	@Override
	protected Map<String, Object> childElement(Map<String, Object> obj,
			String key, XMLParser parser) throws XMLStreamException {
		XMLElement<?> childElement = childElements.get(key);
		if(childElement == null) {
			childElement = defaultElement;
		}

		if(childElement != null) {
			obj.put(key, childElement.parse(parser));
		}
		return obj;
	}

}
