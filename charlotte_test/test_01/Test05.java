package charlotte_test.test_01;

public class Test05 {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() {
		try {
			//throw new RuntimeException();
			//throw null;
			throw new OutOfMemoryError();
		}
		catch(Exception e) {
			System.out.println("ex");
		}
	}
}
