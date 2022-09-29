package com.github.lzyzsd.jsbridge.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import java.util.Set;

/**
 * 非线程安全
 * Created by linqiye on 10/9/16.
 */
public class BiMultimap<K, V> {
    private final SetMultimap<K, V> mapK2v = HashMultimap.create();
    private final SetMultimap<V, K> mapV2k = HashMultimap.create();

    public BiMultimap() {
    }

    public void put(K key, V value) {
        boolean r1 = mapK2v.put(key, value);
        boolean r2 = mapV2k.put(value, key);
    }

    public void remove(K key, V value) {
        mapK2v.remove(key, value);
        mapV2k.remove(value, key);
    }

    public void removeAll(K key) {
        Set<V> setValue = mapK2v.removeAll(key);
        for (V value : setValue) {
            mapV2k.remove(value, key);
        }
    }

    public void removeAllValue(V value) {
        Set<K> setKey = mapV2k.removeAll(value);
        for (K key : setKey) {
            mapK2v.remove(key, value);
        }
    }

    public Set<V> getValue(K key) {
        return mapK2v.get(key);
    }
}
