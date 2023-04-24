package ForgetTheMethods;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Listener implements Runnable{

    ServerSocket serve;
    ExecutorService connections;
    Socket player1;
    Socket player2;
    Stack<String> gameDeck;

    Listener(int port, int max){
        Deck noShuff = new Deck();
        gameDeck = noShuff.allocDeck();
        connections = Executors.newFixedThreadPool(max);
        try {
            serve = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            player1 = serve.accept();
            System.out.println("Player 1 connected!");

            player2 = serve.accept();
            System.out.println("Player 2 connected!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connections.execute(new HandleSession(player1, player2, gameDeck));

    }
}
