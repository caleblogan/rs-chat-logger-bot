import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.listener.MessageListener;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.widgets.message.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


@ScriptManifest(author = "okc", name = "Chat Logger", version = 0.1, description = "Streaming chat", category = Category.UTILITY)
public class main extends AbstractScript implements MessageListener {

    String API_BASE = "http://localhost:8000/api/v1";
    String API_MESSAGES = API_BASE + "/messages";

    String botLocationName = "Varrock Ge";
    int world = 301;

    Timer timer;
    Timer playerTimer;
    long nextPlayerUpdate;
    long nextRotate;
    Map<String, Tile> playerPositions;

    @Override
    public void onStart() {
        log("Chat Logger!");
        timer = new Timer();
        playerTimer = new Timer();
    }

    @Override
    public int onLoop() {
        if (playerTimer.elapsed() > nextPlayerUpdate) {
            updatePlayerPositions();
            nextPlayerUpdate = playerTimer.elapsed() + 2 * 1000;
        }
        if (timer.elapsed() > nextRotate) {
            getCamera().rotateTo(new Random().nextInt(100), new Random().nextInt(100));
            long nextRotateBase = timer.elapsed() + 2 * 60 * 1000;
            nextRotate = Calculations.random(nextRotateBase - 10 * 1000, nextRotateBase + 10 * 1000); // Rotate the camera in 10 minutes
        }
        return 300;
    }

    @Override
    public void onExit() {
        log("Stopping!");
    }

    private void updatePlayerPositions() {
        log("updating player positions");
        playerPositions = new HashMap<>();
        List<Player> players = getPlayers().all();
        for (Player player : players) {
            playerPositions.put(player.getName(), player.getTile());
        }
        log("Number of Players: " + players.size());
    }

    @Override
    public void onGameMessage(Message message) {

    }

    @Override
    public void onPlayerMessage(Message message) {
        try {
            HashMap<String, Object> data = new HashMap<>();
            data.put("username", message.getUsername());
            data.put("message", message.getMessage());
            data.put("type", message.getType());
            if (playerPositions.containsKey(message.getUsername())) {
                Tile pos = playerPositions.get(message.getUsername());
                data.put("tile", pos.getX() + " " + pos.getY() + " " + pos.getZ());
            }
            data.put("locationName", botLocationName);
            data.put("world", world);
//            data.put("created_at", message.getTime());
            HttpClient req = new HttpClient(API_MESSAGES);
            BufferedReader reader = new BufferedReader(new InputStreamReader(req.post(data)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTradeMessage(Message message) {

    }

    @Override
    public void onPrivateInMessage(Message message) {

    }

    @Override
    public void onPrivateOutMessage(Message message) {

    }
}
