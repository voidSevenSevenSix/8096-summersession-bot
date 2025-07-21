package org.summersession.controlSystem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

public class Comms {
    private ServerSocket server;
    private Socket client;
    private BufferedReader in;
    private boolean success = true;

    public Comms(){
        try{
            server = new ServerSocket(5800);
            client = server.accept();
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        }
        catch(Exception e){
            System.out.println("[Comms.java] Initialization threw an exception: " + e);
            success = false;
        }
    }

    public boolean connected(){
        return success;
    }

    public Optional<String> readComms(){
        try{
            String data = in.readLine();
            if(data != null){
                return Optional.of(data);
            }
        }
        catch(Exception e){
            System.out.println("[Comms.java] Connection threw an exception: " + e);
            return Optional.of("EXIT");
        }
        return Optional.empty();
    }
}
