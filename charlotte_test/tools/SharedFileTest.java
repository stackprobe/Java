package charlotte_test.tools;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.FileTools;
import charlotte.tools.IntTools;
import charlotte.tools.MathTools;
import charlotte.tools.SharedFile;

public class SharedFileTest {
	public static void main(String[] args) {
		try {
			test01();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() throws Exception {
		FileTools.writeAllBytes("C:/temp/SharedFile.txt", new byte[]{ 0x00, 0x00, 0x00, 0x00 });
		//FileTools.createFile("C:/temp/SharedFile.txt");
		//FileTools.createFile("C:/temp/SharedFile.txt.locking"); // output stream なので不要

		FileTools.delete("C:/temp/SharedFile.txt.locking");
		//FileTools.delete("C:/temp/SharedFile.txt.locking");

		List<Thread> ths = new ArrayList<Thread>();

		for(int c = 0; c < 100; c++) {
			Thread th = new Thread() {
				@Override
				public void run() {
					try {
						SharedFile sf = new SharedFile("C:/temp/SharedFile.txt");

						try {
							byte[] data = sf.read();
							int value = IntTools.read(data);

							value++;
							System.out.println("-> " + value);

							Thread.sleep(MathTools.random(10));

							data = IntTools.toBytes(value);
							sf.write(data);
						}
						finally {
							sf.release(); // 2bs
						}
					}
					catch(Throwable e) {
						e.printStackTrace();
					}
				}
			};

			ths.add(th);
		}
		for(Thread th : ths) {
			th.start();
		}
		for(Thread th : ths) {
			th.join();
		}

		{
			byte[] data = FileTools.readAllBytes("C:/temp/SharedFile.txt");
			int value = IntTools.read(data);

			System.out.println("value: " + value);
		}
	}
}
