package com.dl.playfun.utils;

import com.blankj.utilcode.util.ObjectUtils;

import java.util.List;

/**
 * @author litchi
 */
public class ListUtils {
    /**
     * 判断list是否为空
     */
    public static boolean isEmpty(List list) {
        if (list == null || list.size() == 0) {
            return true;
        } else {
            try {
                if (list.get(0) == null || ObjectUtils.isEmpty(list.get(0))) {
                    return true;
                }
            } catch (Exception e) {

            }
            return false;
        }
    }
}
