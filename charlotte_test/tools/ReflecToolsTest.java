package charlotte_test.tools;

import java.util.ArrayList;

import charlotte.tools.ReflecTools;
import charlotte.tools.TimeData;

public class ReflecToolsTest {
	public static void main(String[] args) {
		try {
			//test00();
			//test01();
			//test02();
			test03();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test00() {
		test00_b(int.class);
		test00_b(Integer.class);
		test00_b(new int[0].getClass());
		test00_b(new Integer[0].getClass());
		test00_b(new int[0][].getClass());
		test00_b(new Integer[0][].getClass());
		test00_b(new int[0][][].getClass());
		test00_b(new Integer[0][][].getClass());

		test00_b(boolean.class);
		test00_b(Boolean.class);
		test00_b(new boolean[0].getClass());
		test00_b(byte.class);
		test00_b(Byte.class);
		test00_b(new byte[0].getClass());
		test00_b(char.class);
		test00_b(Character.class);
		test00_b(new char[0].getClass());
		test00_b(short.class);
		test00_b(Short.class);
		test00_b(new short[0].getClass());
		test00_b(long.class);
		test00_b(Long.class);
		test00_b(new long[0].getClass());
		test00_b(float.class);
		test00_b(Float.class);
		test00_b(new float[0].getClass());
		test00_b(double.class);
		test00_b(Double.class);
		test00_b(new double[0].getClass());

		test00_b(new ArrayList().getClass());
		test00_b(new ArrayList<Integer>().getClass());
		test00_b(new ArrayList<Long>().getClass());
	}

	private static void test00_b(Class<?> classObj) {
		System.out.println(classObj.getName());
	}

	private static void test01() throws Exception {
		TimeData td = TimeData.now();

		System.out.println("td: " + td);

		long t = (Long)ReflecTools.getObject(
				ReflecTools.getDeclaredField(Class.forName("charlotte.tools.TimeData"), "_t"),
				td
				);

		System.out.println("t: " + t);
		t++;
		System.out.println("t: " + t);

		ReflecTools.setObject(
				ReflecTools.getDeclaredField(Class.forName("charlotte.tools.TimeData"), "_t"),
				td,
				new Long(t)
				);

		System.out.println("td: " + td);
	}

	// ----

	private static void test02() throws Exception {
		ReflecTools.invokeDeclaredCtor(
				Test02Class.class,
				new Object[] { 123 }
				);
		ReflecTools.invokeDeclaredCtor(
				Test02Class_b.class,
				new Object[] { "love you!" }
				);
		ReflecTools.invokeDeclaredCtor(
				Test02Class_02.class,
				new Object[] { new boolean[] { true } }
				);
		ReflecTools.invokeDeclaredCtor(
				Test02Class_03.class,
				new Object[] { new String[] { "ichigo-tan love you!" } }
				);
		ReflecTools.invokeDeclaredCtor(
				Test02Class_04.class,
				new Object[] { new double[][] { new double[] { 123.456 } } }
				);
	}

	private static class Test02Class {
		public Test02Class(int a) {
			System.out.println("Test02Class(int a) executed! a = " + a);
		}
	}

	private static class Test02Class_b {
		public Test02Class_b(String s) {
			System.out.println("Test02Class_b(String s) executed! s = " + s);
		}
	}

	private static class Test02Class_02 {
		public Test02Class_02(boolean[] b) {
			System.out.println("Test02Class_02(boolean[] b) executed! b[0] = " + b[0]);
		}
	}

	private static class Test02Class_03 {
		public Test02Class_03(String[] s) {
			System.out.println("Test02Class_03(String[] s) executed! s[0] = " + s[0]);
		}
	}

	private static class Test02Class_04 {
		public Test02Class_04(double[][] d) {
			System.out.println("Test02Class_04(double[][] d) executed! d[0][0] = " + d[0][0]);
		}
	}

	// ----

	private static void test03() throws Exception {
		System.out.println(ReflecTools.getBinDir(ReflecToolsTest.class));
		System.out.println(ReflecTools.getDir(ReflecToolsTest.class, ReflecToolsTest.class.getPackage()));
		System.out.println(ReflecTools.getFile(ReflecToolsTest.class, ReflecToolsTest.class));
	}
}
