package demo.cache;

public interface CacheStore<K, V> {

    V get(K key);

    void set(K key, V value, long expire);

}
