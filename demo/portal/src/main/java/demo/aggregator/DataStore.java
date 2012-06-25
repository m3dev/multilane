package demo.aggregator;

import com.m3.scalaflavor4j.F1;
import com.m3.scalaflavor4j.Seq;

import java.util.ArrayList;
import java.util.List;

public class DataStore {

    static final List<ContentSetting> contentSettings = new ArrayList<ContentSetting>();

    static {
        contentSettings.add(new ContentSetting(
                "headline",
                "Headline news contents",
                "http://localhost:8080/api/headline",
                500,
                true,
                ContentSetting.CacheType.All,
                30,
                "Unavailable"
        ));
        contentSettings.add(new ContentSetting(
                "weather",
                "Weather contents for users",
                "http://localhost:8080/api/weather",
                600,
                true,
                ContentSetting.CacheType.User,
                30,
                "Unavailable"
        ));
        contentSettings.add(new ContentSetting(
                "mail",
                "Received mail",
                "http://localhost:8080/api/mail",
                800,
                false,
                ContentSetting.CacheType.None,
                0,
                "Unavailable"
        ));
        contentSettings.add(new ContentSetting(
                "recommends",
                "Recommended articles for user",
                "http://localhost:8080/api/recommends",
                600,
                true,
                ContentSetting.CacheType.User,
                120,
                "Unavailable"
        ));
    }

    static final List<User> users = new ArrayList<User>();

    static {
        users.add(new User("Alice", "Tokyo"));
        users.add(new User("Bob", "Kyoto"));
    }

    private DataStore() {
    }

    public static List<String> findAllContentSettingIds() {
        return Seq._(contentSettings).map(new F1<ContentSetting, String>() {
            public String _(ContentSetting setting) {
                return setting.getId();
            }
        }).toList();
    }

    public static List<ContentSetting> findAllContentSettings() {
        return Seq._(contentSettings).map(new F1<ContentSetting, ContentSetting>() {
            public ContentSetting _(ContentSetting setting) {
                return setting.copy();
            }
        }).toList();
    }

    public static User findUser(final String userId) {
        return Seq._(users).find(new F1<User, Boolean>() {
            public Boolean _(User user) {
                return user.getId().equals(userId);
            }
        }).getOrNull();
    }

}
