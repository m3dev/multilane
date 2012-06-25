package demo.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class JVMCacheStore implements CacheStore<String, String> {

    static class Content {
        String value;
        long expire;

        Content(String value, long expire) {
            this.value = value;
            this.expire = expire;
        }
    }

    private static final ConcurrentMap<String, Content> cache = new ConcurrentHashMap<String, Content>();

    public String get(String key) {
        Content content = cache.get(key);
        if (content != null && content.expire > System.currentTimeMillis()) {
            return content.value;
        } else {
            cache.remove(key);
            return null;
        }
    }

    public void set(String key, String value, long expire) {
        if (key != null && value != null) {
            cache.put(key, new Content(value, expire));
        }
    }

}
