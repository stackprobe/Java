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

	public static <T> void shuffle(List<T> list) {
		for(int i = 0; i < list.size() - 1; i++) {
			int j = SystemTools.random(i, list.size() - 1);

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
}
