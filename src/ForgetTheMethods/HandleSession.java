package ForgetTheMethods;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class HandleSession implements Runnable{

    private Socket player1;
    private Socket player2;

    private DataInputStream inP1;
    private DataInputStream inP2;
    private ObjectOutputStream outP1;
    private ObjectOutputStream outP2;
    Stack<String> gameDeck;
    ArrayList<String> p1Deck;
    ArrayList<String> p2Deck;
    boolean p1Done;
    boolean p2Done;

    ArrayList<String> p1SuitPlayed;
    ArrayList<String> p2SuitPlayed;
    ArrayList<Integer> p1PowerPlayed;
    ArrayList<Integer> p2PowerPlayed;

    int p1Score;
    int p2Score;

    ArrayList<String> p1resultsStack;
    ArrayList<String> p2resultStack;

    Queue<String> p1DataQueue;
    Queue<String> p2DataQueue;
    ReentrantLock theLock;

    public HandleSession(Socket p1Session, Socket p2Session, Stack<String> deck){
        player1 = p1Session;
        player2 = p2Session;
        gameDeck = deck;
        theLock = new ReentrantLock();
    }

    @Override
    public void run() {

        try {
            p1Deck = new ArrayList<>();
            p2Deck = new ArrayList<>();
            p1SuitPlayed = new ArrayList<>();
            p2SuitPlayed = new ArrayList<>();
            p1PowerPlayed = new ArrayList<>();
            p2PowerPlayed = new ArrayList<>();
            inP1 = new DataInputStream(player1.getInputStream());
            inP2 = new DataInputStream(player2.getInputStream());
            outP1 = new ObjectOutputStream(player1.getOutputStream());
            outP2 = new ObjectOutputStream(player2.getOutputStream());
            p1Done = false;
            p2Done = false;
            p1Score = 0;
            p2Score = 0;
            p1resultsStack = new ArrayList<>();
            p2resultStack = new ArrayList<>();
            p1DataQueue = new LinkedList<>();
            p2DataQueue = new LinkedList<>();

            for(int i = 0; i < 10; i++){
                if(i % 2 == 0){
                    p1Deck.add(gameDeck.pop());
                }else{
                    p2Deck.add(gameDeck.pop());
                }
            }

            outP1.writeObject(p1Deck);
            outP2.writeObject(p2Deck);


            while (true) {

                inP1.available();
                    if(p1Done && p2Done){
                        break;
                    }
                    if(inP1.available() != 0){
                        int lok = inP1.readInt();
                        if(lok == 0){
                            p1Done = true;
                            continue;
                        }
                        p1PowerPlayed.add(lok);
                        String p1Suit = inP1.readUTF();
                        p1SuitPlayed.add(p1Suit);
                        int p1playgain = inP1.readInt();
                        System.out.println("Player 1 says: " + lok + " " + p1Suit + " " + p1playgain);
                        if (p1playgain == 0) {
                            System.out.println("P1 done");
                            p1Done = true;
                        }
                    }

                    if(p1Done && p2Done){
                        break;
                    }
                    if(inP2.available() != 0) {
                        int steppenzu = inP2.readInt();
                        if(steppenzu == 0){
                            p2Done = true;
                            continue;
                        }
                        p2PowerPlayed.add(steppenzu);
                        String p2suit = inP2.readUTF();
                        p2SuitPlayed.add(p2suit);
                        int p2playgain = inP2.readInt();
                        System.out.println("Player 2 says: " + steppenzu + " " + p2suit + " " + p2playgain);
                        if (p2playgain == 0) {
                            System.out.println("P2 done");
                            p2Done = true;
                        }
                    }
                    if(p1Done && p2Done){
                        break;
                    }

            }
            System.out.println("Left the infinite loop!");

            for(int i = 0; i < p1PowerPlayed.size(); i++){
                p1Score += p1PowerPlayed.get(i);
                System.out.println(p1Score);
            }
            p1resultsStack.add("Your initial score was: " + p1Score);

            for(int i = 0; i < p2PowerPlayed.size(); i++){
                p2Score += p2PowerPlayed.get(i);

                System.out.println(p2Score);
            }
            p2resultStack.add("Your initial score was: " + p2Score);

            for(int i = 0; i < p1SuitPlayed.size(); i++){
                if(p1SuitPlayed.get(i).contains("Rock")){
                    for(int j = 0; j < p2SuitPlayed.size(); j++){
                        if(p2SuitPlayed.get(j).contains("Scissors")){
                            p1Score++;
                            System.out.println(p1Score);
                            p1resultsStack.add("You played rock, player 2 played scissors: " + p1Score);
                        }
                    }
                }
                if(p1SuitPlayed.get(i).contains("Paper")){
                    for(int j = 0; j < p2SuitPlayed.size(); j++){
                        if(p2SuitPlayed.get(j).contains("Rock")){
                            p1Score++;
                            System.out.println(p1Score);
                            p1resultsStack.add("You played Paper, player 2 had Rock: " + p1Score);
                        }
                    }
                }
                if(p1SuitPlayed.get(i).contains("Scissors")){
                    for(int j = 0; j < p2SuitPlayed.size(); j++){
                        if(p2SuitPlayed.get(j).contains("Paper")){
                            p1Score++;
                            System.out.println(p1Score);
                            p1resultsStack.add("You played Scissors, player 2 had Paper: " + p1Score);
                        }
                    }
                }

            }



            for(int i = 0; i < p2SuitPlayed.size(); i++){
                if(p2SuitPlayed.get(i).contains("Rock")){
                    for(int j = 0; j < p1SuitPlayed.size(); j++){
                        if(p1SuitPlayed.get(j).contains("Scissors")){
                            p2Score++;
                            p2resultStack.add("Player 2 played Rock, player 1 had Scissors: " + p2Score);
                        }
                    }
                }
                if(p2SuitPlayed.get(i).contains("Paper")){
                    for(int j = 0; j < p1SuitPlayed.size(); j++){
                        if(p1SuitPlayed.get(j).contains("Rock")){
                            p2Score++;
                            p2resultStack.add("Player 2 played Paper, player 1 had Rock: " + p2Score);
                        }
                    }
                }
                if(p2SuitPlayed.get(i).contains("Scissors")){
                    for(int j = 0; j < p1SuitPlayed.size(); j++){
                        if(p1SuitPlayed.get(j).contains("Paper")){
                            p2Score++;
                            p2resultStack.add("Player 2 played Scissors, player 1 had Paper: " + p2Score);
                        }
                    }
                }
            }

            if(p1Score > p2Score){
                p1resultsStack.add("You have won!");
                p2resultStack.add("You have lost!");
                outP1.writeObject(p1resultsStack);
                outP2.writeObject(p2resultStack);
            } else if (p1Score < p2Score) {
                p2resultStack.add("You have won!");
                p1resultsStack.add("You have lost!");
                outP1.writeObject(p1resultsStack);
                outP2.writeObject(p2resultStack);
            } else {
                p1resultsStack.add("Its a draw!");
                p2resultStack.add("Its a draw!");
                outP1.writeObject(p1resultsStack);
                outP2.writeObject(p2resultStack);
            }



        }catch(IOException e){
                throw new RuntimeException(e);
            }
        }
    }
