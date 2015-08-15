package org.lima.parser.sstax.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericUtil {

	public static boolean isPrimitiveType(Class<?> type) {
		return (getPrimitiveValueOfStringMethod(type) != null)?true:false;
	}

	public static Method getPrimitiveValueOfStringMethod(Class<?> type) {
		Method m = null;
		try {
			m = type.getDeclaredMethod("valueOf", String.class);
			if(!Modifier.isStatic(m.getModifiers())) {
				m = null;
			}
		} catch (Exception e) {
			m = null;
		}
		return m;
	}
	
	public static Class<?>[] getActualTypes(Type genericType) {
        if (ParameterizedType.class.isAssignableFrom(genericType.getClass())) {
            ParameterizedType type = (ParameterizedType) genericType;
            return ((Class<?>[]) type.getActualTypeArguments());
        }
        return null;
    }
}
