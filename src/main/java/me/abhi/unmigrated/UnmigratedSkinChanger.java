package me.abhi.unmigrated;

import me.abhi.unmigrated.mojang.MojangConnection;
import me.abhi.unmigrated.util.MojangUtil;
import me.abhi.unmigrated.util.StringUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.Scanner;

public class UnmigratedSkinChanger {

    public static String username;
    public static String password;
    public static String skinUrl;
    public static String skinModel;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        //Collect the username
        System.out.println("Enter the username of the account");
        username = scanner.next();

        //Collect the password
        System.out.println("Enter the password of the account");
        password = scanner.next();

        //Collect skin url
        System.out.println("Enter the url to the image you would like to use as your skin");
        skinUrl = scanner.next();

        //Collect skin model
        System.out.println("Enter the skin model you would like to use (slim/steve)");
        skinModel = scanner.next();

        try {
            String[] data = MojangUtil.data(username, password, null);

            String authToken = data[0];
            String uuid = data[1];

            //Auth the account with mojang
            new MojangConnection(authToken, uuid, null);

            //Change the skin
            changeSkin(authToken, uuid, skinUrl, null);
            System.out.println("Successfully changed the skin on " + username + "!");
        } catch (Exception ex) {
            System.out.println("Error logging into the account.");
        }
    }

    public static void changeSkin(String token, String uuid, String skinUrl, Proxy proxy) throws Exception {
        URL url = new URL("https://api.mojang.com/user/profile/" + uuid + "/skin");
        HttpURLConnection httpURLConnection = (HttpURLConnection) (proxy != null ? url.openConnection(proxy) : url.openConnection());
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + token);

        String inputString = "model=" + (skinModel.equalsIgnoreCase("slim") ? "slim" : "") + "&url=" + StringUtil.encodeValue(skinUrl);

        try (OutputStream os = httpURLConnection.getOutputStream()) {
            byte[] input = inputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
    }
}
