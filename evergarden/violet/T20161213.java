package evergarden.violet;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.FileTools;
import charlotte.tools.Merging;
import charlotte.tools.StringTools;

public class T20161213 {
	public static void main(String[] args) {
		try {
			main2();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void main2() throws Exception {
		conv(
				"C:/var/20161212_node0_node1/76_2_056.txt",
				"C:/temp/node0_node1_001.txt"
				);
		conv(
				"C:/var/20161212_node0_node1/DrmPlugin_node0_node1.txt",
				"C:/temp/node0_node1_002.txt"
				);
	}

	private static void conv(String rFile, String wFile) throws Exception {
		List<String> dest = new ArrayList<String>();

		for(String line : FileTools.readAllLines(rFile, StringTools.CHARSET_ASCII)) {
			if("".equals(line) == false) {
				String[] tokens = line.split("[\t]");

				if(tokens.length != 2) {
					throw null;
				}
				tokens[0] = trimToken(tokens[0]);
				tokens[1] = trimToken(tokens[1]);

				int i = Integer.parseInt(tokens[0]);
				int j = Integer.parseInt(tokens[1]);

				if(j < i) {
					int tmp = i;
					i = j;
					j = tmp;
				}
				dest.add(i + " " + j);
			}
		}
		Merging.distinct(dest, StringTools.comp);

		FileTools.writeAllLines(wFile, dest, StringTools.CHARSET_ASCII);
	}

	private static String trimToken(String token) {
		token = token.replace(",", "");
		token = token.trim();
		return token;
	}
}
