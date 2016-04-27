package de.simonlaux.guestbook;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class GuestbookServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int i;
	GuestbookStore store = new MySqlGuestbookStore("127.0.0.1", 3306, "guestbook", "guestbook", "123456","book");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		System.out.println("Aufrufzähler: " + i++);
		req.setAttribute("Inhalt", writing(store.getAll()));

		req.getRequestDispatcher("book.jsp").forward(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// resp.getWriter().println("Hey " + i++);
		HttpSession session = req.getSession();
		Object cooldownObj = session.getAttribute("cooldown");
		long cooldown = 0;
		int lasttext = 0;
		if (cooldownObj == null) {
		} else {
			cooldown = (long) cooldownObj;
		}
		if (session.getAttribute("lasttext") == null) {
		} else {
			lasttext = (int) session.getAttribute("lasttext");
		}

		if (cooldown <= (new Date().getTime()) && lasttext != req.getParameter("M").hashCode()) {
			String content=req.getParameter("M");
			content=content.replaceAll("<b>", "brgrg5");
			content=content.replaceAll("</b>", "brgrg6");
			content=content.replaceAll("<", "<!-- ");
			content=content.replaceAll(">", "-->");
			content=content.replaceAll("brgrg5", "<b>");
			content=content.replaceAll("brgrg6", "</b>");
			store.add(new GuestbookEntry(new Date(), req.getParameter("email"), content));
			System.out.println("Aufrufzähler: " + i++ + " über post");
			req.setAttribute("Inhalt", writing(store.getAll()));
			session.setAttribute("cooldown", (new Date().getTime() + 2000));
			session.setAttribute("lasttext", req.getParameter("M").hashCode());
			req.getRequestDispatcher("book.jsp").forward(req, resp);
		} else {
			resp.getWriter()
					.println("<h1>Hör auf das formular zu überlasten</h1><br>Neuladen um das Problem zu beheben");
		}
	}

	String writing(List<GuestbookEntry> l1) {
		int i = 0;
		String raus = "<br>";
		if (l1.size() >= 1) {

			while (l1.size() > i) {
				GuestbookEntry eintrag = l1.get(i);
				SimpleDateFormat sDF = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
				raus = raus + ("<h4>Am " + sDF.format(eintrag.getDate()) + " schrieb " + eintrag.getEmail() + ":</h1><p>"
						+ eintrag.getInhalt() + "</p><tr>");
				i++;
			}
			return raus;
		} else {
			return "Noch keine Einträge verfügbar";
		}
	}

	public void init(ServletConfig config) throws ServletException {
		System.out.println("Guestbookservice gestartet");
	}

	public void destroy() {
		store.close();
		System.out.println("Guestbookservice beendet");
	}

}
