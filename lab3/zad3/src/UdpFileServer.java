import java.net.*;
import java.io.*;

public class UdpFileServer {
    private static final int PACKET_SIZE = 1024;
    private static final int PORT = 9876;

    public static void main(String[] args) throws Exception {
        var socket = new DatagramSocket(PORT);
        System.out.println("Serwer plików UDP nasłuchuje na porcie " + PORT);

        while (true) {
            // Odbierz żądanie pliku od klienta
            var buffer = new byte[1024];
            var packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            var fileName = new String(packet.getData(), 0, packet.getLength());
            var clientAddress = packet.getAddress();
            var clientPort = packet.getPort();

            System.out.println("Klient żąda pliku: " + fileName);

            var file = new File(fileName);

            if (!file.exists() || !file.isFile()) {
                // Wyślij informację o błędzie
                var errorMsg = "ERROR:Plik nie istnieje".getBytes();
                var errorPacket = new DatagramPacket(
                        errorMsg, errorMsg.length, clientAddress, clientPort
                );
                socket.send(errorPacket);
                System.out.println("Plik nie znaleziony: " + fileName);
                continue;
            }

            // Wyślij rozmiar pliku
            long fileSize = file.length();
            var sizeMsg = ("SIZE:" + fileSize).getBytes();
            var sizePacket = new DatagramPacket(
                    sizeMsg, sizeMsg.length, clientAddress, clientPort
            );
            socket.send(sizePacket);
            System.out.println("Wysyłanie pliku, rozmiar: " + fileSize + " bajtów");

            // Wyślij plik w paczkach
            var fis = new FileInputStream(file);
            var fileBuffer = new byte[PACKET_SIZE];
            int bytesRead;
            int packetNumber = 0;

            while ((bytesRead = fis.read(fileBuffer)) != -1) {
                var filePacket = new DatagramPacket(
                        fileBuffer, bytesRead, clientAddress, clientPort
                );
                socket.send(filePacket);
                packetNumber++;

                // Krótkie opóźnienie aby nie przeciążyć sieci
                Thread.sleep(1);
            }

            fis.close();
            System.out.println("Wysłano plik w " + packetNumber + " paczkach\n");
        }
    }
}