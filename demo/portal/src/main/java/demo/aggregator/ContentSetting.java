package demo.aggregator;

public class ContentSetting {

    public static enum CacheType {
        All, User, None
    }

    private String id;

    private String name;

    private String sourceUrl;

    private int timeoutMilliseconds;

    private boolean cacheable;

    private CacheType cacheType;

    private int maxCacheSeconds;

    private String defaultContent;

    public ContentSetting copy() {
        return new ContentSetting(
                getId(),
                getName(),
                getSourceUrl(),
                getTimeoutMilliseconds(),
                isCacheable(),
                getCacheType(),
                getMaxCacheSeconds(),
                getDefaultContent()
        );
    }

    public ContentSetting(
            String id, String name, String sourceUrl, int timeoutMilliseconds,
            boolean cacheable, CacheType cacheType, int maxCacheSeconds,
            String defaultContent) {

        setId(id);
        setName(name);
        setSourceUrl(sourceUrl);
        setTimeoutMilliseconds(timeoutMilliseconds);
        setCacheable(cacheable);
        setCacheType(cacheType);
        setMaxCacheSeconds(maxCacheSeconds);
        setDefaultContent(defaultContent);
    }

    public boolean skipThis() {
        // Bob cannot use mail
        return getSourceUrl().contains("userId=Bob") && getSourceUrl().contains("/mail");
    }

    public String getCacheKey(String userId) {
        if (getCacheType() == CacheType.All) {
            return getId() + "_ALL";
        } else if (getCacheType() == CacheType.User) {
            return getId() + "_" + userId;
        } else {
            throw new IllegalStateException("Unexpected pattern - " + getCacheType());
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public int getTimeoutMilliseconds() {
        return timeoutMilliseconds;
    }

    public void setTimeoutMilliseconds(int timeoutMilliseconds) {
        this.timeoutMilliseconds = timeoutMilliseconds;
    }

    public boolean isCacheable() {
        return cacheable;
    }

    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    public CacheType getCacheType() {
        return cacheType;
    }

    public void setCacheType(CacheType cacheType) {
        this.cacheType = cacheType;
    }

    public int getMaxCacheSeconds() {
        return maxCacheSeconds;
    }

    public void setMaxCacheSeconds(int maxCacheSeconds) {
        this.maxCacheSeconds = maxCacheSeconds;
    }

    public String getDefaultContent() {
        return defaultContent;
    }

    public void setDefaultContent(String defaultContent) {
        this.defaultContent = defaultContent;
    }

}
