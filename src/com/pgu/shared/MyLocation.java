package com.pgu.shared;

public enum MyLocation {
    Brest(48.390394, -4.4860760000000255) //
    , Dusseldorf(51.2277411, 6.773455600000034) //
    , Lausanne(46.5199617, 6.6335970999999745) //
    , Madrid(40.4166909, -3.70034540000006) //
    , Nantes(47.218371, -1.553621) //
    , Paris(48.856614, 2.3522219000000177) //
    , Prague(50.0755381, 14.43780049999998) //
    , Rostock(54.0834186, 12.100428899999997) //
    ;

    private final double latitude;
    private final double longitude;

    MyLocation(final double latitude, final double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
