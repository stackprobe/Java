package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

import jp.co.ntt.isl.camellia.Camellia;

public class Encryptor {
	private final int KEY_BUNDLE_SIZE_MIN = 2;
	private List<byte[]> _keyBundle = new ArrayList<byte[]>();

	public Encryptor() {
	}

	public Encryptor(byte[] rawKey) {
		addRawKey(rawKey);
	}

	public void addRawKey(byte[] rawKey) {
		if(rawKey == null) {
			throw null;
		}
		if(rawKey.length % 32 == 0) {
			addRawKey(rawKey, 32);
		}
		else if(rawKey.length % 24 == 0) {
			addRawKey(rawKey, 24);
		}
		else if(rawKey.length % 16 == 0) {
			addRawKey(rawKey, 16);
		}
		else {
			throw new IllegalArgumentException();
		}
	}

	public void addRawKey(byte[] rawKey, int keyWidth) {
		if(rawKey == null) {
			throw null;
		}
		switch(keyWidth) {
		case 16:
		case 24:
		case 32:
			break;

		default:
			throw new IllegalArgumentException();
		}
		if(rawKey.length % keyWidth != 0) {
			throw new IllegalArgumentException();
		}
		for(int index = 0; index < rawKey.length; index += keyWidth) {
			_keyBundle.add(ArrayTools.getBytes(rawKey, index, index + keyWidth));
		}
	}

	public static byte[] encrypt(byte[] src, byte[] rawKey) {
		return new Encryptor(rawKey).encrypt(src);
	}

	public static byte[] decrypt(byte[] src, byte[] rawKey) {
		return new Encryptor(rawKey).decrypt(src);
	}

	public byte[] encrypt(byte[] src) {
		if(src == null) {
			throw null;
		}
		byte[] padding = getPadding(src);
		byte[] randPart = getRandPart();
		byte[] hash = getHash(src, padding, randPart);

		BlockBuffer buff = new BlockBuffer();

		buff.add(src);
		buff.add(padding);
		buff.add(randPart);
		buff.add(hash);

		byte[] dest = buff.getBytes();

		encryptRingCBC(dest);

		return dest;
	}

	public byte[] decrypt(byte[] src) {
		if(src == null) {
			throw null;
		}
		byte[] dest = ArrayTools.copy(src);

		decryptRingCBC(dest);

		dest = removeHash(dest);
		dest = removeRandPart(dest);
		dest = removePadding(dest);

		return dest;
	}

	public void encryptRingCBC(byte[] dest) {
		checkRingCBCParams(dest);

		for(byte[] key : _keyBundle) {
			Camellia camellia = new Camellia();
			camellia.init(true, key);

			xorBlock(dest, 0, dest.length - 16);
			camellia.processBlock(dest, 0, dest, 0);

			for(int wPos = 16; wPos < dest.length; wPos += 16) {
				int rPos = wPos - 16;

				xorBlock(dest, wPos, rPos);
				camellia.processBlock(dest, wPos, dest, wPos);
			}
		}
	}

	public void decryptRingCBC(byte[] dest) {
		checkRingCBCParams(dest);

		for(int index = _keyBundle.size() - 1; 0 <= index; index--) {
			byte[] key = _keyBundle.get(index);
			Camellia camellia = new Camellia();
			camellia.init(false, key);

			for(int wPos = dest.length - 16; 16 <= wPos; wPos -= 16) {
				int rPos = wPos - 16;

				camellia.processBlock(dest, wPos, dest, wPos);
				xorBlock(dest, wPos, rPos);
			}
			camellia.processBlock(dest, 0, dest, 0);
			xorBlock(dest, 0, dest.length - 16);
		}
	}

	private void checkRingCBCParams(byte[] dest) {
		if(dest == null) {
			throw null;
		}
		if(dest.length < 32) {
			throw new IllegalArgumentException();
		}
		if(dest.length % 16 != 0) {
			throw new IllegalArgumentException();
		}
		if(_keyBundle.size() < KEY_BUNDLE_SIZE_MIN) {
			throw new IllegalArgumentException();
		}
	}

	private static void xorBlock(byte[] dest, int rwPos, int rPos) {
		for(int index = 0; index < 16; index++) {
			dest[rwPos + index] ^= dest[rPos + index];
		}
	}

	private static byte[] getPadding(byte[] src) {
		return null; // TODO
	}

	private static byte[] removePadding(byte[] src) {
		return null; // TODO
	}

	private static byte[] getRandPart() {
		return null; // TODO
	}

	private static byte[] removeRandPart(byte[] src) {
		return null; // TODO
	}

	private static byte[] getHash(byte[] src, byte[] padding, byte[] randPart) {
		return null; // TODO
	}

	private static byte[] removeHash(byte[] src) {
		return null; // TODO
	}
}
