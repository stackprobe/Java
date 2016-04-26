package charlotte.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
		return invokeDeclaredMethod(classObj, methodName, instance, params, getTypes(params));
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
		Constructor<?> ctor = getDeclaredCtor(classObj, paramTypes);
		Object instance = ctor.newInstance(params);
		return instance;
	}

	public static Constructor<?> getDeclaredCtor(Class<?> classObj, Class<?>[] paramTypes) {
		for(Constructor<?> ctor : classObj.getDeclaredConstructors()) {
			Class<?>[] ctorParamTypes = ctor.getParameterTypes();

			if(isSameTypeArray(paramTypes, ctorParamTypes)) {
				ctor.setAccessible(true);
				return ctor;
			}
		}
		return null;
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
		for(int index = 0; index < t1.length; index++) {
			if(isSameType(t1[index], t2[index]) == false) {
				return false;
			}
		}
		return true;
	}

	private static boolean isSameType(Class<?> t1, Class<?> t2) {
		return univPrimitive(t1.getName()).equals(univPrimitive(t2.getName()));
	}

	// XXX m(int a), m(Integer a) みたいなメソッド、コンストラクタが混在しているとアウト

	private static String univPrimitive(String name) {
		//System.out.println("name.1: " + name); // test
		name = univPrimitive_main(name);
		//System.out.println("name.2: " + name); // test
		return name;
	}

	private static String univPrimitive_main(String name) {
		for(String[] pair : _prmtvPairs) {
			if(name.equals(pair[0])) {
				return pair[1];
			}
		}
		for(String[] pair : _prmtvArrPairs) {
			if(name.endsWith(pair[0])) {
				return name.substring(0, name.length() - pair[0].length()) + pair[1];
			}
		}
		return name;
	}

	private static List<String[]> _prmtvPairs;
	private static List<String[]> _prmtvArrPairs;

	static {
		_prmtvPairs = new ArrayList<String[]>();

		addPrmtvPair(_prmtvPairs, Boolean.class, boolean.class);
		addPrmtvPair(_prmtvPairs, Byte.class, byte.class);
		addPrmtvPair(_prmtvPairs, Character.class, char.class);
		addPrmtvPair(_prmtvPairs, Integer.class, int.class);
		addPrmtvPair(_prmtvPairs, Long.class, long.class);
		addPrmtvPair(_prmtvPairs, Float.class, float.class);
		addPrmtvPair(_prmtvPairs, Double.class, double.class);

		_prmtvArrPairs = new ArrayList<String[]>();

		addPrmtvPair(_prmtvArrPairs, new Boolean[0].getClass(), new boolean[0].getClass());
		addPrmtvPair(_prmtvArrPairs, new Byte[0].getClass(), new byte[0].getClass());
		addPrmtvPair(_prmtvArrPairs, new Character[0].getClass(), new char[0].getClass());
		addPrmtvPair(_prmtvArrPairs, new Integer[0].getClass(), new int[0].getClass());
		addPrmtvPair(_prmtvArrPairs, new Long[0].getClass(), new long[0].getClass());
		addPrmtvPair(_prmtvArrPairs, new Float[0].getClass(), new float[0].getClass());
		addPrmtvPair(_prmtvArrPairs, new Double[0].getClass(), new double[0].getClass());
	}

	private static void addPrmtvPair(List<String[]> dest, Class<?> t1, Class<?> t2) {
		dest.add(new String[] { t1.getName(), t2.getName() });
	}

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

	public static class FieldData {
		private Field _field;
		private Object _instance; // null as static

		public FieldData(Field field) {
			_field = field;
		}

		public FieldData(Field field, Object instance) {
			_field = field;
			_instance = instance;
		}

		public Field getField() {
			return _field;
		}

		public Object getInstance() {
			return _instance;
		}
	}

	public static Object getObject(FieldData fieldData) throws Exception {
		return getObject(fieldData.getField(), fieldData.getInstance());
	}

	public static void setObject(FieldData fieldData, Object value) throws Exception {
		fieldData.getField().set(fieldData.getInstance(), value);
	}

	public static boolean instanceOf(Object instance, Class<?> expectClassObj) {
		return instanceOf(instance, expectClassObj.getName());
	}

	public static boolean instanceOf(Object instance, String expectClassName) {
		Class<?> classObj = instance.getClass();

		for(; ; ) {
			if(classObj.getName().equals(expectClassName)) {
				return true;
			}
			classObj = classObj.getSuperclass();

			if(classObj == null) {
				return false;
			}
		}
	}
}
