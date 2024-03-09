package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        boolean x = true;
        String currency, from, to;
        double amount;
        try {
            while (x) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Welcome to Currency Exchanger\t please choose operation");
                System.out.println("1: real-time currency value\t 2: Currency converter at one to other");
                int operation = scanner.nextInt();
                if (operation == 1) {
                    System.out.println("$ USD\t € CAD \t ¥ EUR\t £ HKD\t £ INR");
                    System.out.print("Enter currency Code: ");
                    currency = scanner.next();
                    sendHttpGETRequest(currency);
                }
                if (operation == 2) {
                    System.out.println("Welcome Currency Exchanger");
                    System.out.println("From?");
                    System.out.println("$ USD\t € CAD \t ¥ EUR\t £ HKD\t £ INR");
                    from = scanner.next();
                    System.out.println("To?");
                    System.out.println("$ USD\t € CAD \t ¥ EUR\t £ HKD\t £ INR");
                    to = scanner.next();
                    System.out.println("Enter amount");
                    amount = scanner.nextDouble();
                    sendHttpGETRequest(from, to, amount);
                    System.out.println("to continue, enter 1\t to exit, enter 2");
                    int ce = scanner.nextInt();
                    if (ce == 2) {
                        x = false;
                        System.out.println("Bye");
                    } else x = true;
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Please enter correct value");
        }
    }

    private static void sendHttpGETRequest(String from, String to, double amount) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String url = "http://api.exchangeratesapi.io/v1/latest?access_key=10fcec1bfe93e9d8696c274caff8c2b5&base=" + from + "&symbols=" + to;
            URL get_url = new URL(url);
            JsonNode jsonNode = httpRequestE(get_url);
            JsonNode dataNodeFrom = jsonNode.get("base");
            JsonNode dataNodeTo = jsonNode.get("rates").get(to);
            JsonNode data = jsonNode.get("date");
            System.out.println("data: "+ data.asText()+ "\n" + amount + " " + dataNodeFrom.asText() + " = " + dataNodeTo.asDouble() * amount + " " + to);
        } catch (IOException e) {
            System.out.println("Error! Enter all fields correctly");
        }
    }

    private static void sendHttpGETRequest(String code) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String url = "https://api.currencyapi.com/v3/latest?apikey=cur_live_wrisSefhNBzaN0HHuG0J12WICKaaJsrcF5b5Nv6p&currencies=" + code;
            URL urlGet = new URL(url);
            JsonNode resultNode = httpRequestE(urlGet);

            JsonNode jsonNode = objectMapper.readTree(resultNode.toString());
            JsonNode dataNode = jsonNode.get("data").get(code);
            String currencyCode = dataNode.get("code").asText();
            double currencyValue = dataNode.get("value").asDouble();
            System.out.println("Currency Code: " + currencyCode);
            System.out.println("Currency Value: " + currencyValue);
        } catch (IOException e) {
            System.out.println("Invalid Code entered or problem with network");
        }
    }

    private static JsonNode httpRequestE(URL getUrlAddress) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        HttpURLConnection connection = (HttpURLConnection) getUrlAddress.openConnection();
        connection.setRequestMethod("GET");
        String readLine = null;
        JsonNode response = null;
        int getUrlCode = connection.getResponseCode();

        if (getUrlCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer responseBuffer = new StringBuffer();
            while ((readLine = in.readLine()) != null) {
                responseBuffer.append(readLine);
            }
            in.close();
            response = mapper.readTree(responseBuffer.toString());
        }
        return response;
    }
} 


