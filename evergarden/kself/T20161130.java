package evergarden.kself;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.ArrayTools;
import charlotte.tools.AutoTable;
import charlotte.tools.Cancelled;
import charlotte.tools.CsvData;
import charlotte.tools.IntTools;
import charlotte.tools.Logger;
import charlotte.tools.StringTools;
import charlotte.tools.WNode;
import charlotte.tools.XNode;

public class T20161130 {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static final String FILE_COMMON_XML =
			"C:/Projects/PJ112707P01-100/trunk/src/GHClient/src/ifx/gh/buildingCertification/bukkenSearch/res/edit/Common.xml";
	private static final String FILE_KENCHIKUBUTSU_XML =
			"C:/Projects/PJ112707P01-100/trunk/src/GHClient/src/ifx/gh/buildingCertification/bukkenSearch/res/edit/Kenchikubutsu.xml";
	private static final String FILE_KOUSAKUBUTSU_1_XML =
			"C:/Projects/PJ112707P01-100/trunk/src/GHClient/src/ifx/gh/buildingCertification/bukkenSearch/res/edit/Kousakubutsu1.xml";
	private static final String FILE_KOUSAKUBUTSU_2_XML =
			"C:/Projects/PJ112707P01-100/trunk/src/GHClient/src/ifx/gh/buildingCertification/bukkenSearch/res/edit/Kousakubutsu2.xml";
	private static final String FILE_SHOUKOUKI_XML =
			"C:/Projects/PJ112707P01-100/trunk/src/GHClient/src/ifx/gh/buildingCertification/bukkenSearch/res/edit/Shoukouki.xml";

	private static final String FILE_COMMON_CSV =
			"C:/var/kselfcsv/共通.csv";
	private static final String FILE_KENCHIKUBUTSU_CSV =
			"C:/var/kselfcsv/建築物.csv";
	private static final String FILE_KOUSAKUBUTSU_1_CSV =
			"C:/var/kselfcsv/工作物1.csv";
	private static final String FILE_KOUSAKUBUTSU_2_CSV =
			"C:/var/kselfcsv/工作物2.csv";
	private static final String FILE_SHOUKOUKI_CSV =
			"C:/var/kselfcsv/昇降機.csv";

	private static Logger _logger;

	private static void test01() throws Exception {
		_logger = Logger.create("C:/temp/KSelfCsvProc", "KSelfCsvProc_");

		XNode rootCommon = XNode.load(FILE_COMMON_XML);
		XNode rootKenchikubutsu = XNode.load(FILE_KENCHIKUBUTSU_XML);
		XNode rootKousakubutsu1 = XNode.load(FILE_KOUSAKUBUTSU_1_XML);
		XNode rootKousakubutsu2 = XNode.load(FILE_KOUSAKUBUTSU_2_XML);
		XNode rootShoukouki = XNode.load(FILE_SHOUKOUKI_XML);

		AutoTable<String> csvCommon = readCsv(FILE_COMMON_CSV);
		AutoTable<String> csvKenchikubutsu = readCsv(FILE_KENCHIKUBUTSU_CSV);
		AutoTable<String> csvKousakubutsu1 = readCsv(FILE_KOUSAKUBUTSU_1_CSV);
		AutoTable<String> csvKousakubutsu2 = readCsv(FILE_KOUSAKUBUTSU_2_CSV);
		AutoTable<String> csvShoukouki = readCsv(FILE_SHOUKOUKI_CSV);

		rootCommon = proc(rootCommon, csvCommon, "共通");
		rootKenchikubutsu = proc(rootKenchikubutsu, csvKenchikubutsu, "建築物");
		rootKousakubutsu1 = proc(rootKousakubutsu1, csvKousakubutsu1, "工作物1");
		rootKousakubutsu2 = proc(rootKousakubutsu2, csvKousakubutsu2, "工作物2");
		rootShoukouki = proc(rootShoukouki, csvShoukouki, "昇降機");

		rootCommon.save(FILE_COMMON_XML);
		rootKenchikubutsu.save(FILE_KENCHIKUBUTSU_XML);
		rootKousakubutsu1.save(FILE_KOUSAKUBUTSU_1_XML);
		rootKousakubutsu2.save(FILE_KOUSAKUBUTSU_2_XML);
		rootShoukouki.save(FILE_SHOUKOUKI_XML);

		_logger.close();
	}

	private static List<CsvRow> _csvRows = new ArrayList<CsvRow>();

	private static XNode proc(XNode root, AutoTable<String> csv, String title) {
		_csvRows.clear();

		for(int rowidx = 7; rowidx < csv.getHeight(); rowidx++) {
			try {
				_logger.println(title + " の " + (rowidx + 1) + " 行目...");

				CsvRow cr = new CsvRow();

				cr.label = csv.get(3, rowidx);
				cr.table = csv.get(58, rowidx);
				cr.column = csv.get(63, rowidx);
				cr.nullable = strToNullable(csv.get(69, rowidx));
				cr.ketasuu = strToKetasuu(csv.get(70, rowidx));

				if(StringTools.isEmpty(cr.label)) throw new RuntimeException("Wrong label: " + cr.label);
				if(StringTools.isEmpty(cr.table)) throw new RuntimeException("Wrong table: " + cr.table);
				if(StringTools.isEmpty(cr.column)) throw new RuntimeException("Wrong column: " + cr.column);

				_csvRows.add(cr);

				_logger.println("[" + cr.label + "], [" + cr.table + "], [" + cr.column + "], " + cr.nullable + ", " + cr.ketasuu);
			}
			catch(Cancelled e) {
				// ignore
			}
		}

		WNode wRoot = WNode.create(root);

		for(WNode wTab : wRoot.children) {
			if(wTab.name.equals("Tab") == false) throw new RuntimeException("Wrong wTab name: " + wTab.name);

			for(WNode wRow : wTab.children) {
				if(wRow.name.equals("title")) {
					continue;
				}
				if(wRow.name.equals("Row") == false) throw new RuntimeException("Wrong wRow name: " + wRow.name);

				if(wRow.get("ditto") != null) {
					continue;
				}
				if(wRow.get("type").value.equals("border")) {
					continue;
				}

				WNode wKetasuu = wRow.get("ketasuu");
				WNode wCheck = wRow.get("check");
				WNode wKubunEdit = wRow.get("kubun-edit");

				String wnTitle = wRow.get("title").value;
				String wnTable = "(テーブル無し)";
				String wnColumn = "(カラム無し)";

				if(wKubunEdit != null) {
					WNode wKETitle = wKubunEdit.get("table");

					if(wKETitle != null) {
						wnTable = wKETitle.value;
					}
					WNode wKEColumn = wKubunEdit.get("column");

					if(wKEColumn != null) {
						wnColumn = wKEColumn.value;
					}
				}
				if(StringTools.isEmpty(wnTitle)) throw new RuntimeException("wnTitle is empty");
				if(StringTools.isEmpty(wnTable)) throw new RuntimeException("wnTable is empty");
				if(StringTools.isEmpty(wnColumn)) throw new RuntimeException("wnColumn is empty");

				CsvRow cr = getCsvRow(wnTitle, wnTable, wnColumn);

				if(cr == null) {
					continue;
				}
				if(cr.ketasuu == 0) {
					if(wKetasuu != null) {
						wRow.remove(wKetasuu.name);
					}
				}
				else {
					if(wKetasuu == null) {
						wKetasuu = new WNode();
						wKetasuu.name = "ketasuu";
						wRow.children.add(wKetasuu);
					}
					wKetasuu.value = "" + cr.ketasuu;
				}
				if(wCheck == null) {
					wCheck = new WNode();
					wCheck.name = "check";
					wCheck.value = "";
					wRow.children.add(wCheck);
				}
				List<String> chkTkns = StringTools.tokenize(wCheck.value, "｜", false, true);

				// ---- mod chkTkns ----

				chkTkns = ArrayTools.collect(chkTkns, (String chkTkn) -> {
					return chkTkn.equals("桁数") == false && chkTkn.contains("必須") == false;
				});

				if(cr.ketasuu != 0) {
					chkTkns.add("桁数");
				}
				if(cr.nullable == false && wRow.get("type").value.equals("hidden") == false) {
					chkTkns.add("必須");
				}

				// ----

				if(1 <= chkTkns.size()) {
					wCheck.value = StringTools.join("｜", chkTkns);
				}
				else {
					wRow.remove("check");
				}
			}
		}

		root = wRoot.getXNode();
		root = rewriteHissuRange(root);
		return root;
	}

	private static CsvRow getCsvRow(String wnTitle, String wnTable, String wnColumn) {
		List<CsvRow> crs;

		crs = ArrayTools.collect(_csvRows, (CsvRow cr) -> { return cr.label.equals(wnTitle); });

		if(crs.size() == 0) return null;
		if(crs.size() == 1) return crs.get(0);

		crs = ArrayTools.collect(_csvRows, (CsvRow cr) -> {
			return cr.label.equals(wnTitle) && cr.table.equals(wnTable) && cr.column.equals(wnColumn);
		});

		if(crs.size() == 0) return null;
		if(crs.size() == 1) return crs.get(0);

		throw new RuntimeException("行を特定出来ません。" + wnTitle + ", " + wnTable + ", " + wnColumn);
	}

	private static boolean strToNullable(String str) {
		if("N".equals(str)) {
			return false;
		}
		else if("Y".equals(str)) {
			return true;
		}
		else if("".equals(str)) {
			throw Cancelled.i;
		}
		throw new RuntimeException("Wrong nullable string: " + str);
	}

	private static int strToKetasuu(String str) {
		int ret = Integer.parseInt(str);

		if(IntTools.isRange(ret, 0, IntTools.IMAX) == false) {
			throw new RuntimeException("Wrong ketasuu string: " + str);
		}
		return ret;
	}

	private static class CsvRow {
		public String label;
		public String table;
		public String column;
		public boolean nullable;
		public int ketasuu;
	}

	private static AutoTable<String> readCsv(String file) throws Exception {
		CsvData csv = new CsvData();
		csv.readFile(file);
		return csv.getTable();
	}

	private static XNode rewriteHissuRange(XNode root) {
		WNode wRoot = WNode.create(root);
		List<WNode> wRows = new ArrayList<WNode>();

		for(WNode wTab : wRoot.children) {
			for(WNode wRow : wTab.children) {
				wRows.add(wRow);
			}
		}

		for(int index = 0; index < wRows.size(); index++) {
			WNode wRow = wRows.get(index);
			WNode wCheck = wRow.get("check");

			if(wCheck != null && wCheck.value.contains("必須")) {
				int[] range = null;

				if(wRow.get("print-no") != null) {
					range = new int[] {
							getHissuRange(wRows, index, -1),
							getHissuRange(wRows, index, 1),
					};
				}

				List<String> chkTkns = StringTools.tokenize(wCheck.value, "｜");

				chkTkns = ArrayTools.collect(chkTkns, (String chkTkn) -> { return chkTkn.contains("必須") == false; });

				if(range == null) {
					chkTkns.add("必須");
				}
				else {
					chkTkns.add(
							wRows.get(range[0]).get("title").value +
							" ～ " +
							wRows.get(range[1]).get("title").value +
							" 入力有りの場合のみ必須"
							);
				}

				wCheck.value = StringTools.join("｜", chkTkns);
			}
		}

		root = wRoot.getXNode();
		return root;
	}

	private static int getHissuRange(List<WNode> wRows, int index, int step) {
		while(0 <= index && index < wRows.size()) {
			WNode wRow = wRows.get(index);

			if(isInnerList(wRow) == false) {
				if(wRow.get("print-no") == null) {
					break;
				}
				if(wRow.get("type").value.equals("hidden")) {
					break;
				}
			}
			index += step;
		}
		return index - step;
	}

	private static boolean isInnerList(WNode wRow) {
		return wRow.get("title").value.startsWith("新築以外工事種別"); // XXX
	}
}
