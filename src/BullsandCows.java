import java.io.*;
import java.net.*;
import java.util.Scanner;

public class BullsandCows {
    public static void main(String[] args) {
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        int counter = 0;
        String guessString = "";
        Scanner scanner = new Scanner(System.in);

        try {
            socket = new Socket("localhost", 12345);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String serverResponse = in.readLine();
            if (serverResponse.equals("GO")) {
                System.out.println("Welcome to Bulls and Cows. You will try to guess a 4 digit code using");
                System.out.println("only the digits 0-9). You will lose the game if you are unable to guess");
                System.out.println("the code correctly in 20 guesses. Good Luck!\n");
            }

            while (counter < 20) {
                do {
                    System.out.print("Please enter your guess for the secret code or \"QUIT\" : ");
                    guessString = scanner.nextLine();

                    if (guessString.equals("QUIT")) {
                        System.out.println("\nGoodbye but please play again!");
                        out.println("QUIT");
                        return;
                    }
                } while (!verifyInput(guessString));

                out.println(guessString);
                counter++;

                serverResponse = in.readLine();
                System.out.println(guessString + " " + serverResponse);

                if (serverResponse.equals("BBBB")) {
                    System.out.println("\nCongratulations!!! You guessed the code correctly in " + counter + " guesses");
                    break;
                }

                if (counter >= 20) {
                    System.out.println("Sorry - the game is over. You did not guess the code correctly in 20 moves.");
                }
            }

            out.close();
            in.close();
            socket.close();
            scanner.close();

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: localhost.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: localhost.");
            System.exit(1);
        }
    }

    static boolean verifyInput(String gs) {
        if (gs.equals("QUIT")) {
            return true;
        }

        if (gs.length() != 4) {
            System.out.println("Improperly formatted guess.");
            return false;
        }

        for (int i = 0; i < 4; i++) {
            if (!Character.isDigit(gs.charAt(i))) {
                System.out.println("Improperly formatted guess.");
                return false;
            }
        }

        return true;
    }
}