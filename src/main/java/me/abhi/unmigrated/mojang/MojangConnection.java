package me.abhi.unmigrated.mojang;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

public class MojangConnection {

    private HttpURLConnection httpURLConnection;

    @SneakyThrows
    public MojangConnection(String token, String uuid, Proxy proxy) {
        //Send request to security endpoint before we access the profile
        sendSecurityRequest(token, proxy);

        //Open connection
        URL url = new URL("https://api.mojang.com/user/profile/" + uuid + "/name");
        httpURLConnection = (HttpURLConnection) (proxy != null ? url.openConnection(proxy) : url.openConnection());
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + token);

    }

    @SneakyThrows
    private void sendSecurityRequest(String token, Proxy proxy) {
        URL url = new URL("https://api.mojang.com/user/security/challenges");
        HttpURLConnection httpURLConnection = (HttpURLConnection) (proxy != null ? url.openConnection(proxy) : url.openConnection());
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + token);

        StringBuilder content;
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpURLConnection.getInputStream()))) {

            String line;
            content = new StringBuilder();

            while ((line = in.readLine()) != null) {

                content.append(line);
                content.append(System.lineSeparator());
            }
        }
    }

    public void disconnect() {
        httpURLConnection.disconnect();
    }
}
