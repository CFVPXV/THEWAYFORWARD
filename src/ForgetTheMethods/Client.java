package ForgetTheMethods;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Client implements Runnable{

    String ip;
    int port;
    String name;
    Socket con;
    ObjectInputStream inFromServer;
    DataOutputStream outToServer;
    Scanner scnr;
    ArrayList<String> myDeck;
    ArrayList<String> resultQueue;
    Queue<String> sendQueue;
    int powerpoints;
    Client(String i, int p, String n){
        ip = i;
        port = p;
        name = n;
        powerpoints = 10;
        scnr = new Scanner(System.in);
        try {
            con = new Socket(ip, port);
            inFromServer = new ObjectInputStream(con.getInputStream());
            outToServer = new DataOutputStream(con.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {

            try {
                System.out.println("Players connected!");
                System.out.println("Welcome " + name + "!");

                try {
                    myDeck = (ArrayList<String>) inFromServer.readObject();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                sendQueue = new LinkedList<>();

                while (true) {
                    System.out.println("You have a total of " + powerpoints + " power points!");
                    for (int i = 0; i < myDeck.size(); i++) {
                        System.out.println((i + 1) + ") " + myDeck.get(i));
                    }
                    System.out.println("Which will you play? Or just quit now...");
                    int select = scnr.nextInt();
                    if(select == 0){
                        outToServer.writeInt(select);
                        break;
                    }
                    String playedCard = myDeck.get(select - 1);
                    String powerLevelString = playedCard.replaceAll("[^0-9]", "");
                    String suitString = playedCard.replaceAll("[0-9]", "");
                    int parsed = Integer.parseInt(powerLevelString);
                    if(parsed > powerpoints){
                        System.out.println("Sorry, you don't have enough power to play that card!");
                        continue;
                    }
                    powerpoints -= parsed;
                    System.out.println(parsed);
                    outToServer.writeInt(parsed);
                    outToServer.writeUTF(suitString);
                    myDeck.remove(select - 1);
                    System.out.println("Sent " + playedCard + " to server!");
                    System.out.println("Would you like to play another card?");
                    int playgain = scnr.nextInt();
                    outToServer.writeInt(playgain);
                    if(playgain == 0) {
                        break;
                    }
                }
                System.out.println("Awaiting for other player to complete moves");
                resultQueue = (ArrayList<String>) inFromServer.readObject();
                System.out.println(resultQueue.size());
                for(int i = 0; i < resultQueue.size(); i++){
                    System.out.println(resultQueue.get(i));
                }
            }catch(IOException e){
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
    }

    }

