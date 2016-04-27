package de.simonlaux.guestbook;

import java.util.Date;

public class GuestbookEntry {
	private Date date;
	private String email;
	private String inhalt;

	public GuestbookEntry(Date date, String email, String inhalt) {
		this.date = date;
		this.email = email;
		this.inhalt = inhalt;
	}

	Date getDate() {
		return date;
	}

	String getEmail() {
		return email;
	}

	String getInhalt() {
		return inhalt;
	}

}
