package com.eojin.realtimechat.web.utils;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public final class PagingUtils {
    private PagingUtils(){}
    public static int resolveSize(Integer size, int defaultSize, int maxSize){
        int resolved = (size == null || size <= 0) ? defaultSize : size;
        return Math.min(resolved, maxSize);
    }
    public static <T> void reverseInPlace(List<T> list){
        Collections.reverse(list);
    }
    public static <T> Long caculateNextBeforeId(List<T> rows, Function<T, Long> idGetter){
        if(rows == null || rows.isEmpty()){
            return null;
        }
        T oldest = rows.get(0); // ASC 기준 가장 오래된 것
        return idGetter.apply(oldest);
    }
}
