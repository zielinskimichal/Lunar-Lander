package Serwer.src;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

/**
 * Klasa server
 *
 */
public class Server {
	private static int port;

	/**
	 * Metoda tworzy ServerSocket oraz rozpoczyna nowy wątek.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public Server runServer() throws IOException, InterruptedException {
		getServerInfo();
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("Server is ON");
		while (true) {
			Socket socket = serverSocket.accept();
			new Thread(new ServerThread(socket)).start();
		}

	}

	/**
	 * Metoda pobierajaca informacje na temat serwera
	 * 
	 * @throws IOException
	 */
	public static void getServerInfo() throws IOException {
		Properties p = new Properties();
		InputStream is = new FileInputStream("./Serwer/resources/port.properties");
		p.load(is);
		port = Integer.parseInt(p.getProperty("port"));
	}

}