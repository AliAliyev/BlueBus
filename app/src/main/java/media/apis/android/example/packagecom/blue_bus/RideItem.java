package media.apis.android.example.packagecom.blue_bus;

/**
 * Created by Ali on 20/04/2016.
 */
public class RideItem {
    private String route;
    private String date;
    private int seats;

    public RideItem(final String route, final String date, final  int seats) {
        this.route = route;

        this.date = date;

        this.seats = seats;

    }

    public String getRoute() {return route;}

    public String getDate() { return date; }

    public int getSeats() { return seats; }
}
