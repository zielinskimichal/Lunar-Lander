package Serwer.src;

import java.io.IOException;

/**
 * Główna klasa, posiadająca metodę main
 *
 */
public class Main {
	/**
	 * Metoda main odpowiedzialna za utworzenie serwera
	 * 
	 * @throws IOException
	 * 
	 */
	public static void main(String[] args) throws IOException {
		Server server = new Server().runServer();
	}

}