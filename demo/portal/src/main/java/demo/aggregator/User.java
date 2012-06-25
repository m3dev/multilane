package demo.aggregator;

public class User {

    private String id;

    private String area;

    public User(String id, String area) {
        setId(id);
        setArea(area);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

}
