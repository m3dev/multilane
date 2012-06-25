package demo.aggregator;

public class AggregatorSetting {

    private User user;

    public AggregatorSetting(User user) {
        setUser(user);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
