package charlotte_test.tools;

import charlotte.tools.SecurityTools;

public class SecurityToolsTest {
	public static void main(String[] args) {
		try {
			System.out.println("[]: " + SecurityTools.getSHA512String(""));
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
