package ForgetTheMethods;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Stack;

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

    public HandleSession(Socket p1Session, Socket p2Session, Stack<String> deck){
        player1 = p1Session;
        player2 = p2Session;
        gameDeck = deck;
    }

    @Override
    public void run() {

        try {
            p1Deck = new ArrayList<>();
            p2Deck = new ArrayList<>();
            inP1 = new DataInputStream(player1.getInputStream());
            inP2 = new DataInputStream(player2.getInputStream());
            outP1 = new ObjectOutputStream(player1.getOutputStream());
            outP2 = new ObjectOutputStream(player2.getOutputStream());

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
                int lok = inP1.readInt();
                String p1Suit = inP1.readUTF();
                int p1playgain = inP1.readInt();
                int steppenzu = inP2.readInt();
                String p2suit = inP2.readUTF();
                int p2playgain = inP2.readInt();
                System.out.println("Player 1 says: " + lok + " " + p1Suit + " " + p1playgain);
                System.out.println("Player 2 says: " + steppenzu + " " + p2suit + " " + p2playgain);
                System.out.println("Leaving this here...");
            }
        }catch(IOException e){
                throw new RuntimeException(e);
            }
    }
}
