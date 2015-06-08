package charlotte_test.satellite;

import charlotte.satellite.NamedEventObject;

public class NamedEventObjectTest {
	public static void main(String[] args) {
		try {
			final int N = 10;
			NamedEventObject[] neoList = new NamedEventObject[N];

			for(int c = 0; c < N; c++) {
				neoList[c] = new NamedEventObject("abc_" + c);
			}
			Thread.sleep(2000);

			for(int c = 0; c < N; c++) {
				neoList[c].close();
				neoList[c] = null;
			}
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
