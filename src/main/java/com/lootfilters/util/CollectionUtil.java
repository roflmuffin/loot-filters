package com.lootfilters.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectionUtil {
    private CollectionUtil() {}

    public static <E> List<E> append(List<E> list, E... elements) {
        var newList = new ArrayList<>(list);
        newList.addAll(Arrays.asList(elements));
        return newList;
    }
}
