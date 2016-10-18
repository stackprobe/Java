package charlotte.tools;

public class FaultOperation extends Exception {
	public static final FaultOperation i = new FaultOperation();

	public FaultOperation() {
		super();
	}

	public FaultOperation(String message) {
		super(message);
	}
}
