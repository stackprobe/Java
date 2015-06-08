package charlotte_test.nazofunc;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.ArrayTools;
import charlotte.tools.Base64;
import charlotte.tools.FileTools;
import charlotte.tools.HTTPRequest;
import charlotte.tools.StringTools;

public class PermissionData {
	public PermissionData(byte[] rawData) throws Exception {
		this(rawData, 0, rawData.length);
	}

	public PermissionData(byte[] rawData, int startPos, int size) throws Exception {
		deserialize(rawData, startPos, size);
	}

	private byte[] _firstBlock;
	private UserPart[] _userParts;
	private byte[] _tailBlock;

	private static class UserPart {
		public byte[] nameBlock;
		public byte[][] actionBlocks;
	}

	private byte[] _rData;
	private int _rPos;
	private int _rEnd;

	private int nextByte() {
		return _rData[_rPos++] & 0xff;
	}

	private int nextShort() {
		int h = nextByte();
		int l = nextByte();

		return (h << 8) | l;
	}

	private byte[] nextBlock() {
		byte[] block = new byte[nextShort()];

		for(int index = 0; index < block.length; index++) {
			block[index] = (byte)nextByte();
		}
		return block;
	}

	private void checkByte(int value) throws Exception {
		if(nextByte() != value) {
			throw new Exception("想定されたバイト値と異なります。");
		}
	}

	private void checkShort(int value) throws Exception {
		if(nextShort() != value) {
			throw new Exception("想定されたワード値と異なります。");
		}
	}

	public void deserialize(byte[] rawData, int startPos, int size) throws Exception {
		_rData = rawData;
		_rPos = startPos;
		_rEnd = startPos + size;

		checkByte(0x41);
		checkByte(0x02);
		checkByte(0x00);

		checkShort(1);
		_firstBlock = nextBlock();
		checkShort(0);
		_userParts = new UserPart[nextShort()];

		for(int userPartPos = 0; userPartPos < _userParts.length; userPartPos++) {
			UserPart userPart = new UserPart();

			userPart.nameBlock = nextBlock();
			checkShort(0);
			userPart.actionBlocks = new byte[nextShort()][];

			for(int actionPos = 0; actionPos < userPart.actionBlocks.length; actionPos++) {
				userPart.actionBlocks[actionPos] = nextBlock();
			}
			_userParts[userPartPos] = userPart;
		}
		checkShort(0);
		_tailBlock = nextBlock();

		if(_rPos != _rEnd) {
			throw new Exception("データの終端が想定された位置ではありません。");
		}
		_rData = null;
		_rPos = -1;
		_rEnd = -1;
	}

	private List<byte[]> _wData;

	private void addByte(int value) {
		_wData.add(new byte[] {
				(byte)value,
				});
	}

	private void addShort(int value) {
		_wData.add(new byte[] {
				(byte)((value >>> 8) & 0xff),
				(byte)((value >>> 0) & 0xff),
				});
	}

	private void addBlock(byte[] block) {
		addShort(block.length);
		_wData.add(block);
	}

	public byte[] serialize() {
		_wData = new ArrayList<byte[]>();

		addByte(0x41);
		addByte(0x02);
		addByte(0x00);

		addShort(1);
		addBlock(_firstBlock);
		addShort(0);
		addShort(_userParts.length);

		for(UserPart userPart : _userParts) {
			addBlock(userPart.nameBlock);
			addShort(0);
			addShort(userPart.actionBlocks.length);

			for(byte[] actionBlock : userPart.actionBlocks) {
				addBlock(actionBlock);
			}
		}
		addShort(0);
		addBlock(_tailBlock);

		byte[] ret = join(_wData);
		_wData = null;
		return ret;
	}

	private static byte[] join(List<byte[]> src) {
		int count = 0;

		for(byte[] block : src) {
			count += block.length;
		}
		byte[] dest = new byte[count];
		count = 0;

		for(byte[] block : src) {
			System.arraycopy(block, 0, dest, count, block.length);
			count += block.length;
		}
		return dest;
	}

	public static byte[] getUploadBody(PermissionData dataOld, PermissionData dataNew) {
		List<byte[]> buff = new ArrayList<byte[]>();

		buff.add(new byte[] {
				(byte)0x04,
				});
		buff.add(dataOld.serialize());
		buff.add(dataNew.serialize());

		return join(buff);
	}

	public void addUserPart(UserPart userPart) {
		_userParts = ArrayTools.extend(_userParts, new UserPart[_userParts.length + 1], userPart);
	}

	public void addUserPart(byte[] nameBlock, byte[][] actionBlocks) {
		UserPart userPart = new UserPart();

		userPart.nameBlock = nameBlock;
		userPart.actionBlocks = actionBlocks;

		addUserPart(userPart);
	}

	private static final String BLOCK_CHARSET = "US-ASCII";

	public void addUserPart(String name, String[] actions) throws Exception {
		byte[][] actionBlocks = new byte[actions.length][];

		for(int index = 0; index < actions.length; index++) {
			actionBlocks[index] = actions[index].getBytes(BLOCK_CHARSET);
		}
		addUserPart(
				name.getBytes(BLOCK_CHARSET),
				actionBlocks
				);
	}

	public void removeUserPart(int index) {
		_userParts[index] = _userParts[_userParts.length - 1];
		_userParts = ArrayTools.extend(_userParts, new UserPart[_userParts.length - 1], null);
	}

	public void removeUserPart(String name) throws Exception {
		for(int index = 0; index < _userParts.length; index++) {
			String tmp = new String(_userParts[index].nameBlock, BLOCK_CHARSET);

			if(tmp.equals(name)) {
				removeUserPart(index);
				break;
			}
		}
	}

	/**
	 * new PermissionData(download(url));
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static byte[] download(String url) throws Exception {
		return new HTTPRequest(url).perform().getBody();
	}

	/**
	 * upload(getUploadBody(dataOld, dataNew));
	 * @param url
	 * @param uploadBody
	 * @throws Exception
	 */
	public static void upload(String url, byte[] uploadBody) throws Exception {
		HTTPRequest req = new HTTPRequest(url);

		// test
		//req.setHeaderField("Accept", "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
		//req.setHeaderField("Authorization", "Basic xxx");
		//req.setHeaderField("Connection", "close");
		//req.setHeaderField("Content-type", "application/x-www-form-urlencoded");
		//req.setHeaderField("User-Agent", "Java/1.7.0_40");

		{
			String userId = "xxx";
			String userPassword = "xxx";

			String plain = userId + ":" + userPassword;
			String enc = Base64.getString(plain.getBytes(StringTools.CHARSET_ASCII));

			req.setHeaderField("Authorization", "Basic " + enc);
		}

		req.setBody(uploadBody);
		byte[] body = req.perform().getBody();

		System.out.println("body.length: " + body.length);
		System.out.println("body[0]: " + body[0]);
	}

	public static PermissionData[] download(String url, int count) throws Exception {
		byte[] body = download(url);
		PermissionData[] ret = new PermissionData[count];

		for(int index = 0; index < count; index++) {
			ret[index] = new PermissionData(body);
		}
		return ret;
	}

	public static void upload(String url, PermissionData dataOld, PermissionData dataNew) throws Exception {
		upload(url, getUploadBody(dataOld, dataNew));
	}

	public static final String ACTION_CONTROL = "control";
	public static final String ACTION_OBTAIN = "obtain";
	public static final String ACTION_PRINT = "print";
	public static final String ACTION_READ = "read";
	public static final String ACTION_WRITE = "write";

	public static class UserInfo {
		public String name;
		public List<String> actions;
	}

	public List<UserInfo> getUserInfos() throws Exception {
		List<UserInfo> userInfos = new ArrayList<UserInfo>();

		for(UserPart userPart : _userParts) {
			UserInfo userInfo = new UserInfo();

			userInfo.name = new String(userPart.nameBlock, BLOCK_CHARSET);
			userInfo.actions = new ArrayList<String>();

			for(byte[] actionBlock : userPart.actionBlocks) {
				userInfo.actions.add(new String(actionBlock, BLOCK_CHARSET));
			}
			userInfos.add(userInfo);
		}
		return userInfos;
	}

	public void setUserInfos(List<UserInfo> userInfos) throws Exception {
		_userParts = new UserPart[userInfos.size()];

		for(int index = 0; index < userInfos.size(); index++) {
			UserPart userPart = new UserPart();

			userPart.nameBlock = userInfos.get(index).name.getBytes(BLOCK_CHARSET);
			userPart.actionBlocks = new byte[userInfos.get(index).actions.size()][];

			for(int ndx = 0; ndx < userInfos.get(index).actions.size(); ndx++) {
				userPart.actionBlocks[ndx] = userInfos.get(index).actions.get(ndx).getBytes(BLOCK_CHARSET);
			}
			_userParts[index] = userPart;
		}
	}

	public static void remove(List<UserInfo> userInfos, String name) {
		for(int index = 0; index < userInfos.size(); index++) {
			UserInfo userInfo = userInfos.get(index);

			if(userInfo.name.equals(name)) {
				userInfos.remove(index);
				break;
			}
		}
	}

	public static final int TYPE_PUBLIC = 0x30;
	public static final int TYPE_GROUP = 0x31;
	public static final int TYPE_USER = 0x32;
	public static final int TYPE_PRIVATE = 0x33;

	public void setType(int type) {
		int typePos = _tailBlock.length - 30;
		_tailBlock[typePos] = (byte)type;
	}

	public static void main(String[] args) {
		try {
			//test01();
			test02();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01() throws Exception {
		String url = "http://localhost:28080/xxx";

		PermissionData[] dtdt = download(url, 2);
		PermissionData dataOld = dtdt[0];
		PermissionData dataNew = dtdt[1];

		dataNew.addUserPart(
				"U_xxxx",
				new String[] {
						ACTION_READ,
						ACTION_WRITE,
				});

		dataNew.setType(TYPE_USER);

		//print(dataOld); // test
		//print(dataNew); // test

		upload(url, dataOld, dataNew);
	}

	private static void test02() throws Exception {
		byte[] rawData = FileTools.readAllBytes("C:/tmp/PermissionData.bin");
		PermissionData pd = new PermissionData(rawData, 1, 369);
		print( pd);

		pd = new PermissionData(rawData, 370, 395);
		print( pd);
	}

	private static void print(PermissionData pd) throws Exception {
		print("first", pd._firstBlock);

		for(UserPart userPart : pd._userParts) {
			print("name", userPart.nameBlock);

			for(byte[] actionBlock : userPart.actionBlocks) {
				print("action", actionBlock);
			}
		}
		//print("tail", pd._tailBlock);
		System.out.println("tail_length=" + pd._tailBlock.length);
	}

	private static void print(String header, byte[] block) throws Exception {
		System.out.println(header + "=[" + new String(block, BLOCK_CHARSET) + "]");
	}
}
