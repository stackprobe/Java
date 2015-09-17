package charlotte.flowertact;

public class BlueFortewave extends Fortewave {
	public BlueFortewave(String ident) throws Exception {
		super(ident);
	}

	public BlueFortewave(String rIdent, String wIdent) throws Exception {
		super(rIdent, wIdent);
	}

	@Override
	protected void init() throws Exception {
		_rPob = new RapidPOBox(_rIdent);
		_wPob = new RapidPOBox(_wIdent);
	}
}
