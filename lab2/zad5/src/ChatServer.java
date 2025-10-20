import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatServer {
    public static void main(String[] args) {
        int port = 6000;
        System.out.println("CHAT server start na porcie " + port);

        try (ServerSocket server = new ServerSocket(port);
             Socket socket = server.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
             Scanner keyboard = new Scanner(System.in, "UTF-8")) {

            System.out.println("Połączono z: " + socket.getRemoteSocketAddress());
            System.out.println("Schemat: Klient pisze pierwszy, serwer odpowiada, potem znowu klient itd.");

            while (true) {
                String fromClient = in.readLine();
                if (fromClient == null) {
                    System.out.println("Klient się rozłączył.");
                    break;
                }
                System.out.println("Klient: " + fromClient);
                if ("/quit".equalsIgnoreCase(fromClient)) {
                    System.out.println("Klient zakończył rozmowę.");
                    break;
                }

                System.out.print("Ty (serwer): ");
                String reply = keyboard.nextLine();
                out.println(reply);
                if ("/quit".equalsIgnoreCase(reply)) {
                    System.out.println("Kończę czat.");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
