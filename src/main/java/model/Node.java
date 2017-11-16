package model;

/**
 * Created by #{QiuYW }on 2017/11/14.
 */
public class Node {
    private int id;
    private String country;
    private double longitude;
    private double latitude;

    public Node() {
    }

    public Node(int id, String country, double longitude, double latitude) {
        this.id = id;
        this.country = country;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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
                ", country='" + country + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
