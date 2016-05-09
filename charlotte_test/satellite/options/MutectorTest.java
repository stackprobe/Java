package charlotte_test.satellite.options;

import charlotte.satellite.options.Mutector;
import charlotte.tools.StringTools;

public class MutectorTest {
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
		final boolean[] death = new boolean[1];

		Mutector.Sender sender = new Mutector.Sender("Cure*Magical");
		Mutector.Recver recver = new Mutector.Recver("Cure*Magical") {
			@Override
			public void recved(byte[] message) {
				try {
					System.out.println("Recved=[" + new String(message, StringTools.CHARSET_ASCII) + "]");
				}
				catch(Throwable e) {
					e.printStackTrace();
				}
			}

			@Override
			public boolean interlude() {
				return death[0] == false;
			}
		};

		Thread th = new Thread() {
			@Override
			public void run() {
				try {
					recver.perform();
				}
				catch(Throwable e) {
					e.printStackTrace();
				}
			}
		};

		th.start();
		try {
			// recver の受信開始前に送信すると例外を投げる！

			Thread.sleep(100); // recver の開始待ち。待ち時間は適当！

			sender.send("#".getBytes(StringTools.CHARSET_ASCII));
			sender.send("123".getBytes(StringTools.CHARSET_ASCII));
			sender.send("ABCDEF".getBytes(StringTools.CHARSET_ASCII));
			sender.send("cure*up-lapapa".getBytes(StringTools.CHARSET_ASCII));

			// この時点で recver の受信は完了している。-- Test01Recver.Interlude が false を返しても良い！
		}
		finally {
			death[0] = true;
			th.join();
		}
	}
}
