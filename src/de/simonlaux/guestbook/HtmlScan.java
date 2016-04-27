package de.simonlaux.guestbook;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class HtmlScan {
	List<KeysForHtmlScan> keep = new ArrayList<KeysForHtmlScan>();
	int le = 0;
	String[] tags;

	public HtmlScan(String... tags) {

		this.tags = tags;
		le = tags.length;
		for (int x = 0; x < tags.length; x++) {
			keep.add(new KeysForHtmlScan("%3C" + tags[x] + "%3E",
					"h%5B%3F8%C2%B6" + tags[x].hashCode() + "%C2%B68%3F%5D"));
			keep.add(new KeysForHtmlScan("%3C%2F" + tags[x] + "%3E",
					"%5B%3F8%C2%B6" + tags[x].hashCode() + "%C2%B68%3F%5D"));
		}
	}

	String filter(String input) {
		try {
			input = URLEncoder.encode(input, "UTF-8");

			for (int x = 0; x < (le * 2); x++) {
				input = input.replaceAll(keep.get(x).thing(), keep.get(x).key());
			}
			input = input.replaceAll("%3Chi%3E", "*5325");
			input = input.replaceAll("%3E", "--%3E");
			input = input.replaceAll("%3C", "%3C%21--");

			for (int x = 0; x < (le * 2); x++) {
				input = input.replaceAll(keep.get(x).key(), keep.get(x).thing());
			}

			for (int x = 0; x < tags.length; x++) {
				input = fixHtml(input, tags[x]);
			}

			input = URLDecoder.decode(input, "UTF-8");

			return input;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return input;
	}

	public static void main(String args[]) {
		HtmlScan scan = new HtmlScan("hi", "b", "a", "p");
		System.out.println(
				scan.filter("<hi>ds</hi></hi></p>ds<a>d</a><b>ffdss<script></b><a></script>f$$sdddsdddsQfds<<ff<>>>"));
	}

	private String fixHtml(String input, String tag) {
		int a = StringUtils.countMatches(input, "%3C" + tag + "%3E");
		int b = StringUtils.countMatches(input, "%3C%2F" + tag + "%3E");
		if (a == b) {
			return input;
		} else if (a > b) {
			return input + "%3C%2F" + tag + "%3E";
		} else {
			return "%3C" + tag + "%3E" + input;
		}

	}
}
