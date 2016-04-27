package de.simonlaux.guestbook;

public class KeysForHtmlScan {
	private String original;
	private String key;

	public KeysForHtmlScan(String thing, String key) {
		// TODO Auto-generated constructor stub
		this.original = thing;
		this.key = key;
	}
	
	String thing(){return original;}
	String key(){return key;}

}
