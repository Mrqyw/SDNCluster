package model;

/**
 * Created by #{QiuYW }on 2017/11/14.
 */
public class Node {
    private int id;
    private String city;
    private double longitude;
    private double latitude;

    public Node() {
    }

    public Node(int id, String city, double longitude, double latitude) {
        this.id = id;
        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
