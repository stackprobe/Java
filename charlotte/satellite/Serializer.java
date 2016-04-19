package charlotte.satellite;

import charlotte.tools.ByteBuffer;
import charlotte.tools.ObjectList;
import charlotte.tools.ObjectMap;
import charlotte.tools.StringTools;

public class Serializer {
	public static final byte KIND_NULL = 0x4e;
	public static final byte KIND_BYTES = 0x42;
	public static final byte KIND_MAP = 0x4d;
	public static final byte KIND_LIST = 0x4c;
	public static final byte KIND_STRING = 0x53;

	private ByteBuffer _buff = new ByteBuffer();

	public Serializer() {
	}

	public Serializer(Object obj) throws Exception {
		add(obj);
	}

	public void add(Object obj) throws Exception {
		if(obj == null) {
			_buff.add(KIND_NULL);
		}
		else if(obj instanceof byte[]) {
			_buff.add(KIND_BYTES);
			addBlock((byte[])obj);
		}
		else if(obj instanceof ObjectMap) {
			_buff.add(KIND_MAP);
			ObjectMap om = (ObjectMap)obj;
			addInt(om.size());

			for(String key : om.keySet()) {
				add(key);
				add(om.get(key));
			}
		}
		else if(obj instanceof ObjectList) {
			_buff.add(KIND_LIST);
			ObjectList ol = (ObjectList)obj;
			addInt(ol.size());

			for(Object value : ol.getList()) {
				add(value);
			}
		}
		else if(obj instanceof String) {
			_buff.add(KIND_STRING);
			addBlock(obj.toString().getBytes(StringTools.CHARSET_UTF8));
		}
		else {
			throw new Exception("invalid object class: " + obj.getClass());
		}
	}

	private void addBlock(byte[] block) {
		addInt(block.length);
		_buff.bindAdd(block);
	}

	/**
	 * big endian
	 * @param value
	 */
	private void addInt(int value) {
		_buff.add((byte)((value >>> 24) & 0xff));
		_buff.add((byte)((value >>> 16) & 0xff));
		_buff.add((byte)((value >>>  8) & 0xff));
		_buff.add((byte)((value >>>  0) & 0xff));
	}

	public byte[] getBytes() {
		return _buff.getBytes();
	}
}
