package charlotte_test.satellite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import charlotte.satellite.Deserializer;
import charlotte.satellite.Serializer;
import charlotte.tools.ObjectList;
import charlotte.tools.ObjectMap;
import charlotte.tools.StringTools;

public class SerializerTest {
	public static void main(String[] args) {
		try {
			{
				Map<String, Object> m = new HashMap<String, Object>();

				m.put("aaa", "bbb");
				m.put("zzz", "bbbccc");
				m.put("123", "bbbdddeee");
				m.put("123", "bbbdddeee");

				System.out.println("" + m.size());
			}

			// ----

			//test01(new TimeData(0));

			test01(null);
			test01("ABC");
			test01(StringTools.hex("deadbeef"));

			{
				ObjectMap map = new ObjectMap();

				map.add("aaaaa", "123456");
				map.add("bbbbbb", "987654321");
				map.add("ccccccc", "121212121212");

				test01(map);
			}

			{
				ObjectList list = new ObjectList();

				list.add("abc");
				list.add("defhij");
				list.add("klmnopqrs");

				test01(list);
			}

			for(int depth = 1; depth <= 10; depth++) {
				for(int c = 0; c < 100; c++) {
					test01(getRandData(depth));
				}
			}

			System.out.println("OK");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static Random _r = new Random();

	private static Object getRandData(int depth) {
		if(depth <= 0) {
			return null;
		}
		depth--;

		switch(_r.nextInt(5)) {
		case 0:
		{
			return null;
		}
		case 1:
		{
			int size = _r.nextInt(30);
			byte[] block = new byte[size];
			_r.nextBytes(block);
			return block;
		}
		case 2:
		{
			int size = _r.nextInt(10);
			ObjectMap map = new ObjectMap();

			for(int c = 0; c < size; c++) {
				map.add(getRandString(), getRandData(depth));
			}
			return map;
		}
		case 3:
		{
			int size = _r.nextInt(10);
			ObjectList list = new ObjectList();

			for(int c = 0; c < size; c++) {
				list.add(getRandData(depth));
			}
			return list;
		}
		case 4:
		{
			return getRandString();
		}
		}
		return null;
	}

	private static Object getRandString() {
		int size = _r.nextInt(30);
		StringBuffer buff = new StringBuffer();
		final String CHRS = "123abcABCあいうアイウ#$%文化財";

		for(int c = 0; c < size; c++) {
			buff.append(CHRS.charAt(_r.nextInt(CHRS.length())));
		}
		return buff.toString();
	}

	private static void test01(Object src) throws Exception {
		String strSrc = toString(src);
		System.out.println("src: " + strSrc);
		Serializer s = new Serializer(src);
		byte[] data = s.getBytes();
		Deserializer d = new Deserializer(data);
		Object dest = d.next();
		String strDest = toString(dest);
		System.out.println("ret: " + strDest);

		if(strSrc.equals(strDest) == false) {
			throw new Exception("test_error");
		}
	}

	private static String toString(Object obj) {
		if(obj == null) {
			return "N";
		}
		if(obj instanceof byte[]) {
			return "B[" + StringTools.toHex((byte[])obj) + "]";
		}
		if(obj instanceof ObjectMap) {
			return "M[" + mapToString((ObjectMap)obj) + "]";
		}
		if(obj instanceof ObjectList) {
			return "L[" + listToString((ObjectList)obj) + "]";
		}
		return "S[" + obj + "]";
	}

	private static String mapToString(ObjectMap map) {
		StringBuffer buff = new StringBuffer();
		boolean added = false;

		for(String key : getKeys(map.keySet())) {
			Object value = map.get(key);

			if(added) {
				buff.append(",");
			}
			buff.append(toString(key));
			buff.append("=");
			buff.append(toString(value));
			added = true;
		}
		return buff.toString();
	}

	private static String[] getKeys(Set<String> keySet) {
		List<String> keys = new ArrayList<String>();

		for(String key : keySet) {
			keys.add(key);
		}
		String[] keyArr = keys.toArray(new String[0]);

		Arrays.sort(keyArr, new Comparator<String>(){
			@Override
			public int compare(String s1, String s2) {
				return s1.compareTo(s2);
			}
		});

		return keyArr;
	}

	private static String listToString(ObjectList list) {
		StringBuffer buff = new StringBuffer();
		boolean added = false;

		for(Object eObj : list.asList()) {
			if(added) {
				buff.append(",");
			}
			buff.append(toString(eObj));
			added = true;
		}
		return buff.toString();
	}
}
