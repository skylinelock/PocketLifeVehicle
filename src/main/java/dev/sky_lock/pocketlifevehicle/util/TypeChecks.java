package dev.sky_lock.pocketlifevehicle.util;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sky_lock
 */

public class TypeChecks {

    public static <E> List<E> checkListTypeDynamically(Object listObj, Class<E> type) {
        return Optional.of(listObj)
                .filter(List.class::isInstance)
                .map(List.class::cast)
                .map(list -> checkTypeDynamically(list, type))
                .orElseThrow(ClassCastException::new);
    }

    private static <E> List<E> checkTypeDynamically(List<?> uncheckedList, Class<E> type) {
        return uncheckedList.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toList());
    }

}
