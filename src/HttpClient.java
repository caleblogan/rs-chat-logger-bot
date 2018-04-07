import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by clogan202 on 8/9/2016.
 */
public class HttpClient {

    private URL url;
    private String apiKey;
    private Proxy proxy;
    int timeout = 30000;

    public HttpClient(String url) throws MalformedURLException {
        this(url, null, null);
    }

    public HttpClient(String url, String apiKey) throws MalformedURLException {
        this(url, apiKey, null);
    }

    public HttpClient(String url, String apiKey, Proxy proxy) throws MalformedURLException {
        this.url = new URL(url);
        this.proxy = proxy;
        this.apiKey = apiKey;
    }

    public InputStream get() throws IOException {
        URLConnection con;
        if (proxy != null) {
            con = url.openConnection(proxy);
        } else {
            con = url.openConnection();
        }
//        if (url.getPath().contains(BotfarmClient.getHost())) {
//            con.setRequestProperty("pass", "bobsmyuncle62");
//        }
        if (apiKey != null) {
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
        }
        con.setDoOutput(true);
//        con.setRequestProperty("pass", "bobsmyuncle99");
        con.setRequestProperty("charset", "utf-8");
        return con.getInputStream();
    }

    public InputStream post(HashMap<String, Object> data) throws IOException {
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (postData.length() != 0) {
                postData.append("&");
            }
            postData.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            postData.append("=");
            postData.append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
        }

        byte[] postDataBytes = postData.toString().getBytes("UTF-8");
        URLConnection con;
        if (proxy != null) {
            con = url.openConnection(proxy);
        } else {
            con = url.openConnection();
        }
//        if (url.getPath().contains(BotfarmClient.getHost())) {
//            con.setRequestProperty("pass", "bobsmyuncle62");
//        }
        if (apiKey != null) {
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
        }

        con.setDoOutput(true);
//        con.setRequestProperty("pass", "bobsmyuncle99");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        con.setRequestProperty("Referer", "https://secure.runescape.com/m=account-creation/l=2/g=oldscape/create_account");
        con.setConnectTimeout(timeout);
        con.setReadTimeout(timeout);
//        con.setRequestProperty("charset", "utf-8");

        OutputStream os = con.getOutputStream();
        os.write(postDataBytes);
        os.flush();
        InputStream res = con.getInputStream();
        return res;
    }

    public static void main(String[] args) {
        HashMap<String, Object> data = new HashMap<>();
        try {
            HttpClient req = new HttpClient("http://localhost:8000/api/v1/chat/");
            BufferedReader reader = new BufferedReader(new InputStreamReader(req.post(data)));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("line:" + line);
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
