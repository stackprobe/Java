package charlotte_test.satellite;

import charlotte.satellite.Satellizer;
import charlotte.tools.ObjectList;
import charlotte.tools.ObjectMap;

public class SatellizerTest {
	public static void main(String[] args) {
		try {
			//test01();
			test02();
			//test03_client();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() throws Exception {
		final Satellizer server = new Satellizer("AAA", "BBB");
		Satellizer client = new Satellizer("AAA", "CCC");

		Thread th = new Thread(){
			@Override
			public void run() {
				try {
					server.connect(2000);
					System.out.println("s1"); // test

					System.out.println("s1.5"); // test
					Object obj = server.recv(2000);
					System.out.println("s2"); // test
					server.send("[" + obj + "]");
					System.out.println("s3"); // test
					server.disconnect();
					System.out.println("s4"); // test
				}
				catch(Throwable e) {
					e.printStackTrace();
				}
			}
		};
		th.start();

		System.out.println("c1"); // test
		client.connect(2000);
		System.out.println("c2"); // test
		client.send("aabbcc");
		System.out.println("c3"); // test
		Object obj = client.recv(2000);

		System.out.println("recvObj: " + obj);

		th.join();

		System.out.println("e1"); // test
		server.close();
		System.out.println("e2"); // test
		client.close();
		System.out.println("e3"); // test
	}

	private static void test02() throws Exception {
		final boolean[] deadFlagBox = new boolean[]{ false };

		Thread th = new Thread(){
			@Override
			public void run() {
				System.out.println("s1");

				try {
					Thread.sleep(100);

					Satellizer.listen("AAA", "BBB", 2000, new Satellizer.Server() {
						@Override
						public boolean interlude() throws Exception {
							return deadFlagBox[0] == false;
						}

						@Override
						public void serviceTh(Satellizer stllzr) throws Exception {
							Object obj = stllzr.recv(2000);

							if(obj != null) {
								stllzr.send("[" + obj + "]");
							}
						}
					});
				}
				catch (Exception e) {
					e.printStackTrace();
				}

				System.out.println("s2");
			}
		};
		th.start();

		for(int c = 0; c < 20; c++) {
			Satellizer client = new Satellizer("AAA", "CCC");

			client.connect(2000);
			client.send("TEST_" + c);
			Object obj = client.recv(2000);
			System.out.println("recvObj: " + obj);

			client.close();
		}

		System.out.println("e1");
		deadFlagBox[0] = true;
		th.join();
		System.out.println("e2");
	}

	private static void test03_client() throws Exception {
		{
			Satellizer client = new Satellizer("Test03", "ClientSide");

			{
				client.connect(2000);

				ObjectMap om = new ObjectMap();

				om.add("COMMAND", "ECHO");
				om.add("MESSAGE", "123_abc-DEF/あいう");

				client.send(om);

				Object recvObj = client.recv(2000);
				ObjectList ol = (ObjectList)recvObj;

				System.out.println("ol_0: " + ol.get(0));
				System.out.println("ol_1: " + ol.get(1));
				System.out.println("ol_2: " + ol.get(2));

				client.disconnect();
			}

			{
				client.connect(2000);
				ObjectMap om = new ObjectMap();
				om.add("COMMAND", "CLOSE");
				client.send(om);
			}

			client.close();
		}

		{
			Satellizer client = new Satellizer("Test03", "ClientSide");

			{
				client.connect(2000);
				ObjectMap om = new ObjectMap();
				om.add("COMMAND", "DEAD");
				client.send(om);

				client.disconnect();
			}

			{
				client.connect(2000);
				ObjectMap om = new ObjectMap();
				om.add("COMMAND", "CLOSE");
				client.send(om);
			}

			client.close();
		}
	}
}
