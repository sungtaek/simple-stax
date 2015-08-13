package org.lima.parser.sstax.element;

import java.lang.reflect.Method;

import javax.xml.stream.XMLStreamException;

import org.lima.parser.sstax.XMLParser;
import org.lima.parser.sstax.util.GenericUtil;

public class XMLPrimitiveElement<E> extends XMLAbstractSingleElement<E> {
	private Class<E> clazz;

	public XMLPrimitiveElement(Class<E> clazz) throws Exception {
		this.clazz = clazz;
		if(!GenericUtil.isPrimitiveType(clazz)) {
			throw new Exception("invalid clazz: is not generic type class");
		}
	}

	@Override
	protected E init() {
		return null;
	}
	@Override
	protected E attribute(Object obj, String key, String value)
			throws XMLStreamException {
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected E content(Object obj, String content)
			throws XMLStreamException {
		Method gm = GenericUtil.getPrimitiveValueOfStringMethod(clazz);
		if(gm != null) {
			try {
				return (E)gm.invoke(null, content);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected E childElement(Object obj, String key, XMLParser parser)
			throws XMLStreamException {
		return null;
	}
}
