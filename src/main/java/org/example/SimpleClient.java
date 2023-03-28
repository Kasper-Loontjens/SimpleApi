package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SimpleClient {

    // Don't forget to start the server first
    public static void main(String[] args) throws IOException, ParseException {


        Scanner scan = new Scanner(System.in);
        String requestString = "greetings";
        String requestInt = "greetings";
        String jsonObjNameForGet;

        while (true){

            // Asks for which phrase
            System.out.println("-------------------");
            System.out.println("Write 1, 2 or quit");
            System.out.println("1.Greetings");
            System.out.println("2.Farewells");
            String message = scan.nextLine();
            if (message.equalsIgnoreCase("quit")){
                break;
            } else if (message.equalsIgnoreCase("1")) {
                requestString = "greetings";
                jsonObjNameForGet = "greeting";
            } else {
                requestString = "farewells";
                jsonObjNameForGet = "farewell";
            }

            // asks for how many phrases / max 5
            System.out.println("Write a number between one and five or quit");
            message = scan.nextLine();
            if (message.equalsIgnoreCase("quit")){
                break;
            } else {
                requestInt = message;
            }
            System.out.println("-------------------");

            //Gets Json object
            URL myUrl = new URL("http://localhost:8080/"+requestString+"/"+requestInt);
            JSONObject phrasesJsonObj = fetchJsonFromApi(myUrl);
            System.out.println(phrasesJsonObj);
            System.out.println("-------------------");

            //Prints everything the objects contains
            boolean done = false;
            int intForWhileLoop = 0;
            while (done == false){
                intForWhileLoop++;
                if (phrasesJsonObj.get(jsonObjNameForGet+intForWhileLoop) != null){
                    JSONObject onePhraseObj = (JSONObject) phrasesJsonObj.get(jsonObjNameForGet+intForWhileLoop);
                    System.out.println("Phrase: "+ onePhraseObj.get("name"));
                    System.out.println("Rating: "+ onePhraseObj.get("rating") + "/10");
                    System.out.println("Comment: "+ onePhraseObj.get("comment"));
                    System.out.println("------");
                }
                else {
                    done = true;
                }
            }
            System.out.println("Press enter for new phrases");
            message = scan.nextLine();
        }
    }

    //Methods
    static JSONObject fetchJsonFromApi(URL apiUrl)throws IOException, ParseException {

        //Creates a connection and sends GET request
        HttpURLConnection conn = (HttpURLConnection) apiUrl.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        //Checks for success
        if (conn.getResponseCode() == 200) System.out.println("lyckat");
        else System.out.println("miss");

        //Parses into Json Object
        StringBuilder strData = new StringBuilder();
        Scanner scanner = new Scanner(apiUrl.openStream());
        while (scanner.hasNext()) {
            strData.append(scanner.nextLine());
        }
        scanner.close();
        return  (JSONObject) new JSONParser().parse(String.valueOf(strData));
    }
}
