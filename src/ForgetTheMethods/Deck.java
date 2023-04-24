package ForgetTheMethods;


import java.util.Random;
import java.util.Stack;

public class Deck {

    String[] theDeck;
    boolean[] truthyDeck;
    Stack<String> shuffledDeck;

    /*
    MAKE THIS A TWO DIMENSIONAL ARRAYLIST OF TYPES INT AND STRING!
     */
    Deck() {
        theDeck = new String[18];
        for (int i = 0; i < 18; i++) {

            if (i < 6) {
                theDeck[i] = "Rock of " + (6 - i);
            } else if (i < 12) {
                theDeck[i] = "Paper of " + (12 - i);
            } else if (i < 18) {
                theDeck[i] = "Scissors of " + (18 - i);
            }
        }
    }

    Stack<String> allocDeck() {
        truthyDeck = new boolean[18];
        shuffledDeck = new Stack<>();
        Random rand = new Random();
        int i = 0;

        while (i < 18) {
            int possible = rand.nextInt(18);
            if (truthyDeck[possible] == false) {
                shuffledDeck.push(theDeck[possible]);
                i++;
                truthyDeck[possible] = true;
            }
        }
        return shuffledDeck;
    }
}

