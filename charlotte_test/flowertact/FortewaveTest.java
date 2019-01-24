package charlotte_test.flowertact;

import charlotte.flowertact.Fortewave;

public class FortewaveTest {
	private static Fortewave _client;
	private static Fortewave _server;
	private static Fortewave _loop;

	public static void main(String[] args) {
		try {
			_client = new Fortewave("TEST_CLIENT", "TEST_SERVER");
			_server = new Fortewave("TEST_SERVER", "TEST_CLIENT");
			_server.clear();
			_loop = new Fortewave("LOOP");
			_loop.clear();

			//test01_Client();
			//test01_Server();
			test01();
			//test02();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() throws Exception {
		new Thread() {
			@Override
			public void run() {
				try {
					test01_Server();
				}
				catch(Throwable e) {
					e.printStackTrace();
				}
			}
		}
		.start();

		test01_Client();
	}

	private static void test01_Client() throws Exception {
		for(int c = 0; c < 100; c++) {
			System.out.println("c_send: " + c);

			_client.send("TEST_STRING_" + c);
		}
		for(int c = 0; c < 100; c++) {
			String assumeRet = "TEST_STRING_" + c + "_RET";

			System.out.println("assumeRet: " + assumeRet);

			for(; ; ) {
				String ret = (String)_client.recv(2000);

				System.out.println("c_ret: " + ret);

				if(ret != null) {
					if(ret.equals(assumeRet) == false) {
						throw new Exception("ng");
					}
					break;
				}
			}
		}
	}

	private static void test01_Server() throws Exception {
		for(int c = 0; c < 100; c++) {
			for(; ; ) {
				String ret = (String)_server.recv(2000);

				System.out.println("s_ret: " + ret);

				if(ret != null) {
					_server.send(ret + "_RET");
					break;
				}
			}
		}
	}

	private static void test02() throws Exception {
		_loop.send("TEST_STRING_-3");
		_loop.send("TEST_STRING_-2");
		_loop.send("TEST_STRING_-1");

		for(int c = 0; c < 2100; c++) {
			_loop.send("TEST_STRING_" + c);

			String assume = "TEST_STRING_" + (c - 3);
			String ret = (String)_loop.recv(2000);

			if(assume.equals(ret) == false) {
				throw new Exception("ng");
			}
		}
	}
}
