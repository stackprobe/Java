package cecilia;

import cecilia.htt.HttAlcott;
import charlotte.flowertact.Fortewave;
import charlotte.htt.HttServer;

public class Start {
	public static final String ID_HTT_ALCOTT = "{dcd68f30-d69d-40e4-a5a9-52524abb7359}";

	public static void main(String[] args) {
		try {
			HttAlcott ha = new HttAlcott();
			Thread th = new Thread() {
				@Override
				public void run() {
					try {
						HttServer.perform(ha);
					}
					catch(Throwable e) {
						e.printStackTrace();
					}
				}
			};
			th.start();

			Fortewave f = new Fortewave(ID_HTT_ALCOTT);
			while(f.recv(2000) == null);
			f.close();

			ha.end();
			th.join();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
