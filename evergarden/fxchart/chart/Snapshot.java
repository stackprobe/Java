package evergarden.fxchart.chart;

import charlotte.tools.FileTools;
import charlotte.tools.WorkDir;

public class Snapshot {
	private static WorkDir _wd;

	static {
		try {
			System.out.println("SS.1");
			Runtime.getRuntime().exec("C:/Factory/SubTools/nrun.exe /s mimiko Fx/snapshot").waitFor();
			System.out.println("SS.2");

			_wd = new WorkDir("{2370bde3-646d-4bb5-aeb6-9bc71cfe2fdc}");

			for(String file : FileTools.ls("S:/Fx/Snapshot")) {
				String lFile = FileTools.getLocal(file);
				String wFile = FileTools.combine(_wd.getDir(), lFile);

				System.out.println("< " + file);
				System.out.println("> " + wFile);

				FileTools.moveFile(file, wFile);
			}
			System.out.println("SS.3");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	public static String getFile(String lFile) {
		return FileTools.combine(_wd.getDir(), lFile);
	}
}
