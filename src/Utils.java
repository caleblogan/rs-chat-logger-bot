import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by cloga_000 on 4/7/2018.
 */
public class Utils {

    public static String getClientID() {
        String PID = getPID();
        return getMACAddress() + ":" + PID;
    }

    public static String getMACAddress() {
        return "123";
    }

    public static String getPID() {
        return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }
}
