package com.org.source.test;

import java.util.ArrayList;
import java.util.List;

import android.widget.BaseAdapter;

public abstract class CacheAdapter<K, T> extends BaseAdapter {
    private static class KeyValue<K, T> {
        public final K mKey;
        public T mValue;

        public KeyValue(K key, T value) {
            if (null == key) {
                throw new RuntimeException("Invalid param");
            }
            mKey = key;
            mValue = value;
        }

        public boolean isEmpty() {
            return null == mValue;
        }

        public void clear() {
            mValue = null;
        }
    }

    private List<KeyValue<K, T>> mIndexList;
    private int mPreLoadRange = 10;

    public CacheAdapter() {
    }

    public CacheAdapter(List<K> indexList) {
        if (null == indexList) {
            throw new RuntimeException("Invalid param");
        }

        mIndexList = new ArrayList<KeyValue<K, T>>();

        for (K key : indexList) {
            mIndexList.add(new KeyValue<K, T>(key, null));
        }
    }

    public void update(List<K> indexList) {
        if (null != mIndexList) {
            mIndexList.clear();
        }

        for (K key : indexList) {
            mIndexList.add(new KeyValue<K, T>(key, null));
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return null == mIndexList ? 0 : mIndexList.size();
    }

    @Override
    public T getItem(int position) {
        KeyValue<K, T> keyValue = mIndexList.get(position);

        if (null == keyValue.mValue) {
            preLoad(position);
        }

        return keyValue.mValue;
    }

    private void preLoad(int pos) {
        int expectedStartPos = Math.max(pos - mPreLoadRange, 0);
        int expectedEndPos = Math.min(pos + mPreLoadRange,
                mIndexList.size() - 1);
        int size = mIndexList.size();

        for (int i = 0; i < size; i++) {
            KeyValue<K, T> keyValue = mIndexList.get(i);

            if (i < expectedStartPos || i > expectedEndPos) {
                keyValue.clear();
            } else if (keyValue.isEmpty()) {
                keyValue.mValue = getValueInner(keyValue.mKey);
            }
        }
    }

    public void setPreLoadRange(int range) {
        if (range < 0) {
            throw new RuntimeException("the range is invalid");
        }

        mPreLoadRange = range;
    }

    public abstract T getValueInner(K index);
}
