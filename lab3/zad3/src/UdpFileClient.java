import java.net.*;
import java.io.*;
import java.util.Scanner;

public class UdpFileClient {
    private static final int PACKET_SIZE = 1024;
    private static final int PORT = 9876;

    public static void main(String[] args) throws Exception {
        var socket = new DatagramSocket();
        var address = InetAddress.getLocalHost();
        var scanner = new Scanner(System.in);

        System.out.println("Klient pobierania plików UDP");
        System.out.print("Podaj nazwę pliku do pobrania: ");
        var fileName = scanner.nextLine();

        // Wyślij żądanie pliku
        var requestBuffer = fileName.getBytes();
        var requestPacket = new DatagramPacket(
                requestBuffer, requestBuffer.length, address, PORT
        );
        socket.send(requestPacket);
        System.out.println("Wysłano żądanie pliku: " + fileName);

        // Odbierz rozmiar pliku lub błąd
        var buffer = new byte[1024];
        var packet = new DatagramPacket(buffer, buffer.length);
        socket.setSoTimeout(5000);
        socket.receive(packet);

        var response = new String(packet.getData(), 0, packet.getLength());

        if (response.startsWith("ERROR:")) {
            System.out.println("Błąd: " + response.substring(6));
            socket.close();
            scanner.close();
            return;
        }

        if (!response.startsWith("SIZE:")) {
            System.out.println("Nieoczekiwana odpowiedź od serwera");
            socket.close();
            scanner.close();
            return;
        }

        long fileSize = Long.parseLong(response.substring(5));
        System.out.println("Rozmiar pliku: " + fileSize + " bajtów");
        System.out.println("Pobieranie...");

        // Odbierz plik
        var outputFileName = "pobrane_" + fileName;
        var fos = new FileOutputStream(outputFileName);
        long bytesReceived = 0;

        while (bytesReceived < fileSize) {
            var fileBuffer = new byte[PACKET_SIZE];
            var filePacket = new DatagramPacket(fileBuffer, fileBuffer.length);
            socket.receive(filePacket);

            int bytesToWrite = (int) Math.min(
                    filePacket.getLength(),
                    fileSize - bytesReceived
            );
            fos.write(filePacket.getData(), 0, bytesToWrite);
            bytesReceived += bytesToWrite;

            // Wyświetl postęp
            int progress = (int)((bytesReceived * 100) / fileSize);
            System.out.print("\rPostęp: " + progress + "% (" +
                    bytesReceived + "/" + fileSize + " bajtów)");
        }

        fos.close();
        socket.close();
        scanner.close();

        System.out.println("\n\nPlik zapisany jako: " + outputFileName);
        System.out.println("Pobieranie zakończone!");
    }
}