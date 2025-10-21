import java.net.*;
import java.util.Scanner;

public class UDPChatClient {
    public static void main(String[] args) throws Exception {
        var socket = new DatagramSocket();
        var address = InetAddress.getLocalHost();
        int port = 9876;
        var scanner = new Scanner(System.in);

        System.out.println("Klient czatu UDP. Wpisz wiadomość i naciśnij Enter.");
        System.out.println("Wpisz 'exit' aby zakończyć.\n");

        while (true) {
            System.out.print("Ty: ");
            var message = scanner.nextLine();

            if (message.equalsIgnoreCase("exit")) {
                break;
            }

            // Wysłanie wiadomości
            var buffer = message.getBytes();
            var packet = new DatagramPacket(buffer, buffer.length, address, port);
            socket.send(packet);

            // Oczekiwanie na odpowiedź
            var responseBuffer = new byte[1024];
            var responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
            socket.setSoTimeout(5000); // timeout 5 sekund

            try {
                socket.receive(responsePacket);
                var response = new String(
                        responsePacket.getData(),
                        0,
                        responsePacket.getLength()
                );
                System.out.println("Serwer: " + response + "\n");
            } catch (SocketTimeoutException e) {
                System.out.println("Brak odpowiedzi od serwera\n");
            }
        }

        socket.close();
        scanner.close();
        System.out.println("Zakończono");
    }
}