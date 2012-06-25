package demo.aggregator;

import com.m3.multilane.HttpGetToStringMultiLane;
import com.m3.multilane.action.HttpGetToStringAction;
import demo.cache.CacheStore;
import demo.cache.JVMCacheStore;

import java.util.List;
import java.util.Map;

public class Aggregator {

    private AggregatorSetting aggregatorSetting;

    private List<ContentSetting> contentSettings;

    private CacheStore<String, String> cacheStore = new JVMCacheStore();

    private HttpGetToStringMultiLane multiLane = new HttpGetToStringMultiLane();

    public Aggregator(AggregatorSetting aggregatorSetting, List<ContentSetting> contentSettings) {
        setAggregatorSetting(aggregatorSetting);
        for (ContentSetting setting : contentSettings) {
            if (aggregatorSetting.getUser() != null) {
                setting.setSourceUrl(setting.getSourceUrl()
                        + "?userId=" + aggregatorSetting.getUser().getId()
                        + "&area=" + aggregatorSetting.getUser().getArea());
            }
        }
        setContentSettings(contentSettings);
    }

    public Aggregator start() {
        for (ContentSetting setting : contentSettings) {
            String defaultValue = setting.getDefaultContent();
            if (!setting.skipThis()) {
                if (setting.isCacheable()) {
                    HttpGetToStringAction action = new CacheableHttpGetToStringAction(
                            setting.getSourceUrl(),
                            setting.getTimeoutMilliseconds(),
                            setting.getCacheKey(aggregatorSetting.getUser().getId()),
                            setting.getMaxCacheSeconds(),
                            cacheStore);
                    multiLane.start(setting.getId(), action, defaultValue);
                } else {
                    HttpGetToStringAction action = new HttpGetToStringAction(
                            setting.getSourceUrl(),
                            setting.getTimeoutMilliseconds());
                    multiLane.start(setting.getId(), action, defaultValue);
                }
            } else {
                multiLane.start(setting.getId(), new EchoAction("Skipped!"), defaultValue);
            }
        }
        return this;
    }

    public Map<String, String> collectValues() {
        return multiLane.collectValues();
    }

    public AggregatorSetting getAggregatorSetting() {
        return aggregatorSetting;
    }

    public void setAggregatorSetting(AggregatorSetting aggregatorSetting) {
        this.aggregatorSetting = aggregatorSetting;
    }

    public List<ContentSetting> getContentSettings() {
        return contentSettings;
    }

    public void setContentSettings(List<ContentSetting> contentSettings) {
        this.contentSettings = contentSettings;
    }

    public CacheStore getCacheStore() {
        return cacheStore;
    }

    public void setCacheStore(CacheStore cacheStore) {
        this.cacheStore = cacheStore;
    }

    public HttpGetToStringMultiLane getMultiLane() {
        return multiLane;
    }

    public void setMultiLane(HttpGetToStringMultiLane multiLane) {
        this.multiLane = multiLane;
    }

}
