package cecilia;

import javax.swing.UIManager;

import cecilia.htt.HttAlcott;
import charlotte.flowertact.Fortewave;
import charlotte.htt.HttServer;
import charlotte.tools.FaultOperation;
import charlotte.tools.StringTools;
import charlotte.tools.ThreadTools;

/**
 * don't enable 'Kill WinAPITools.exe Zombies' @ 2016.12.21
 *
 */
public class StartHttAlcott2 {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(
					"com.sun.java.swing.plaf.windows.WindowsLookAndFeel"
					);

			new StartHttAlcott2().main2();
		}
		catch(Throwable e) {
			FaultOperation.caught(null, e, "StartHttAlcott2");//e.printStackTrace();
		}
		System.exit(0);
	}

	private void main2() throws Exception {
		if(HttAlcott.lock()) {
			try {
				main3();
			}
			finally {
				HttAlcott.unlock();
			}
		}
	}

	private static final String FORTEWAVE_IDENT = "{319a5123-066b-44fe-b754-5cb16cc9c79d}"; // shared_uuid@g

	private HttAlcott _ha = null; // 生存確認用
	private Thread _th;

	private void main3() throws Exception {
		System.out.println("Fortewave-Starting...");

		Fortewave f = new Fortewave(FORTEWAVE_IDENT);
		try {
			System.out.println("Fortewave-Started");

			for(; ; ) {
				byte[] bCommand = (byte[])f.recv(2000);

				if(bCommand == null) {
					continue;
				}
				String command = new String(bCommand, StringTools.CHARSET_SJIS);

				System.out.println("command: " + command);

				if(command.equals("START")) {
					System.out.println("Starting...");
					start();
					System.out.println("Started");
				}
				else if(command.equals("CLEAR")) {
					System.out.println("Clearing...");
					clear();
					System.out.println("Cleared");
				}
				else if(command.equals("END")) {
					System.out.println("Ending...");
					end();
					System.out.println("Ended");
				}
				else if(command.equals("EXIT")) {
					System.out.println("Exiting...");
					end();
					break;
				}
				else {
					throw new Exception("Unknown command: " + command);
				}
			}
		}
		finally {
			f.close();
		}
		System.out.println("Exited");
	}

	private void start() {
		end();
		_ha = new HttAlcott();
		_th = new Thread() {
			@Override
			public void run() {
				try {
					HttServer.perform(_ha);
				}
				catch(Throwable e) {
					e.printStackTrace();
				}
			}
		};
		_th.start();
	}

	private void clear() {
		if(_ha == null) {
			start();
		}
		_ha.clear();
	}

	private void end() {
		if(_ha == null) {
			return; // not running
		}
		_ha.end();
		_ha = null;

		ThreadTools.join(_th);

		_th = null;
	}
}
