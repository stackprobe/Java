package codejr;

import charlotte.tools.DataConv;
import charlotte.tools.StringTools;

public class J1597 {
	public static void main(String[] args) {
		try {
			test01(new int[][] {
					new int[]{ 1, 0, 0 },
					new int[]{ 0, 1, 0 },
					new int[]{ 0, 0, 1 },
					new int[]{ 0, 0, 1 },
			});

			// ----

			test01(new int[][] {
					new int[]{ 0 },
			});

			test01(new int[][] {
					new int[]{ 1 },
			});

			test01(new int[][] {
					new int[]{ 1, 0, 1 },
					new int[]{ 0, 1, 0 },
					new int[]{ 1, 0, 1 },
			});
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static void test01(int[][] prm) {
		int[] ret = perform(prm);
		System.out.println("ret: " + StringTools.join(", ", DataConv.getStringList(ret)));
	}

	private static int[] perform(int[][] prm) {
		int[] pos = new int[prm[0].length + 1];

		for(int c = 0; c < pos.length; c++) {
			pos[c] = c;
		}
		for(int r = 0; r < prm.length; r++) {
			for(int c = 0; c < pos.length - 1; c++) {
				if(prm[r][c] == 1) {
					int tmp = pos[c];
					pos[c] = pos[c + 1];
					pos[c + 1] = tmp;
				}
			}
		}
		int[] ret = new int[pos.length];

		for(int c = 0; c < pos.length; c++) {
			int p = 0;

			while(pos[p] != c) {
				p++;
			}
			ret[c] = p + 1;
		}
		return ret;
	}
}
