package de.simonlaux.guestbook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class InMemoryGuestbookStore implements GuestbookStore{
	private List<GuestbookEntry> entries = new ArrayList<>();
	
	public InMemoryGuestbookStore() {
		entries.add(new GuestbookEntry(new Date(), "test1@example.org", "Hallo Welt"));
	}

	@Override
	public void add(GuestbookEntry entry) {
		entries.add(entry);
	}

	@Override
	public List<GuestbookEntry> getAll() {
		return Collections.unmodifiableList(entries);
	}

	@Override
	public boolean close() {
		return true;
	}
}
