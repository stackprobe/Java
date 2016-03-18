package codeiq;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import charlotte.tools.ArrayTools;
import charlotte.tools.FileTools;
import charlotte.tools.StringTools;

public class J1565 {
	public static void main(String[] args) {
		try {
			test01(read(FileTools.readToEnd(J1565.class.getResource("res1565/sample.in.txt"))));
			test01(read(FileTools.readToEnd(J1565.class.getResource("res1565/small.in.txt"))));
			test01(read(FileTools.readToEnd(J1565.class.getResource("res1565/large.in.txt"))));
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private static int[][] read(byte[] fileData) throws Exception {
		String textData = new String(fileData, StringTools.CHARSET_ASCII);
		textData = textData.replace("\r", "");
		textData = StringTools.removeEndsWith(textData, "\n");
		List<String> lines = StringTools.tokenize(textData, "\n");

		int[][] ret = new int[lines.size()][];

		for(int index = 0; index < lines.size(); index++) {
			String line = lines.get(index);
			List<String> tokens = StringTools.tokenize(line, " ");

			ret[index] = new int[] {
					Integer.parseInt(tokens.get(0)),
					Integer.parseInt(tokens.get(1)),
					};
		}
		return ret;
	}

	private static void test01(int[][] prm) {
		print(prm, "prm");
		int[][] ret = perform(prm);
		print(ret, "ret");
	}

	private static void print(int[][] prm, String title) {
		System.out.println(title + " {");

		for(int[] row : prm) {
			System.out.println(row[0] + ", " + row[1]);
		}
		System.out.println("}");
	}

	private static int[][] perform(int[][] src) {
		_groups = new ArrayList<Group>();

		for(int[] row : src) {
			addToGroups(row[0], row[1]);
		}
		sortGroups();

		for(int index = 1; index < _groups.size(); index++) {
			_groups.get(index).next = _groups.get(index - 1);
		}
		for(int index = 0; index < _groups.size(); index++) {
			Group group = _groups.get(index);

			for(Member member : group.members) {
				member.carriable = getCarriable(member);
			}
		}
		int[][] longest = new int[0][];

		for(Group group : _groups) {
			for(Member member : group.members) {
				if(longest.length < member.carriable.count) {
					longest = getResult(member);
				}
			}
		}
		return longest;
	}

	private static void addToGroups(int tall, int weight) {
		int index;

		for(index = 0; index < _groups.size(); index++) {
			if(_groups.get(index).tall == tall) {
				break;
			}
		}
		if(index == _groups.size()) {
			_groups.add(new Group(tall));
		}
		_groups.get(index).add(weight);
	}

	private static void sortGroups() {
		ArrayTools.sort(_groups, new Comparator<Group>(){
			@Override
			public int compare(Group g1, Group g2) {
				return g1.tall - g2.tall;
			}
		});

		for(Group group : _groups) {
			group.sort();
		}
	}

	private static Carriable getCarriable(Member member) {
		Carriable ret = new Carriable();

		ret.count = 0;
		ret.next = null;

		for(Group next = member.group.next; next != null; next = next.next) {
			Member nextMember = next.getNextMember(member);

			if(nextMember != null) {
				if(ret.count < nextMember.carriable.count) {
					ret.count = nextMember.carriable.count;
					ret.next = nextMember;
				}
			}
		}
		ret.count++; // 自分を足す。
		return ret;
	}

	private static int[][] getResult(Member member) {
		int[][] ret = new int[member.carriable.count][];

		for(int index = 0; index < ret.length; index++) {
			ret[index] = new int[] {
					member.group.tall,
					member.weight,
			};
			member = member.carriable.next;
		}
		ArrayTools.reverse(ret);
		return ret;
	}

	private static List<Group> _groups;

	private static class Group {
		public int tall;
		public List<Member> members = new ArrayList<Member>();

		public Group(int t) {
			tall = t;
		}

		public void add(int weight) {
			members.add(new Member(this, weight));
		}

		public void sort() {
			ArrayTools.sort(members, new Comparator<Member>(){
				@Override
				public int compare(Member m1, Member m2) {
					return m1.weight - m2.weight;
				}
			});
		}

		public Member getNextMember(Member last) {
			int l = -1;
			int r = members.size();

			while(l + 1 < r) {
				int m = (l + r) / 2;

				if(members.get(m).weight < last.weight) {
					l = m;
				}
				else {
					r = m;
				}
			}
			if(l == -1) {
				return null;
			}
			return members.get(l);
		}

		public Group next;
	}

	private static class Member {
		public Group group;
		public int weight;

		public Member(Group g, int w) {
			group = g;
			weight = w;
		}

		public Carriable carriable;
	}

	private static class Carriable {
		public int count; // 自分を含む個数(高さ)
		public Member next; // null == 無し
	}
}
