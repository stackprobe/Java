package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class BlockBuffer {
	private List<SubBlock> _buff = new ArrayList<SubBlock>();

	public BlockBuffer() {
	}

	public void add(byte[] block) {
		add(block, 0);
	}

	public void add(byte[] block, int startPos) {
		add(block, startPos, block.length - startPos);
	}

	public void add(byte[] block, int startPos, int size) {
		SubBlock sb = new SubBlock();

		sb.block = block;
		sb.startPos = startPos;
		sb.size = size;

		_buff.add(sb);
	}

	public byte[] getBytes() {
		int count = 0;

		for(SubBlock sb : _buff) {
			count += sb.size;
		}
		byte[] all = new byte[count];
		count = 0;

		for(SubBlock sb : _buff) {
			System.arraycopy(sb.block, sb.startPos, all, count, sb.size);
			count += sb.size;
		}
		return all;
	}

	private class SubBlock {
		public byte[] block;
		public int startPos;
		public int size;
	}
}
