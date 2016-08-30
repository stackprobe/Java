package evergarden.labo;

public class Test02 {
	public static void main(String[] args) {
		new A();
		System.out.println("- - -");
		new B();
		System.out.println("- - -");
		new C();
	}

	public static class A {
		public A() {
			System.out.println("Called A ctor.");
		}
	}

	public static class B extends A {
		public B() {
			System.out.println("Called B ctor.");
		}
	}

	public static class C extends B {
		public C() {
			System.out.println("Called C ctor.");
		}
	}
}
