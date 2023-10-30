package programming3.rocky.entity;

import programming3.rocky.entity.enumdata.Danger;

public class Coordinate {
    private long id;
    private double longitude;
    private double latitude;
    private double datetime;
    private Danger danger;
    private String description;
    private String usrName;

    public Coordinate(long id, double longitude, double latitude, double datetime, Danger danger, String usrName) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.datetime = datetime;
        this.danger = danger;
        this.usrName = usrName;
    }
}