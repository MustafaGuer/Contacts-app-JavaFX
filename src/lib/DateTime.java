package lib;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class DateTime {

	public static String getFormattedDateTime() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter myFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
		String formattedNow = now.format(myFormatter);
		return formattedNow;
	}
}
