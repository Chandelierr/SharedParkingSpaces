package graduationdesign.sharedparkingspaces.model;

/**
 * Created by 王梦洁 on 2018/4/11.
 */

public class ParkingLot {
    public static final String USING = "USING";
    public static final String FREE = "FREE";

    private int id;
    private double lat;
    private double lng;
    private String parking_name;
    private int used;
    //    private String province;
//    private String city;
//    private String country;
//    private String address;

//    private int purpose;

    public ParkingLot(){}

    public ParkingLot(double latitude, int usingState, double longitude) {
        lat = latitude;
        used = usingState;
        lng = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getParking_name() {
        return parking_name;
    }

    public void setParking_name(String parking_name) {
        this.parking_name = parking_name;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    @Override
    public String toString() {
        return "ParkingLot{" +
                "id=" + id +
                ", lat=" + lat +
                ", lng=" + lng +
                ", parking_name='" + parking_name + '\'' +
                ", used=" + used +
                '}';
    }
}
