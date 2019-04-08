package dev.sky_lock.mocar.util;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sky_lock
 */

public class ListUtil {

    public static <E> List<E> checkedListObject(Object listObj, Class<E> type) {
        return Optional.of(listObj)
                .filter(List.class::isInstance)
                .map(List.class::cast)
                .map(list -> checkedList(list, type))
                .orElseThrow(ClassCastException::new);
    }

    private static <E> List<E> checkedList(List<?> uncheckedList, Class<E> type) {
        return uncheckedList.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toList());
    }

    public static List<String> singleton(String str) {
        return Collections.singletonList(str);
    }
}
