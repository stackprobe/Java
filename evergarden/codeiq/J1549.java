package evergarden.codeiq;

public class J1549 {
	public static void main(String[] args) {
		try {
			perform();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static int _ans;

	private static void perform() {
		_ans = 0;

		for(int a = 0; a < 24 * 60; a++) {
			for(int b = 0; b <= a; b++) {
				check(a ,b, a - b);
			}
		}
		System.out.println("ans: " + _ans);
	}

	private static void check(int a, int b, int c) {
		int ah = a / 60;
		int am = a % 60;
		int bh = b / 60;
		int bm = b % 60;
		int ch = c / 60;
		int cm = c % 60;

		int ah1 = ah / 10;
		int ah2 = ah % 10;
		int am1 = am / 10;
		int am2 = am % 10;
		int bh1 = bh / 10;
		int bh2 = bh % 10;
		int bm1 = bm / 10;
		int bm2 = bm % 10;
		int ch1 = ch / 10;
		int ch2 = ch % 10;
		int cm1 = cm / 10;
		int cm2 = cm % 10;

		ah1 = (ah1 + 9) % 10 + 1;
		am1 = (am1 + 9) % 10 + 1;
		bh1 = (bh1 + 9) % 10 + 1;
		bm1 = (bm1 + 9) % 10 + 1;
		ch1 = (ch1 + 9) % 10 + 1;
		cm1 = (cm1 + 9) % 10 + 1;

		int flg = 0;

		flg |= 1 << ah1;
		flg |= 1 << ah2;
		flg |= 1 << am1;
		flg |= 1 << am2;
		flg |= 1 << bh1;
		flg |= 1 << bh2;
		flg |= 1 << bm1;
		flg |= 1 << bm2;
		flg |= 1 << ch1;
		flg |= 1 << ch2;
		flg |= 1 << cm1;
		flg |= 1 << cm2;

		flg &= 1023;

		if(flg == 1023) {
			System.out.println(ah + ":" + am + " - " + bh + ":" + bm + " = " + ch + ":" + cm);
			_ans++;
		}
	}
}
