import java.io.*;
import java.net.*;
import java.util.Random;

public class gameThread extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String code = "";

    public gameThread(Socket socket) {
        this.socket = socket;
    }

    String processGuess(String guess) {
        int bulls = 0;
        int cows = 0;

        char[] codeChars = code.toCharArray();
        char[] guessChars = guess.toCharArray();
        boolean[] codeMatched = new boolean[4];
        boolean[] guessMatched = new boolean[4];

        for (int i = 0; i < 4; i++) {
            if (guessChars[i] == codeChars[i]) {
                bulls++;
                codeMatched[i] = true;
                guessMatched[i] = true;
            }
        }

        for (int i = 0; i < 4; i++) {
            if (!guessMatched[i]) {
                for (int j = 0; j < 4; j++) {
                    if (!codeMatched[j] && guessChars[i] == codeChars[j]) {
                        cows++;
                        codeMatched[j] = true;
                        break;
                    }
                }
            }
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < cows; i++) {
            result.append("C");
        }

        for (int i = 0; i < bulls; i++) {
            result.append("B");
        }

        while (result.length() < 4) {
            result.append(" ");
        }

        return result.toString();
    }

    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Random rand = new Random();
            StringBuilder codeBuilder = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                codeBuilder.append(rand.nextInt(10));
            }
            code = codeBuilder.toString();

            System.out.println("CODE : " + code);

            out.println("GO");

            String result = "";
            int counter = 0;
            String guess;

            while (!result.equals("BBBB") && counter < 20) {
                guess = in.readLine();

                if (guess == null || guess.equals("QUIT")) {
                    break;
                }

                result = processGuess(guess);
                out.println(result);
                counter++;
            }

            out.close();
            in.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}