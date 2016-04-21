package charlotte.tools;

public class Jammer {
	public static byte[] encrypt(byte[] src) throws Exception {
		return Encryptor.encrypt(src, getConcretePassphrase());
	}

	public static byte[] decrypt(byte[] src) throws Exception {
		return Encryptor.decrypt(src, getConcretePassphrase());
	}

	public static String getConcretePassphrase() {
		return "J";
	}
}
