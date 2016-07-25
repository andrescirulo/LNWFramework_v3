package net.latin.server.utils.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionFactory {

	public static <T> List<T> createList(T[] elements) {

		List<T> list = new ArrayList<T>();

		if (elements != null) {
			for (int i = 0; i < elements.length; i++) {
				list.add(elements[i]);
			}
		}

		return list;
	}

	public static <K, V> Map<K, V> createMap() {
		return new HashMap<K, V>();
	}

	public static <T> List<T> createList() {
		return new ArrayList<T>();
	}

	public static <T> Set<T> createSet() {
		return new HashSet<T>();
	}

	public static <T> List<T> createListSorted(T[] elements, Comparator<T> comparator) {
		List<T> list = createList(elements);

		Collections.sort(list, comparator);

		return list;
	}
}
