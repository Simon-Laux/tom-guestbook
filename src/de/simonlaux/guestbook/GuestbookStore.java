package de.simonlaux.guestbook;

import java.util.List;

public interface GuestbookStore {
	void add(GuestbookEntry entry);

	List<GuestbookEntry> getAll();
}
