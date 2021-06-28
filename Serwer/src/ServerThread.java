package Serwer.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Klasa, która obsługuje wątek. Odpowiada za relacje klient-serwer.
 */
public class ServerThread implements Runnable {
	private Socket socket;

	public ServerThread(Socket socket) {
		this.socket = socket;
	}

	/**
	 * Metoda run otrzymuje żądanie od klienta, które następnie oddaje do
	 * przetworzenia klasie ServerCommands i wysyła odpowiedź do klienta.
	 */
	public void run() {
		try {
			while (true) {
				InputStream inputStream = socket.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				OutputStream outputStream = socket.getOutputStream();
				PrintWriter printWriter = new PrintWriter(outputStream, true);
				String fromClient = bufferedReader.readLine();

				if (fromClient != null) {
					System.out.println("From client: " + fromClient);
					String serverRespond = ServerCommands.serverAction(fromClient);
					printWriter.println(serverRespond + "\n");
					printWriter.flush();

				}
			}
		} catch (IOException e) {
			System.out.println("Connection lost");
		}

	}

}
