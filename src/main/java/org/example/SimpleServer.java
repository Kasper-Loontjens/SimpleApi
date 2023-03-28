package org.example;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class SimpleServer {
    public static void main(String[] args) throws IOException, ParseException {
        //Json objects passed in GET requests
        Object greetingsObject = new JSONParser().parse(new FileReader("src/main/resources/greetings.json"));
        Object farewellObject = new JSONParser().parse(new FileReader("src/main/resources/farewells.json"));
        JSONObject greetingsPhrasesJson = (JSONObject) greetingsObject;
        JSONObject farewellPhrasesJson = (JSONObject) farewellObject;

        //Creates server
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Server is running");

        while (true){

            //Accepts new user
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");

            //Reads request
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String hTTPMessage;

            //Saves first line of request
            String fistLine = reader.readLine();
            //Saves Method
            String method = fistLine.split("\\s")[0];
            System.out.println(method);
            //Saves Url from request
            String uRL = fistLine.split("\\s+")[1];
            System.out.println(uRL);
            String[] uRLS = splitURL(uRL);

            //User can write how many variables they want in JSON object. Here its taken from the URL
            int uRLInt = 5;
            if (uRLS.length > 2){
                uRLInt = convertToUrlInt(uRLS[2]);
            }
            System.out.println(uRLInt);

            //Prints out the rest of the request
            while ((hTTPMessage = reader.readLine()) != null){
                System.out.println(hTTPMessage);
                if (hTTPMessage.isEmpty()){
                    break;
                }
            }

            String cRLF = "\r\n";
            OutputStream clientOutput = clientSocket.getOutputStream();

            //Depending on method creates a response
            String response = "";
            switch (method){
                case "GET":
                    response = createGetResponse(uRLS[1], uRLInt, greetingsPhrasesJson, farewellPhrasesJson);
                    break;
                case "POST":
                    //TODO create POST response
                    break;
            }

            //Writes response to client
            clientOutput.write(response.getBytes());
            clientOutput.write((cRLF+cRLF).getBytes());

            //Closes client connection
            clientOutput.flush();
            clientOutput.close();
            reader.close();
            clientSocket.close();
            System.out.println("closed");
        }
    }
//---------- Methods ----------
    static String[] splitURL(String url) {

        //Splits the URL
        String[] urls = url.split("/");
        for (String urlString : urls) {
            System.out.println("url: " + urlString);
        }
        return urls;

    }
    static int convertToUrlInt(String intString){
        //Saves second parameter in URL into an int between 1 and 5.
        int urlInt;
        try {
            urlInt = Integer.parseInt(intString);
            if (urlInt < 1){
                urlInt = 1;
            } else if (urlInt>5) {
                urlInt=5;
            }
        }catch (NumberFormatException ex){
            urlInt = 5;
        }
        return urlInt;
    }
    static String createGetResponse(String api, int amountOfJsonResponse, JSONObject greetingsPhrasesJson, JSONObject farewellPhrasesJson){

        //Creates GET response containing 1-5 greetings or farewells from JSON objects.
        JSONObject responseJsonObj = new JSONObject();
        String cRLF = "\r\n";
        String response = ("HTTP/1.1 200 Ok"+cRLF+cRLF);
        if (api.equals("greetings") ){
            for (int i = 0; i < amountOfJsonResponse; i++) {
                responseJsonObj.put(("greeting"+(i+1)), greetingsPhrasesJson.get("greeting"+(i+1)));

            }
            response += responseJsonObj.toJSONString();
            System.out.println(response);
        } else if (api.equals("farewells")) {
            for (int i = 0; i < amountOfJsonResponse; i++) {
                responseJsonObj.put(("farewell"+(i+1)), farewellPhrasesJson.get("farewell"+(i+1)));

            }
            response += responseJsonObj.toJSONString();
            System.out.println(response);
        }
        return response;
    }
}
