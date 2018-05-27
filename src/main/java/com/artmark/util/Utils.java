package com.artmark.util;

import com.google.common.base.Strings;

/**
 * @author Ushmodin N.
 * @since 13.01.2016 10:33
 */

public class Utils {
	@SafeVarargs
	public static <T> T coalesce(T ... items) {
		if (items != null) {
			for (T item : items) {
				if (item != null) {
					return item;
				}
			}
		}
		return null;
	}

	public static boolean isEmpty(String value) {
		return Strings.isNullOrEmpty(value);
	}
}
