package de.simonlaux.guestbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileGuestbookStore implements GuestbookStore {

	@Override
	public void add(GuestbookEntry entry) {

		// Format:
		// date; email; inhalt;
		// date; email; inhalt;
		// date; email; inhalt;

		String email = null;
		String inhalt = null;
		try {
			email = URLEncoder.encode(entry.getEmail(), "UTF-8");
			inhalt = URLEncoder.encode(entry.getInhalt(), "UTF-8");
			// new URLDecoder().decode("", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (java.lang.NullPointerException e) {
			System.out.println("Guestbookentry nicht vollständig");
		}

		String adding = new String((entry.getDate().getTime() + ";" + email + ";" + inhalt + ";")) + '\n';
		String all = adding + open();
		File file = new File("DATA4");
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(all);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// System.out.println("Die neuen Daten lauten:" + '\n' + adding);
		System.out.println("Jemand hat einen neuen Eintrag getaetigt");
	}

	@Override
	public List<GuestbookEntry> getAll() {
		// TODO Auto-generated method stub
		List<GuestbookEntry> liste = new ArrayList<GuestbookEntry>();
		String chache = open();
		try {
			String[] strArray = chache.split(('\n' + ""));
			for (int i = 0; i < strArray.length; i++) {
				String[] strArray2 = strArray[i].split(";");
				new URLDecoder();
				try {
					liste.add(new GuestbookEntry(new Date(Long.parseLong(strArray2[0])),
							URLDecoder.decode(strArray2[1], "UTF-8"), URLDecoder.decode(strArray2[2], "UTF-8")));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					System.out.println("Fehler: Datei ist evt leer - NumberFormatException");
					// e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return liste;
		} catch (java.lang.NumberFormatException e) {
			System.out.println("Datei ist leer");
			return null;
		}

	}

	/**
	 * public static void main(String[] args) { GuestbookStore store = new
	 * FileGuestbookStore();
	 * 
	 * GuestbookEntry entry = new GuestbookEntry(new Date(),
	 * "the-email@simonlaux.de",
	 * "der La<|<>aang inhalt....hier stän\nd mal    wäs[]// $§'");
	 * 
	 * //store.add(entry); //store.getAll(); }
	 **/

	private String open() {
		File file = new File("DATA4");
		try {
			FileReader fr = new FileReader(file);
			char[] temp = new char[(int) file.length()];
			fr.read(temp);
			fr.close();
			return new String(temp);

		} catch (FileNotFoundException e1) {
			System.err.println("Datei nicht gefunden: " + file);
			try {
				FileWriter writer = new FileWriter(file);
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e2) {
			e2.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean close() {
		return true;
	}

}
