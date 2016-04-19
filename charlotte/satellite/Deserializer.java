package charlotte.satellite;

import charlotte.tools.ObjectList;
import charlotte.tools.ObjectMap;
import charlotte.tools.StringTools;

public class Deserializer {
	private byte[] _data;
	private int _index;

	public Deserializer(byte[] data) {
		_data = data;
	}

	private byte readByte() {
		return _data[_index++];
	}

	/**
	 * big endian
	 * @return
	 */
	private int readInt() {
		byte b1 = readByte();
		byte b2 = readByte();
		byte b3 = readByte();
		byte b4 = readByte();

		return
			((b1 & 0xff) << 24) |
			((b2 & 0xff) << 16) |
			((b3 & 0xff) <<  8) |
			((b4 & 0xff) <<  0);
	}

	private byte[] readBlock(int size) {
		byte[] dest = new byte[size];
		System.arraycopy(_data, _index, dest, 0, size);
		_index += size;
		return dest;
	}

	private byte[] readBlock() {
		return readBlock(readInt());
	}

	public Object next() throws Exception {
		byte kind = readByte();

		if(kind == Serializer.KIND_NULL) {
			return null;
		}
		if(kind == Serializer.KIND_BYTES) {
			return readBlock();
		}
		if(kind == Serializer.KIND_MAP) {
			ObjectMap om = ObjectMap.create();
			int size = readInt();

			for(int index = 0; index < size; index++) {
				Object key = next();
				Object value = next();

				om.add(key, value);
			}
			return om;
		}
		if(kind == Serializer.KIND_LIST) {
			ObjectList ol = new ObjectList();
			int size = readInt();

			for(int index = 0; index < size; index++) {
				ol.add(next());
			}
			return ol;
		}
		if(kind == Serializer.KIND_STRING) {
			return new String(readBlock(), StringTools.CHARSET_UTF8);
		}
		throw new Exception("kind: " + kind);
	}
}
