package charlotte_test.flowertact;

import charlotte.flowertact.BlueFortewave;

public class BlueFortewaveTest {
	private static BlueFortewave _client;
	private static BlueFortewave _server;
	private static BlueFortewave _loop;

	public static void main(String[] args) {
		try {
			_client = new BlueFortewave("TEST_CLIENT", "TEST_SERVER");
			_server = new BlueFortewave("TEST_SERVER", "TEST_CLIENT");
			_server.clear();
			_loop = new BlueFortewave("LOOP");
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
			String ret;

			// 送信が非同期であるため、取れるまでリトライ
			for(int t = 0; ; t++) {
				ret = (String)_loop.recv(2000);

				if(ret != null) {
					break;
				}
				System.out.println("recv_null_" + t);
			}

			if(assume.equals(ret) == false) {
				throw new Exception("ng");
			}
		}
	}
}
