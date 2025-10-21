import java.net.*;
import java.util.*;

public class UDPChatServer {
    public static void main(String[] args) throws Exception {
        int port = 9876;
        var socket = new DatagramSocket(port);
        System.out.println("Serwer czatu UDP nasłuchuje na porcie " + port);

        var buffer = new byte[1024];

        while (true) {
            var packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            var message = new String(packet.getData(), 0, packet.getLength());
            var clientAddress = packet.getAddress();
            var clientPort = packet.getPort();

            System.out.println("Klient [" + clientAddress + ":" + clientPort + "]: " + message);

            // Odpowiedź do klienta
            var response = "Serwer otrzymał: " + message;
            var responseBuffer = response.getBytes();
            var responsePacket = new DatagramPacket(
                    responseBuffer,
                    responseBuffer.length,
                    clientAddress,
                    clientPort
            );
            socket.send(responsePacket);
        }
    }
}