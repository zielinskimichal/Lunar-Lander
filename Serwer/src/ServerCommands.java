package Serwer.src;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

/**
 * Klasa rozpatrująca zadanie, oraz implementuje funkcje tablicy wynikow
 *
 */
public class ServerCommands {
	/**
	 * Metoda obsługująca żądania klienta
	 * 
	 * @param request żądanie od klienta
	 * @return odpowiedz serwera
	 * @throws IOException
	 */
	public static String serverAction(String request) throws IOException {
		String serverMessage;

		String[] commands = request.split(",");

		request = commands[0];

		switch (request) {

			case "GET_SCORES":
				serverMessage = getScores();
				System.out.println(serverMessage);
				break;
			case "SAVE_SCORE":
				serverMessage = saveScore(commands[1], commands[2]);
				break;
			case "GET_CONFIG":
				serverMessage = getConfig();
				break;
			default:
				serverMessage = "INVALID_COMMAND";
		}
		return serverMessage;
	}

	/**
	 * Metoda obsługująca żądanie listy najlepszych wyników
	 * 
	 * @return linia tekstu z danymi oddzielonymi ",".
	 * @throws IOException
	 */

	public static String getScores() throws IOException {
		String dataToReturn = "";
		try {

			BufferedReader reader = new BufferedReader(new FileReader("./Serwer/resources/ranking.txt"));
			String line = reader.readLine();

			while (line != null) {

				dataToReturn = dataToReturn + line + ",";

				line = reader.readLine();

			}

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return dataToReturn;
	}

	/**
	 * Metoda obsługująca żądanie listy poziomów.
	 * 
	 * @return linia tekstu z danymi oddzielonymi "," oraz ";"
	 * @throws IOException
	 */
	public static String getConfig() throws IOException {
		String dataToReturn = "";
		try {
			String configFilePath = "./Serwer/resources/config.txt";
			BufferedReader reader = new BufferedReader(new FileReader(configFilePath));
			String line = reader.readLine();

			while (line != null) {

				dataToReturn = dataToReturn + line + ";";

				line = reader.readLine();

			}

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return dataToReturn;
	}

	/**
	 * Metoda zapisujaca nowy wynik na serwerze
	 * 
	 * @param nickname Informacja o nazwie gracza.
	 * @param score    Informacja o wyniku gracza.
	 * @return Informacja o powodzeniu/niepowodzeniu zapisu.
	 * @throws IOException
	 */
	private static String saveScore(String nickname, String score) throws IOException {

		ArrayList<Player> players = new ArrayList<Player>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader("./Serwer/resources/ranking.txt"));
			String line = reader.readLine();

			while (line != null) {

				String[] dataHolder = line.split(",");

				Player temp = new Player(dataHolder[0], Integer.parseInt(dataHolder[1]));

				players.add(temp);

				line = reader.readLine();

			}

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		players.add(new Player(nickname, Integer.parseInt(score)));
		Collections.sort(players);
		try {
			FileWriter rankWriter = new FileWriter("./Serwer/resources/ranking.txt");
			for (int i = 0; i < players.size(); i++)
				rankWriter.write(players.get(i).nickname + "," + players.get(i).score + "\n");
			rankWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
			return "Zapisywania nie powiodlo sie";
		}
		return "Zapisywanie powiodło się";
	}

}

/**
 * Klasa odpowiedzialna za przechowywanie wyniku pojedynczego gracza i
 * porównywanie go z innymi graczami
 */
class Player implements Comparable {
	String nickname;
	int score;

	Player(String name, int points) {
		nickname = name;
		score = points;
	}

	@Override
	public int compareTo(Object o) {
		Player p = (Player) o;
		return p.score - this.score;
	}
}