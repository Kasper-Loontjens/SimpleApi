package org.example;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
    public static void main(String[] args)throws IOException {
        JSONObject hello = new JSONObject();
        hello.put("message", "hello");
        JSONObject bye = new JSONObject();
        bye.put("message", "bye");

        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Server is running");

        while (true){
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");

            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String hTTPMessage;

            String fistLine = reader.readLine();
            String method = fistLine.split("\\s")[0];
            System.out.println(method);

            String uRL = fistLine.split("\\s+")[1];
            System.out.println(uRL);

            while ((hTTPMessage = reader.readLine()) != null){
                System.out.println(hTTPMessage);
                if (hTTPMessage.isEmpty()){
                    break;
                }
            }

            String cRLF = "\r\n";
            OutputStream clientOutput = clientSocket.getOutputStream();
            clientOutput.write("HTTP/1.1 200 Ok\r\n".getBytes());
            clientOutput.write("\r\n".getBytes());

            if (uRL.equals("/hello")){
                clientOutput.write(hello.toJSONString().getBytes());
            } else if (uRL.equals("/bye")) {
                clientOutput.write(bye.toJSONString().getBytes());
            }else {
                clientOutput.write("No api".getBytes());
            }
            clientOutput.write("\r\n\r\n".getBytes());
            clientOutput.flush();
            clientOutput.close();
            reader.close();
            System.out.println("closed");
        }


    }
}
