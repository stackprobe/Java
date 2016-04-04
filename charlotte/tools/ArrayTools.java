package charlotte.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ArrayTools {
	public static <T> List<T> copy(List<T> src) {
		return new ArrayList<T>(src);
	}

	public static <T> T[] copy(T[] src, T[] dest) {
		if(src.length != dest.length) {
			throw new IllegalArgumentException("コピー元配列とコピー先配列の長さが違います。" + src.length + ", " + dest.length);
		}
		return extend(src, dest, null);
	}

	public static <T> T[] extend(T[] src, T[] dest, T elementNew) {
		if(src.length < dest.length) {
			System.arraycopy(src, 0, dest, 0, src.length);

			for(int index = src.length; index < dest.length; index++) {
				dest[index] = elementNew;
			}
		}
		else {
			System.arraycopy(src, 0, dest, 0, dest.length);
		}
		// old
		/*
		int end = Math.min(src.length, dest.length);
		int index;

		for(index = 0; index < end; index++) {
			dest[index] = src[index];
		}
		for(; index < dest.length; index++) {
			dest[index] = elementNew;
		}
		*/
		return dest;
	}

	public static <T> void sort(T[] array, Comparator<T> comp) {
		Arrays.sort(array, comp);
	}

	public static <T> void sort(List<T> list, Comparator<T> comp) {
		Collections.sort(list, comp);
	}

	public static <T> void swap(T[] array, int i, int j) {
		T tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
	}

	public static <T> void swap(List<T> list, int i, int j) {
		T tmp = list.get(i);
		list.set(i, list.get(j));
		list.set(j, tmp);
	}

	public static void swap(byte[] block, int i, int j) {
		byte tmp = block[i];
		block[i] = block[j];
		block[j] = tmp;
	}

	public static <T> void fastRemove(List<T> list, int index) {
		swap(list, index, list.size() - 1);
		list.remove(list.size() - 1);
	}

	public static <T> void reverse(T[] array) {
		int l = 0;
		int r = array.length - 1;

		while(l < r) {
			swap(array, l, r);

			l++;
			r--;
		}
	}

	public static <T> void reverse(List<T> list) {
		int l = 0;
		int r = list.size() - 1;

		while(l < r) {
			swap(list, l, r);

			l++;
			r--;
		}
	}

	public static void reverse(byte[] block) {
		int l = 0;
		int r = block.length - 1;

		while(l < r) {
			swap(block, l, r);

			l++;
			r--;
		}
	}

	public static <T> void shuffle(List<T> list) {
		for(int i = 0; i < list.size() - 1; i++) {
			int j = MathTools.random(i, list.size() - 1);

			swap(list, i, j);
		}
	}

	public static <T> int indexOf(T[] list, T target, Comparator<T> comp) {
		for(int index = 0; index < list.length; index++) {
			if(comp.compare(list[index], target) == 0) {
				return index;
			}
		}
		return -1;
	}

	public static <T> int indexOf(List<T> list, T target, Comparator<T> comp) {
		for(int index = 0; index < list.size(); index++) {
			if(comp.compare(list.get(index), target) == 0) {
				return index;
			}
		}
		return -1;
	}

	public static byte[] byteSq(int bgnChr, int endChr) {
		byte[] buff = new byte[(endChr - bgnChr) + 1];

		for(int index = 0; bgnChr + index <= endChr; index++) {
			buff[index] = (byte)(bgnChr + index);
		}
		return buff;
	}

	public static <T> List<T> shallowCopy(List<T> src) {
		List<T> dest = new ArrayList<T>();

		for(T element : src) {
			dest.add(element);
		}
		return dest;
	}

	public static <T> boolean contains(T[] arr, T target, Comparator<T> comp) {
		return indexOf(arr, target, comp) != -1;
	}

	public static <T> boolean contains(List<T> list, T target, Comparator<T> comp) {
		return indexOf(list, target, comp) != -1;
	}

	public static byte[] getBytes(byte[] src, int startPos, int size) {
		byte[] dest = new byte[size];
		System.arraycopy(src, startPos, dest, 0, size);
		return dest;
	}

	public static byte[] copy(byte[] src) {
		return getBytes(src, 0, src.length);
	}

	public static byte[] changeSize(byte[] src, int size, byte pad) {
		byte[] dest = new byte[size];

		if(src.length < size) {
			System.arraycopy(src, 0, dest, 0, src.length);
			Arrays.fill(dest, src.length, size, pad);
		}
		else {
			System.arraycopy(src, 0, dest, 0, size);
		}
		return dest;
	}

	public static boolean isSame(byte[] block1, int startPos1, byte[] block2, int startPos2, int size) {
		for(int index = 0; index < size; index++) {
			if(block1[startPos1 + index] != block2[startPos2 + index]) {
				return false;
			}
		}
		return true;
	}

	public static boolean isSame(byte[] block1, byte[] block2) {
		if(block1 == null && block2 == null) {
			return true;
		}
		if(block1 == null || block2 == null) {
			return false;
		}
		if(block1.length != block2.length) {
			return false;
		}
		return isSame(block1, 0, block2, 0, block1.length);
	}

	public static int[] toArray(List<Integer> src) {
		int[] dest = new int[src.size()];

		for(int index = 0; index < src.size(); index++) {
			dest[index] = src.get(index).intValue();
		}
		return dest;
	}

	public static List<Integer> toList(int[] src) {
		List<Integer> dest = new ArrayList<Integer>();

		for(int index = 0; index < src.length; index++) {
			dest.add(src[index]);
		}
		return dest;
	}

	public static <T> List<T> toList(T[] src) {
		List<T> dest = new ArrayList<T>();

		for(int index = 0; index < src.length; index++) {
			dest.add(src[index]);
		}
		return dest;
	}

	public static <T> void copy(T[] src, int rPos, T[] dest, int wPos, int size) {
		System.arraycopy(src, rPos, dest, wPos, size);
	}

	public static void copy(byte[] src, int rPos, byte[] dest, int wPos, int size) {
		System.arraycopy(src, rPos, dest, wPos, size);
	}
}
