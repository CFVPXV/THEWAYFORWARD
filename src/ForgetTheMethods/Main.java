package ForgetTheMethods;

import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        Thread driver;
        Scanner s = new Scanner(System.in);
        System.out.println("Enter S for server, C for Client");
        String ans = s.next();


        if (ans.compareTo("S") == 0) {
            System.out.println("Starting Server...");
            driver = new Thread(new Listener(9000, 100));
        }


        else if (ans.compareTo("C") == 0) {
            System.out.println("Enter Name: ");
            String n = s.next();
            System.out.println("Waiting on other player...");
            driver = new Thread(new Client("127.0.0.1", 9000, n));
        }



        else {
            System.out.println("Incorrect input!");
            return;
        }
        driver.start();
//Buisness Logic if necessary
        try {
            driver.join();
        } catch (InterruptedException e) {
            System.out.println("Error waiting on thread! " + e.getMessage());
        }
    }
}

