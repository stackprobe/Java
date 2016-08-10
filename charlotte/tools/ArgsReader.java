package charlotte.tools;

public class ArgsReader {
	private String[] _args;
	private int _index = 0;

	public ArgsReader(String[] args) {
		_args = args;
	}

	public boolean hasArgs(int count) {
		return _index + count <= _args.length;
	}

	public boolean hasArgs() {
		return hasArgs(1);
	}

	public String getArg(int index) {
		return _args[_index + index];
	}

	public String nextArg() {
		return _args[_index++];
	}

	public boolean argIs(String spell) {
		if(hasArgs() && getArg(0).equalsIgnoreCase(spell)) {
			nextArg();
			return true;
		}
		return false;
	}
}
