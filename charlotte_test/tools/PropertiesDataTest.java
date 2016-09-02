package charlotte_test.tools;

import charlotte.tools.PropertiesData;

public class PropertiesDataTest {
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
		PropertiesData props = PropertiesData.create();

		props.addFile("C:/var/initialize.properties");

		for(String key : props.keySet()) {
			System.out.println(key + "=" + props.get(key));
		}
	}
}
