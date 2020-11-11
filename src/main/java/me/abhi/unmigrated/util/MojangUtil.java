package me.abhi.unmigrated.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MojangUtil {

    public static String[] data(String username, String password, Proxy proxy) throws Exception {

        String genClientToken = UUID.randomUUID().toString();

        // Setting up json POST request
        String payload = "{\"agent\": {\"name\": \"Minecraft\",\"version\": 1},\"username\": \"" + username
                + "\",\"password\": \"" + password + "\",\"clientToken\": \"" + genClientToken + "\"}";

        String output = postReadURL(payload, new URL("https://authserver.mojang.com/authenticate"), proxy);

        JSONObject jsonObject = (JSONObject) new JSONParser().parse(output);
        JSONObject selectedProfile = (JSONObject) jsonObject.get("selectedProfile");
        String id = (String) selectedProfile.get("id");

        // Setting up patterns
        String authBeg = "{\"accessToken\":\"";
        String authEnd = "\",\"clientToken\":\"";
        String clientEnd = "\",\"selectedProfile\"";

        // What we are looking for
        String authtoken = getStringBetween(output, authBeg, authEnd);
        return new String[]{authtoken, id};
    }

    private static String postReadURL(String payload, URL url, Proxy proxy) throws Exception {
        HttpsURLConnection con = (HttpsURLConnection) (proxy != null ? url.openConnection(proxy) : url.openConnection());
        con.setReadTimeout(15000);
        con.setConnectTimeout(15000);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoInput(true);
        con.setDoOutput(true);

        OutputStream out = con.getOutputStream();
        out.write(payload.getBytes("UTF-8"));
        out.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String output = "";
        String line = null;
        while ((line = in.readLine()) != null)
            output += line;

        in.close();

        return output;
    }

    private static String getStringBetween(String base, String begin, String end) {

        Pattern patbeg = Pattern.compile(Pattern.quote(begin));
        Pattern patend = Pattern.compile(Pattern.quote(end));

        int resbeg = 0;
        int resend = base.length() - 1;

        Matcher matbeg = patbeg.matcher(base);

        if (matbeg.find())
            resbeg = matbeg.end();

        Matcher matend = patend.matcher(base);

        if (matend.find())
            resend = matend.start();

        return base.substring(resbeg, resend);
    }

}
