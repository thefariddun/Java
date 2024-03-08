package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean x= true;
        while (x==true) {
            String from;
            Scanner scanner = new Scanner(System.in);
            System.out.println("1:$ USD\t 2:€ CAD \t 3:¥ EUR\t 4:£ HKD\t 5:£ INR");
            System.out.print("Enter currency Code: ");
            from = scanner.next();
            sendHttpGETRequest(from);
            System.out.println("to continue, enter 1\t to exit, enter 2");
            int ce=scanner.nextInt();
            if(ce==2){
                x=false;
                System.out.println("Bye");
            } else x=true;
        }
    }
    private static void sendHttpGETRequest(String code){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String url = "https://api.currencyapi.com/v3/latest?apikey=cur_live_wrisSefhNBzaN0HHuG0J12WICKaaJsrcF5b5Nv6p&currencies="+code;
            URL urlget = new URL(url);

            String readLine = null;

            HttpURLConnection connection = (HttpURLConnection) urlget.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer response = new StringBuffer();
                while ((readLine = in.readLine()) != null) {
                    response.append(readLine);
                }
                in.close();
                JsonNode jsonNode = objectMapper.readTree(response.toString());
                JsonNode dataNode = jsonNode.get("data").get(code);

                String currencyCode = dataNode.get("code").asText();
                double currencyValue = dataNode.get("value").asDouble();

                System.out.println("Currency Code: " + currencyCode);
                System.out.println("Currency Value: " + currencyValue);
                System.out.println(response.toString());
            } else {
                throw new Exception("Error");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
