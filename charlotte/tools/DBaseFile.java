package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class DBaseFile {
	public DBaseFile(String file) throws Exception {
		this(FileTools.readAllBytes(file));
	}

	public DBaseFile(byte[] fileData) {
		load(fileData);
	}

	public DBaseFile() {
	}

	public byte fileType;
	public byte updateY;
	public byte updateM;
	public byte updateD;
	public int recordCount;
	public int firstRecordPos;
	public int recordSize;
	public byte[] reserved;
	public byte flag;
	public byte codePageMark;
	public byte[] reserved2;
	public List<FieldSubrecord> fieldSubrecords = new ArrayList<FieldSubrecord>();
	//public byte[] backLink;
	public List<RowData> rows = new ArrayList<RowData>();

	public static class FieldSubrecord {
		public byte[] name;
		public byte fieldType;
		public byte[] reserved;
		public int fieldSize;
		public byte decimalPlaceNum;
		public byte flag;
		public int nextValue;
		public int stepValue;
		public byte[] reserved2;
	}

	public static class RowData {
		public byte delFlag;
		public List<CellData> cells = new ArrayList<CellData>();
	}

	public static class CellData {
		public byte[] fieldData;

		// work -->

		public byte[] tmp;
	}

	public void load(byte[] fileData) {
		int c = 0;
		fileType = fileData[c++];
		updateY = fileData[c++];
		updateM = fileData[c++];
		updateD = fileData[c++];
		recordCount = IntTools.toInt(fileData, c);
		c += 4;
		firstRecordPos = IntTools.toInt16(fileData, c);
		c += 2;
		recordSize = IntTools.toInt16(fileData, c);
		c += 2;
		reserved = ArrayTools.getBytes(fileData, c, 16);
		c += 16;
		flag = fileData[c++];
		codePageMark = fileData[c++];
		reserved2 = ArrayTools.getBytes(fileData, c, 2);
		c += 2;

		while(fileData[c] != 0x0d) {
			FieldSubrecord fsr = new FieldSubrecord();

			fsr.name = ArrayTools.getBytes(fileData, c, 11);
			c += 11;
			fsr.fieldType = fileData[c++];
			fsr.reserved = ArrayTools.getBytes(fileData, c, 4);
			c += 4;
			fsr.fieldSize = fileData[c++] & 0xff;
			fsr.decimalPlaceNum = fileData[c++];
			fsr.flag = fileData[c++];
			fsr.nextValue = IntTools.toInt(fileData, c);
			c += 4;
			fsr.stepValue = fileData[c++] & 0xff;
			fsr.reserved2 = ArrayTools.getBytes(fileData, c, 8);
			c += 8;

			fieldSubrecords.add(fsr);
		}
		c++; // terminator
		//backLink = ArrayTools.getBytes(fileData, c, 263);
		//c += 263;

		rows.clear();

		for(int rowidx = 0; rowidx < recordCount; rowidx++) {
			RowData row = new RowData();

			row.delFlag = fileData[c++];

			for(int colidx = 0; colidx < fieldSubrecords.size(); colidx++) {
				CellData cell = new CellData();

				FieldSubrecord fsr = fieldSubrecords.get(colidx);
				cell.fieldData = ArrayTools.getBytes(fileData, c, fsr.fieldSize);
				c += fsr.fieldSize;

				row.cells.add(cell);
			}
			rows.add(row);
		}
	}

	public ByteBuffer getFileData() {
		ByteBuffer buff = new ByteBuffer();

		buff.add(fileType);
		buff.add(updateY);
		buff.add(updateM);
		buff.add(updateD);
		buff.bindAdd(IntTools.toBytes(recordCount));
		buff.bindAdd(IntTools.toBytes(firstRecordPos), 0, 2);
		buff.bindAdd(IntTools.toBytes(recordSize), 0, 2);
		buff.bindAdd(reserved);
		buff.add(flag);
		buff.add(codePageMark);
		buff.bindAdd(reserved2);

		for(FieldSubrecord fsr : fieldSubrecords) {
			buff.bindAdd(fsr.name);
			buff.add(fsr.fieldType);
			buff.bindAdd(fsr.reserved);
			buff.bindAdd(IntTools.toBytes(fsr.fieldSize), 0, 1);
			buff.add(fsr.decimalPlaceNum);
			buff.add(fsr.flag);
			buff.bindAdd(IntTools.toBytes(fsr.nextValue));
			buff.bindAdd(IntTools.toBytes(fsr.stepValue), 0, 1);
			buff.bindAdd(fsr.reserved2);
		}
		buff.add((byte)0x0d); // terminator

		for(RowData row : rows) {
			buff.add(row.delFlag);

			for(CellData cell : row.cells) {
				buff.bindAdd(cell.fieldData);
			}
		}
		buff.add((byte)0x1a); // terminator ???
		return buff;
	}

	public void save(String file) throws Exception {
		FileTools.writeAllBytes(file, getFileData().directGetBuff());
	}

	public void changeCharset(String charsetOld, String charsetNew) throws Exception {
		for(int colidx = 0; colidx < fieldSubrecords.size(); colidx++) {
			FieldSubrecord fsr = fieldSubrecords.get(colidx);

			// ? C
			if(fsr.fieldType == (byte)0x43) {
				int maxSize = -1;

				for(int rowidx = 0; rowidx < rows.size(); rowidx++) {
					RowData row = rows.get(rowidx);
					CellData cell = row.cells.get(colidx);

					String mid = new String(cell.fieldData, charsetOld);
					mid = mid.trim();
					cell.tmp = mid.getBytes(charsetNew);

					maxSize = Math.max(maxSize, cell.tmp.length);
				}
				int diff = maxSize - fsr.fieldSize;
				diff = Math.max(0, diff);

				fsr.fieldSize += diff;
				recordSize += diff;

				for(int rowidx = 0; rowidx < rows.size(); rowidx++) {
					RowData row = rows.get(rowidx);
					CellData cell = row.cells.get(colidx);

					cell.fieldData = ArrayTools.changeSize(cell.tmp, fsr.fieldSize, (byte)0x20);
					cell.tmp = null;
				}
			}
		}
	}
}
