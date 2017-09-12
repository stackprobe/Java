package charlotte.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

		if(methodObj == null) {
			throw new Exception("クラス [" + classObj + "] に、メソッド [" + methodName + "] は定義されていません。");
		}
		return methodObj.invoke(instance, params);
	}

	public static Method getDeclaredMethod(Class<?> classObj, String methodName, Class<?>[] paramTypes) {
		for(; ; ) {
			for(int fuzzyLv = 0; fuzzyLv <= 1; fuzzyLv++) {
				for(Method mo : classObj.getDeclaredMethods()) {
					if(methodName.equals(mo.getName())) {
						Class<?>[] moParamTypes = mo.getParameterTypes();

						if(isSameTypeArray(paramTypes, moParamTypes, fuzzyLv)) {
							mo.setAccessible(true);
							return mo;
						}
					}
				}
			}
			classObj = classObj.getSuperclass();

			if(classObj == null) {
				return null;
			}
		}
	}

	public static Object invokeDeclaredCtor(String className, Object[] params) throws Exception {
		return invokeDeclaredCtor(Class.forName(className), params);
	}

	public static Object invokeDeclaredCtor(Class<?> classObj, Object[] params) throws Exception {
		return invokeDeclaredCtor(classObj, params, getTypes(params));
	}

	public static Object invokeDeclaredCtor(Class<?> classObj, Object[] params, Class<?>[] paramTypes) throws Exception {
		Constructor<?> ctor = getDeclaredCtor(classObj, paramTypes);

		if(ctor == null) {
			throw new Exception("クラス [" + classObj + "] のコンストラクタが見つかりません。");
		}
		Object instance = ctor.newInstance(params);
		return instance;
	}

	public static Constructor<?> getDeclaredCtor(Class<?> classObj, Class<?>[] paramTypes) {
		for(int fuzzyLv = 0; fuzzyLv <= 1; fuzzyLv++) {
			for(Constructor<?> ctor : classObj.getDeclaredConstructors()) {
				Class<?>[] ctorParamTypes = ctor.getParameterTypes();

				if(isSameTypeArray(paramTypes, ctorParamTypes, fuzzyLv)) {
					ctor.setAccessible(true);
					return ctor;
				}
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

	private static boolean isSameTypeArray(Class<?>[] t1, Class<?>[] t2, int fuzzyLv) {
		if(t1.length != t2.length) {
			return false;
		}
		for(int index = 0; index < t1.length; index++) {
			if(isSameType(t1[index], t2[index], fuzzyLv) == false) {
				return false;
			}
		}
		//System.out.println("fuzzyLv: " + fuzzyLv); // test
		return true;
	}

	private static boolean isSameType(Class<?> t1, Class<?> t2, int fuzzyLv) {
		String n1 = t1.getName();
		String n2 = t2.getName();

		if(1 <= fuzzyLv) {
			n1 = univPrimitive(n1);
			n2 = univPrimitive(n2);
		}
		return n1.equals(n2);
	}

	private static String univPrimitive(String name) {
		for(String[] pair : _primitivePairs) {
			if(name.equals(pair[0])) {
				return pair[1];
			}
		}
		return name;
	}

	private static List<String[]> _primitivePairs;

	static {
		_primitivePairs = new ArrayList<String[]>();

		addPrimitivePair(_primitivePairs, Boolean.class, boolean.class);
		addPrimitivePair(_primitivePairs, Byte.class, byte.class);
		addPrimitivePair(_primitivePairs, Character.class, char.class);
		addPrimitivePair(_primitivePairs, Integer.class, int.class);
		addPrimitivePair(_primitivePairs, Long.class, long.class);
		addPrimitivePair(_primitivePairs, Float.class, float.class);
		addPrimitivePair(_primitivePairs, Double.class, double.class);
	}

	private static void addPrimitivePair(List<String[]> dest, Class<?> t1, Class<?> t2) {
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

	public static Field getField(Class<?> classObj, String fieldName) throws Exception {
		Field field = getDeclaredField(classObj, fieldName);

		if(field == null) {
			throw new Exception("クラス [" + classObj + "] に、フィールド [" + fieldName + "] は定義されていません。");
		}
		return field;
	}

	public static Object getValue(Class<?> classObj, String fieldName, Object instance) throws Exception {
		return getObject(getField(classObj, fieldName), instance);
	}

	public static void setValue(Class<?> classObj, String fieldName, Object instance, Object value) throws Exception {
		setObject(getField(classObj, fieldName), instance, value);
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
		return typeOf(instance.getClass(), expectClassName);
	}

	public static boolean typeOf(Class<?> classObj, Class<?> expectClassObj) {
		return typeOf(classObj, expectClassObj.getName());
	}

	public static boolean typeOf(Class<?> classObj, String expectClassName) {
		QueueData<Class<?>> classObjs = new QueueData<Class<?>>();

		classObjs.add(classObj);

		while(1 <= classObjs.size()) {
			classObj = classObjs.poll();

			if(classObj.getName().equals(expectClassName)) {
				return true;
			}

			{
				Class<?>[] interfaceObjs = classObj.getInterfaces();

				if(interfaceObjs != null) {
					for(Class<?> interfaceObj : interfaceObjs) {
						classObjs.add(interfaceObj);
					}
				}
			}

			classObj = classObj.getSuperclass();

			if(classObj != null) {
				classObjs.add(classObj);
			}
		}
		return false;
	}

	public static String getDir(Class<?> critClassObj, Package p) {
		return FileTools.combine(getBinDir(critClassObj), p.getName().replace('.', '/'));
	}

	public static String getFile(Class<?> critClassObj, Class<?> classObj) {
		return FileTools.combine(getBinDir(critClassObj), classObj.getName().replace('.', '/')) + ".class";
	}

	public static Class<?> getClass(Class<?> critClassObj, String file) throws Exception {
		file = FileTools.eraseRoot(file, getBinDir(critClassObj));
		file = StringTools.removeEndsWithIgnoreCase(file, ".class");
		file = file.replace('/', '.');
		System.out.println("getClass_name: " + file); // test
		return Class.forName(file);
	}

	public static Package getPackage(Class<?> critClassObj, String dir) throws Exception {
		dir = FileTools.eraseRoot(dir, getBinDir(critClassObj));
		dir = dir.replace('/', '.');
		System.out.println("getPackage_name: " + dir); // test
		return Package.getPackage(dir);
	}

	public static String getBinDir(Class<?> critClassObj) {
		String className = critClassObj.getSimpleName();
		URL url = critClassObj.getResource(className + ".class");
		String path = url.getPath();

		path = FileTools.norm(path);

		if(FileTools.exists(path) == false) {
			throw new RuntimeException(".jar化されたクラスのパスは取得出来ません。[" + path + "]");
		}
		String classPath = critClassObj.getName();
		int deep = StringTools.getCount(classPath, '.') + 1;

		for(int index = 0; index < deep; index++) {
			path = FileTools.eraseLocal(path);
		}
		return path;
	}

	public static List<Class<?>> getClasses(Class<?> critClassObj, Package p) {
		return FileTools.ls(ReflecTools.getDir(critClassObj, p))
				.stream()
				.filter(file -> FileTools.getExt(file).equalsIgnoreCase("class"))
				.filter(file -> file.indexOf("$") == -1)
				.map(file -> RunnableEx.call(() -> getClass(critClassObj, file)))
				.collect(Collectors.<Class<?>>toList());
	}

	public static List<Package> getPackages(Class<?> critClassObj, Package p) {
		return FileTools.ls(ReflecTools.getDir(critClassObj, p))
				.stream()
				.filter(dir -> FileTools.isDirectory(dir))
				.map(dir -> RunnableEx.call(() -> getPackage(critClassObj, dir)))
				.collect(Collectors.<Package>toList());
	}
}
