import org.dreambot.api.utilities.Timer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.HashMap;

/**
 * Created by cloga_000 on 4/7/2018.
 */
public class Bot {

    public static String API_BASE = "http://localhost:8000/api/v1";
    public static String API_BOTS = API_BASE + "/bots";

    private static String description = "";
    private static long checkinInterval = 2 * 60 * 1000;
    private Timer checkinTimer;
    private long nextCheckin;
    private String apiToken;
    private String locationName;
    private int world;

    public Bot(String apiToken, String locationName, int world) {
        this.apiToken = apiToken;
        this.locationName = locationName;
        this.world = world;
        this.checkinTimer = new Timer();
    }

    public boolean shouldCheckin() {
        return checkinTimer.elapsed() > nextCheckin;
    }

    public void checkin() {
        nextCheckin = checkinTimer.elapsed() + checkinInterval;
        String clientID = Utils.getClientID();
        try {
            HashMap<String, Object> data = new HashMap<>();
            data.put("client_id", clientID);
            data.put("world", world);
            data.put("description", description);
            data.put("location_name", locationName);
            HttpClient req = new HttpClient(API_BOTS + "/" + clientID + "/checkin", apiToken);
            BufferedReader reader = new BufferedReader(new InputStreamReader(req.post(data)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
