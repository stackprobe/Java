package charlotte.tools;

/**
 * Xor-shift 128 bit
 *
 */
public class Xorshift {
	public Xorshift() {
		this(SecurityTools.cRandom(),
				SecurityTools.cRandom(),
				SecurityTools.cRandom(),
				SecurityTools.cRandom() & 1,
				10
				);
	}

	private int _x;
	private int _y;
	private int _z;
	private int _a;

	public Xorshift(int x, int y, int z, int a, int skips) {
		if(x == 0 &&
				y == 0 &&
				z == 0 &&
				a == 0
				) {
			throw new IllegalArgumentException();
		}
		_x = x;
		_y = y;
		_z = z;
		_a = a;

		while(0 < skips) {
			next();
			skips--;
		}
	}

	public int next() {
		int t = _x;

		t ^= _x << 11;
		t ^= t >>> 8;
		t ^= _a;
		t ^= _a >>> 19;
		_x = _y;
		_y = _z;
		_z = _a;
		_a = t;

		return t;
	}

	public int nextInt(int modulo) {
		if(modulo < 1) throw null;
		return (int)((next() & 0xffffffffL) % modulo); // HACK
	}
}
