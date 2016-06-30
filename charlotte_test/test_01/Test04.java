package charlotte_test.test_01;

public class Test04 {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() throws Exception {
		Runtime.getRuntime().exec("CMD /C START \"\" \"C:/Program Files/Common Files\"");
	}
}
