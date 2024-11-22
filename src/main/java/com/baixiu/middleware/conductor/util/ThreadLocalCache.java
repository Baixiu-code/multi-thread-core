package com.baixiu.middleware.conductor.util;

import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

public final class ThreadLocalCache<T>{

    //保证可见性和原子性
    private static volatile ConcurrentHashMap<String,Object> map;
    private static final Object lock=new Object();

    //使用 softReference 来做缓存.通过 softReference 的特性.利用jvm 在内存不足时可以进行协助回收.
    //优势:能够通过继承 InheritableThreadLocal 来实现线程副本的传递.
    //劣势: 1.默认继承父线程的 threadLocal 增加了性能开销,虽然可以实现自定义.同时拉长了线程副本的生命周期.在不知情的场景下.
    // 没有进行 remove 很可能出现线程泄露.
    private final static InheritableThreadLocal<SoftReference<ConcurrentHashMap<String,Object>>> threadLocal =
            new InheritableThreadLocal<SoftReference<ConcurrentHashMap<String,Object>>>(){

        //初始化一个 ConcurrentHashMap 对象.并被 softReference 包裹.从工具提供的角度,来做到一定的内存泄露
        @Override
        protected SoftReference<ConcurrentHashMap<String,Object>> initialValue() {
            return new SoftReference<ConcurrentHashMap<String,Object>>(new ConcurrentHashMap<String,Object>());
        }

        //默认返回父类中所有的线程副本.
        @Override
        protected SoftReference<ConcurrentHashMap<String,Object>> childValue(SoftReference<ConcurrentHashMap<String,Object>> parentValue) {
            return parentValue;
        }
    };


    //double lock check .保证在多线程的情况下,不会出现线程中 map 为空的场景.防止出现 npe
    public static ConcurrentHashMap<String,Object> getAllThreadLocalMap(){
        SoftReference<ConcurrentHashMap<String,Object>> softReference=threadLocal.get();
        if(softReference==null || softReference.get()==null) {
            synchronized (lock){
                if(softReference==null || softReference.get()==null){
                    map=new ConcurrentHashMap<String,Object>();
                    threadLocal.set(new SoftReference(map));
                }
            }
        }
        return map;
    }

    //移除所有线程上的缓存
    public static void removeAll(){
        if(getAllThreadLocalMap()!=null){
            threadLocal.remove();
        }
    }

    //移除指定 key 的缓存
    public static void removeByKey(String key){
        if(getAllThreadLocalMap()!=null){
            ConcurrentHashMap map=getAllThreadLocalMap();
            map.remove(key);
        }
    }

    //设置缓存
    public static void put(String key,Object value){
        ConcurrentHashMap map=getAllThreadLocalMap();
        map.put(key,value);
    }

    //获取缓存
    public static <T> T get(String key){
        ConcurrentHashMap map=getAllThreadLocalMap();
        return (T) map.get(key);
    }
}
