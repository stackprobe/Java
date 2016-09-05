package evergarden.violet.reftest01;

public class Test01 {
	public static void main(String[] args) {
		try {
			test01();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() {
		Class<?> cls = Test01.class;
		Package pkg = cls.getPackage();
		//Reflections refs = new Reflections(pkg.getName()); // ne-yo
		
		
	}
}
