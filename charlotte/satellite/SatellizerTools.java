package charlotte.satellite;

public class SatellizerTools {
	public static String getTmp() throws Exception {
		return WinAPITools.i().getEnv("TMP", "C:/temp");
	}
}
