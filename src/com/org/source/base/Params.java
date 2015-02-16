package com.org.source.base;

import android.util.SparseArray;

import com.org.source.base.LinkedPool.ILinkedPoolable;
import com.org.source.base.LinkedPool.InstanceCreator;

public class Params implements ILinkedPoolable
{
    private static final int MAX_RECYCLED = 16;
    private static final ThreadLocal<LinkedPool<Params>> sPool = new ThreadLocal<LinkedPool<Params>>();

    private static LinkedPool<Params> getPool()
    {
        if (sPool.get() == null)
        {
            sPool.set(new LinkedPool<Params>(CREATOR, MAX_RECYCLED));
        }
        return sPool.get();
    }

    private static final InstanceCreator<Params> CREATOR = new InstanceCreator<Params>()
    {
        public Params createInstance()
        {
            return new Params();
        }
    };

    /**
     * Get with default value in following conditions: <li>params is null.</li>
     * <li>key is not contained.</li> <li>value is not instance of checkType.
     * 
     * @param params
     * @param key
     * @param checkType
     * @param defaultValue
     * @return
     */
    public static Object get(Params params, int key, Class<?> checkType, Object defaultValue)
    {
        Object obj = get(params, key, defaultValue);
        if (obj != null && !checkType.isInstance(obj))
        {
            return defaultValue;
        }
        return obj;
    }

    /**
     * Get with default value when params is null or key is not contained.
     * 
     * @param params
     * @param key
     * @param defaultValue
     * @return
     */
    public static Object get(Params params, int key, Object defaultValue)
    {
        if (params == null || !params.containsKey(key))
        {
            return defaultValue;
        }
        return params.get(key);
    }

    /**
     * Try to put a value into params. If params is null, do nothing.
     * 
     * @param params
     * @param key
     * @param value
     */
    public static void put(Params params, int key, Object value)
    {
        if (params != null)
        {
            params.put(key, value);
        }
    }

    /**
     * Safe way to check if a params which is possibly {@code null} contains a
     * key.
     * 
     * @param params
     * @param key
     * @return
     */
    public static boolean contains(Params params, int key)
    {
        if (params == null)
        {
            return false;
        }
        return params.containsKey(key);
    }

    /**
     * Safe way to recycle a params if it's not null.
     * 
     * @param params
     */
    public static void recycle(Params params)
    {
        if (params != null)
        {
            params.recycle();
        }
    }

    private final SparseArray<Object> mMap = new SparseArray<Object>();

    /**
     * Ensure {@link #obtain()} and {@link #recycle()} as a pair of calls.
     * 
     * @param src
     * @return
     */
    public static Params obtain()
    {
        return getPool().obtain();
    }

    /**
     * Obtain and merge src. Ensure {@link #obtain()} and {@link #recycle()} as
     * a pair of calls.
     * 
     * @param src
     * @return
     */
    public static Params obtain(Params src)
    {
        Params params = obtain();
        params.merge(src);
        return params;
    }

    private Params()
    {
    }

    public final void recycle()
    {
        mMap.clear();
        getPool().recycle(this);
    }

    private void ensureNotRecycled()
    {
        getPool().ensureNotRecycled(this);
    }

    public Object get(int key)
    {
        return get(key, false);
    }

    /**
     * get value
     * 
     * @param key
     * @param notNull
     *            if true an exception will be throwed when key is not contained
     *            or value is null.
     * @return
     */
    public Object get(int key, boolean notNull)
    {
        ensureNotRecycled();

        final Object value = mMap.get(key);
        if (value == null && notNull)
        {
            throw new RuntimeException("UcParams[" + key + "] is null");
        }
        return value;
    }

    /**
     * Copy srcKey to dstKey.
     * 
     * @param srcKey
     * @param dstKey
     */
    public Params copyValue(int srcKey, int dstKey)
    {
        ensureNotRecycled();

        int index = mMap.indexOfKey(srcKey);
        if (index < 0)
        {
            throw new RuntimeException("Can't find UcParams[" + srcKey + "]");
        }
        put(dstKey, mMap.valueAt(index));
        return this;
    }

    public boolean containsKey(int key)
    {
        ensureNotRecycled();

        return mMap.indexOfKey(key) >= 0;
    }

    public Params put(int key, Object value)
    {
        ensureNotRecycled();

        mMap.put(key, value);
        return this;
    }

    public void remove(int key)
    {
        ensureNotRecycled();

        mMap.remove(key);
    }

    public void clear()
    {
        ensureNotRecycled();

        mMap.clear();
    }

    public Params merge(Params src)
    {
        ensureNotRecycled();

        if (src != null)
        {
            final SparseArray<Object> srcMap = src.mMap;
            final SparseArray<Object> map = mMap;
            for (int i = 0, size = srcMap.size(); i < size; ++i)
            {
                map.put(srcMap.keyAt(i), srcMap.valueAt(i));
            }
        }
        return this;
    }

    /**
     * Implement ILinkedPoolable
     */
    private Object mNext = null;

    @Override
    public Object getNext()
    {
        return mNext;
    }

    @Override
    public void setNext(Object nextNode)
    {
        mNext = nextNode;
    }
}