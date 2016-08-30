package evergarden.violet;

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
		Runtime.getRuntime().exec("CMD /C START \"\" \"C:/Program Files/Common Files\"").waitFor(); // open dir
		//Runtime.getRuntime().exec("CMD /C START \"\" \"C:/var/mp4-layout.txt\"").waitFor(); // open file
	}
}
