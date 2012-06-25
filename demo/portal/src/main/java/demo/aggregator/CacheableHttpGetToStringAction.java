package demo.aggregator;

import com.m3.multilane.action.HttpGetToStringAction;
import com.m3.scalaflavor4j.Either;
import com.m3.scalaflavor4j.Right;
import demo.cache.CacheStore;

import java.util.Date;

public class CacheableHttpGetToStringAction extends HttpGetToStringAction {

    private String cacheKey;
    private Integer maxCacheSeconds;
    private CacheStore<String, String> cacheStore;

    public CacheableHttpGetToStringAction(
            String url, Integer timeoutMillis,
            String cacheKey, Integer maxCacheSeconds, CacheStore<String, String> cacheStore) {
        super(url, timeoutMillis);
        setCacheKey(cacheKey);
        setMaxCacheSeconds(maxCacheSeconds);
        setCacheStore(cacheStore);
    }

    @Override
    public Either<Throwable, String> apply() {
        String cached = cacheStore.get(cacheKey);
        if (cached != null) {
            return Right._(cached);
        }
        Either<Throwable, String> result = super.apply();
        if (result.isRight() && result.right().isDefined()) {
            cacheStore.set(cacheKey,
                    result.right().getOrNull(),
                    new Date().getTime() + getMaxCacheSeconds() * 1000L);
        }
        return result;
    }

    public CacheStore<String, String> getCacheStore() {
        return cacheStore;
    }

    public void setCacheStore(CacheStore<String, String> cacheStore) {
        this.cacheStore = cacheStore;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public Integer getMaxCacheSeconds() {
        return maxCacheSeconds;
    }

    public void setMaxCacheSeconds(Integer maxCacheSeconds) {
        this.maxCacheSeconds = maxCacheSeconds;
    }
}
