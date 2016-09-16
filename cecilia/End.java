package cecilia;

import charlotte.flowertact.Fortewave;

public class End {
	public static void main(String[] args) {
		try {
			Fortewave f = new Fortewave(Start.ID_HTT_ALCOTT);
			try {
				f.send("END");
			}
			finally {
				f.close();
			}
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
