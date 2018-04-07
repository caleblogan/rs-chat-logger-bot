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
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            return sb.toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e){
            e.printStackTrace();
        }
        return "";
    }

    public static String getPID() {
        return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }

    public static void main(String[] args) {
        System.out.println(getClientID());
        System.out.println(ManagementFactory.getRuntimeMXBean().getName());
    }
}
