package charlotte_test.satellite;

import java.util.Random;

import charlotte.satellite.MutexObject;
import charlotte.satellite.WinAPITools;
import charlotte.tools.StringTools;


public class MutexObjectTest {
	public static void main(String[] args) {
		try {
			//test01();
			test02();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void test01() throws Exception {
		Random r = new Random();
		final int THC = 10;
		Thread[] ths = new Thread[THC];
		final String mutexName = StringTools.getUUID();

		for(int c = 0; c < THC; c++) {
			final int fc = c;
			final int bw = r.nextInt(100);
			final int lw = r.nextInt(2000);

			ths[c] = new Thread() {
				@Override
				public void run() {
					try {
						MutexObject mo = new MutexObject(mutexName);
						Thread.sleep(bw);
						System.out.println("W1_" + fc);

						if(mo.waitOne(WinAPITools.INFINITE) == false) {
							throw new Exception();
						}
						System.out.println("L1_" + fc);
						Thread.sleep(lw);
						System.out.println("L2_" + fc);
						mo.release();
						System.out.println("R1_" + fc);
					}
					catch(Throwable e) {
						e.printStackTrace();
					}
				}
			};

			ths[c].start();
		}
		for(int c = 0; c < THC; c++) {
			ths[c].join();
		}
	}

	private static void test02() throws Exception {
		final int THC = 10;
		Thread[] ths = new Thread[THC];

		for(int c = 0; c < THC; c++) {
			ths[c] = new Thread() {
				@Override
				public void run() {
					try {
						for(int c = 0; c < 100; c++) {
							MutexObject mo = new MutexObject(StringTools.getUUID());

							if(mo.waitOne(0) == false) {
								throw null;
							}
							mo.release();
						}
					}
					catch(Throwable e) {
						e.printStackTrace();
					}
				}
			};

			ths[c].start();
		}
		for(int c = 0; c < THC; c++) {
			ths[c].join();
		}
	}
}
