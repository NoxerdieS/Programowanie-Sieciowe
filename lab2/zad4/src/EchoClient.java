import java.io.*;
import java.net.*;
import java.util.Scanner;

public class EchoClient {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 5000;

        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
             Scanner keyboard = new Scanner(System.in, "UTF-8")) {

            String serverHello = in.readLine();
            if (serverHello != null) System.out.println("Serwer: " + serverHello);

            while (true) {
                System.out.print("Ty: ");
                String line = keyboard.nextLine();
                out.println(line);

                String resp = in.readLine();
                if (resp == null) {
                    System.out.println("Serwer rozłączył się.");
                    break;
                }
                System.out.println("Serwer: " + resp);

                if ("/quit".equalsIgnoreCase(line)) break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
