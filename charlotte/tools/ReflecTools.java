package charlotte.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflecTools {
	/**
	 *
	 * @param className ex. "charlotte.tools.ReflecTools"
	 * @param methodName ex. "invokeDeclaredMethod"
	 * @param instance null as static
	 * @param params ex. new Object[]{ new Integer(2), Boolean.TRUE }
	 * @return 実行したメソッドの戻り値
	 * @throws Exception
	 */
	public static Object invokeDeclaredMethod(String className, String methodName, Object instance, Object[] params) throws Exception {
		Class<?> classObj = Class.forName(className);
		return invokeDeclaredMethod(classObj, methodName, instance, params);
	}

	public static Object invokeDeclaredMethod(Class<?> classObj, String methodName, Object instance, Object[] params) throws Exception {
		return invokeDeclaredMethod(classObj, methodName, params, getTypes(params));
	}

	public static Object invokeDeclaredMethod(Class<?> classObj, String methodName, Object instance, Object[] params, Class<?>[] paramTypes) throws Exception {
		Method methodObj = getDeclaredMethod(classObj, methodName, paramTypes);
		return methodObj.invoke(instance, params);
	}

	public static Method getDeclaredMethod(Class<?> classObj, String methodName, Class<?>[] paramTypes) {
		for(; ; ) {
			for(Method mo : classObj.getDeclaredMethods()) {
				if(methodName.equals(mo.getName())) {
					Class<?>[] moParamTypes = mo.getParameterTypes();

					if(isSameTypeArray(paramTypes, moParamTypes)) {
						mo.setAccessible(true);
						return mo;
					}
				}
			}
			classObj = classObj.getSuperclass();

			if(classObj == null) {
				return null;
			}
		}
	}

	public static Object invokeDeclaredCtor(Class<?> classObj, Object[] params) throws Exception {
		return invokeDeclaredCtor(classObj, params, getTypes(params));
	}

	public static Object invokeDeclaredCtor(Class<?> classObj, Object[] params, Class<?>[] paramTypes) throws Exception {
		Constructor<?> ctor = classObj.getDeclaredConstructor(paramTypes);
		ctor.setAccessible(true);
		Object instance = ctor.newInstance(params);
		return instance;
	}

	private static Class<?>[] getTypes(Object[] params) {
		Class<?>[] types = new Class<?>[params.length];

		for(int index = 0; index < params.length; index++) {
			types[index] = params[index].getClass();
		}
		return types;
	}

	private static boolean isSameTypeArray(Class<?>[] t1, Class<?>[] t2) {
		if(t1.length != t2.length) {
			return false;
		}
		// TODO
		/*
		for(int index = 0; index < t1.length; index++) {
			if(isSameType(t1[index], t2[index]) == false) {
				return false;
			}
		}
		*/
		return true;
	}

	// TODO [class java.lang.Integer, class java.lang.Boolean] <-> [int, boolean]
	/*
	private static boolean isSameType(Class<?> t1, Class<?> t2) {
		return t1.getName().equals(t2.getName());
	}
	*/

	public static Field getDeclaredField(Class<?> classObj, String fieldName) throws Exception {
		for(; ; ) {
			for(Field field : classObj.getDeclaredFields()) {
				String tmp = field.getName();

				if(tmp.equals(fieldName)) {
					field.setAccessible(true);
					return field;
				}
			}
			classObj = classObj.getSuperclass();

			if(classObj == null) {
				return null;
			}
		}
	}

	public static Object getObject(Field field, Object instance) throws Exception {
		return field.get(instance);
	}

	public static void setObject(Field field, Object instance, Object value) throws Exception {
		field.set(instance, value);
	}
}
