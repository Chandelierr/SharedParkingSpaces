package graduationdesign.sharedparkingspaces.model;

/**
 * Created by 王梦洁 on 2018/4/11.
 */

public class ParkingLot {
    public static final String USING = "USING";
    public static final String FREE = "FREE";

    private int id;
    private double latitude;
    private double longitude;
    private String usingState;
    private String province;
    private String city;
    private String country;
    private String address;
    private String parkingName;
    private int purpose;


    public ParkingLot(double latitude, String usingState, double longitude) {
        this.latitude = latitude;
        this.usingState = usingState;
        this.longitude = longitude;
    }
}
