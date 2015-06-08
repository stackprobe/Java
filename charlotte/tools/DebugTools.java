package charlotte.tools;

public class DebugTools {
	public static <T> void printList(String title, Iterable<T> list) {
		System.out.println(title + " {");

		for(T obj : list) {
			System.out.println("\t" + obj);
		}
		System.out.println("}");
	}
}
