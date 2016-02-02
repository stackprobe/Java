package charlotte_test.tools;

import charlotte.tools.ArrayTools;
import charlotte.tools.Encryptor;
import charlotte.tools.FileTools;


public class EncryptorTest {
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
		test01_file("C:/tmp/B-aCrH0CUAA5wtn.jpg");
		test01_file("C:/tmp/Test05_Log.txt");
		test01_file("C:/tmp/20141209055801.bmp");
		test01_file("C:/tmp/ifxまとめ/GHClientLoginTunnelLog.txt");
	}

	private static int _test01_file_no = 1;

	private static void test01_file(String file) throws Exception {
		byte[] src = FileTools.readAllBytes(file);
		byte[] enc = Encryptor.encrypt(src, "abc");
		byte[] dec = Encryptor.decrypt(enc, "abc");

		if(ArrayTools.isSame(src, enc)) {
			throw null;
		}
		if(ArrayTools.isSame(src, dec) == false) {
			throw null;
		}
		String ext = FileTools.getExt(file);

		FileTools.writeAllBytes(FileTools.addExt("C:/temp/" + _test01_file_no + "_1", ext), src);
		FileTools.writeAllBytes(FileTools.addExt("C:/temp/" + _test01_file_no + "_2", ext) + ".enc", enc);
		FileTools.writeAllBytes(FileTools.addExt("C:/temp/" + _test01_file_no + "_3", ext), dec);

		_test01_file_no++;
	}
}
