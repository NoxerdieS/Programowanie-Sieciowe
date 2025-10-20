import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 6000;

        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
             Scanner keyboard = new Scanner(System.in, "UTF-8")) {

            System.out.println("Połączono z serwerem. Schemat: Ty (klient) piszesz pierwszy.");

            while (true) {
                System.out.print("Ty (klient): ");
                String msg = keyboard.nextLine();
                out.println(msg);
                if ("/quit".equalsIgnoreCase(msg)) {
                    System.out.println("Koniec.");
                    break;
                }

                String reply = in.readLine();
                if (reply == null) {
                    System.out.println("Serwer się rozłączył.");
                    break;
                }
                System.out.println("Serwer: " + reply);
                if ("/quit".equalsIgnoreCase(reply)) {
                    System.out.println("Serwer zakończył rozmowę.");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
