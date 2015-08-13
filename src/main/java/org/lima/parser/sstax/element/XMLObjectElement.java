package org.lima.parser.sstax.element;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.lima.parser.sstax.XMLParser;
import org.lima.parser.sstax.util.GenericUtil;

public class XMLObjectElement<E> extends XMLAbstractSingleElement<E> {
	private Class<E> clazz;
	private Map<String, Method> setters;
	private Map<String, Field> fields;

	public XMLObjectElement (Class<E> clazz) {
		this.clazz = clazz;
		this.setters = new HashMap<String, Method>();
		for(Method m: clazz.getMethods()) {
			if(m.getName().startsWith("set") && m.getParameterTypes().length == 1) {
				setters.put(m.getName().substring(3).toLowerCase(), m);
			}
		}
		this.fields = new HashMap<String, Field>();
		for(Field f: clazz.getFields()) {
			if(f.isAccessible()) {
				fields.put(f.getName().toLowerCase(), f);
			}
		}
	}

	@Override
	protected E init() {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected E attribute(E obj, String key, String value)
			throws XMLStreamException {
		key = key.replaceAll("-", "_");
		try {
			obj = setStringValue(obj, key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	@Override
	protected E content(E obj, String content) throws XMLStreamException {
		try {
			obj = setStringValue(obj, "content", content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	@Override
	protected E childElement(E obj, String key, XMLParser parser)
			throws XMLStreamException {
		key = key.replaceAll("-", "_");
		try {
			obj = setElementValue(obj, key, parser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	private E setStringValue(E obj, String key, String value) throws Exception {
		Method setter = null;
		Field field = null;
		Class<?> fieldType = null;

		if((setter = setters.get(key)) != null) {
			fieldType = setter.getParameterTypes()[0];
			//System.out.println("string setter [" + key + "] fieldType[" + fieldType + "]");
			if(fieldType == String.class) {
				setter.invoke(obj, value);
				return obj;
			}
			else if(GenericUtil.isPrimitiveType(fieldType)) {
				Method primitiveMethod = GenericUtil.getPrimitiveValueOfStringMethod(fieldType);
				setter.invoke(obj, primitiveMethod.invoke(null, value));
				return obj;
			}
		}
		else if((field = fields.get(key)) != null) {
			fieldType = field.getType();
			//System.out.println("string field [" + key + "] fieldType[" + fieldType + "]");
			if(fieldType == String.class) {
				field.set(obj, value);
				return obj;
			}
			else if(GenericUtil.isPrimitiveType(fieldType)) {
				Method primitiveMethod = GenericUtil.getPrimitiveValueOfStringMethod(fieldType);
				field.set(obj, primitiveMethod.invoke(null, value));
				return obj;
			}
		}
		
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private E setElementValue(E obj, String key, XMLParser parser) throws Exception {
		Method setter = null;
		Field field = null;
		Class<?> fieldType = null;

		if((setter = setters.get(key)) != null) {
			fieldType = setter.getParameterTypes()[0];
			//System.out.println("elem setter [" + key + "] fieldType[" + fieldType + "]");
			if(fieldType == String.class) {
				setter.invoke(obj, new XMLStringElement().parse(parser));
				return obj;
			}
			else if(GenericUtil.isPrimitiveType(fieldType)) {
				setter.invoke(obj, new XMLPrimitiveElement(fieldType).parse(parser));
				return obj;
			}
			else if(Collection.class.isAssignableFrom(fieldType)) {
				Class<?> actualType = GenericUtil.getActualType(setter.getGenericParameterTypes()[0]);
				//System.out.println("	actualType[" + actualType+ "]");
				if(GenericUtil.isPrimitiveType(actualType)) {
					setter.invoke(obj, new XMLPrimitiveElement(actualType).parse(parser));
					return obj;
				}
				else if(Collection.class.isAssignableFrom(actualType)) {
					throw new XMLStreamException("not support: list in list");
				}
				else {
					setter.invoke(obj, new XMLListElement(null, new XMLObjectElement(actualType)).parse(parser));
					return obj;
				}
			}
			else {
				setter.invoke(obj, new XMLObjectElement(fieldType).parse(parser));
				return obj;
			}
		}
		else if((field = fields.get(key)) != null) {
			fieldType = field.getType();
			//System.out.println("elem field [" + key + "] fieldType[" + fieldType + "]");
			if(fieldType == String.class) {
				field.set(obj, new XMLStringElement().parse(parser));
				return obj;
			}
			else if(GenericUtil.isPrimitiveType(fieldType)) {
				field.set(obj, new XMLPrimitiveElement(fieldType).parse(parser));
				return obj;
			}
			else if(Collection.class.isAssignableFrom(fieldType)) {
				Class<?> actualType = GenericUtil.getActualType(field.getGenericType());
				if(GenericUtil.isPrimitiveType(actualType)) {
					field.set(obj, new XMLPrimitiveElement(actualType).parse(parser));
					return obj;
				}
				else if(Collection.class.isAssignableFrom(actualType)) {
					throw new XMLStreamException("not support: list in list");
				}
				else {
					field.set(obj, new XMLListElement(null, new XMLObjectElement(actualType)).parse(parser));
					return obj;
				}
			}
			else {
				field.set(obj, new XMLObjectElement(fieldType).parse(parser));
				return obj;
			}
		}
		return null;
	}

}