package cecilia;

import cecilia.htt.HttAlcott;
import charlotte.htt.HttServer;
import charlotte.satellite.NamedEventObject;
import charlotte.satellite.WinAPITools;

public class Start {
	public static final String ID_EV_END = "aafergjerogijreojoerig";

	public static void main(String[] args) {
		try {
			HttAlcott ha = new HttAlcott();
			HttServer.perform(ha);

			NamedEventObject evEnd = new NamedEventObject(ID_EV_END);
			try {
				evEnd.waitOne(WinAPITools.INFINITE); // XXX
			}
			finally {
				evEnd.close();
			}
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
