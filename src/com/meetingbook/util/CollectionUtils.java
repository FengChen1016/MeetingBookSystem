package com.meetingbook.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public class CollectionUtils {
	
	// TODO add more collection related utility method
	
	public static <T> List<T> toList(Iterator<T> iter) {
		List<T> ans = new ArrayList<T>();
		while (iter.hasNext()) {
			T next = iter.next();
			if (next == null) {
				continue;
			}
			ans.add(next);
		}
		return ans;
	}
	
	public static <T> boolean any(final Collection<T> collection, final Predicate<T> predicate) {
		for (final T obj : collection)
			if (predicate.fit(obj)) return true;
		return false;
	}

	
	
}
