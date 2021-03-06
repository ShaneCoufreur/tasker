package sh.strm.tasker.schedule;

public class ScheduleParser {

	/**
	 * Convert an `every` expression into its respective cron expression. Due to
	 * some different implementation on spring Cron, where it adds another field
	 * (Seconds) as the first field, this implementation isn't compatible with
	 * UNIX Cron standards.
	 * 
	 * @param expression
	 *            an Every time format expression
	 * @return a converted Cron (Spring) compatible expression
	 * 
	 * @throws IllegalArgumentException
	 *             when the expression parameter isn't correctly. Or
	 *             {@link NumberFormatException} when the number isn't in the
	 *             correct {@link Integer} format
	 */
	public static String expressionToCron(String expression) {
		// normalization
		String every = expression.toLowerCase().trim();

		String cron = null;
		// Singular words/cases
		if (every.equals("minute") || every.equals("1 minute") || every.equals("1 minutes")) {
			cron = "0 * * * * *";
		} else if (every.equals("hour") || every.equals("1 hour") || every.equals("1 hours")) {
			cron = "0 0 * * * *";
		} else if (every.equals("day") || every.equals("1 day") || every.equals("1 days")) {
			cron = "0 0 0 * * *";
		} else if (every.equals("month") || every.equals("1 month") || every.equals("1 months")) {
			cron = "0 0 0 1 * *";
		} else if (every.equals("year")) {
			cron = "0 0 0 1 1 *";
		}
		// Weekdays
		else if (every.equals("sunday")) {
			cron = "0 0 0 0 0 SUN";
		} else if (every.equals("monday")) {
			cron = "0 0 0 0 0 MON";
		} else if (every.equals("tuesday")) {
			cron = "0 0 0 0 0 TUE";
		} else if (every.equals("wednesday")) {
			cron = "0 0 0 0 0 WED";
		} else if (every.equals("thursday")) {
			cron = "0 0 0 0 0 THU";
		} else if (every.equals("friday")) {
			cron = "0 0 0 0 0 FRI";
		} else if (every.equals("saturday")) {
			cron = "0 0 0 0 0 SAT";
		}
		// Other cases
		else {
			String split[] = every.split(" ");
			if (split.length != 2) {
				throw new IllegalArgumentException(expression + " isn't a valid every expression");
			}

			int amount = Integer.parseInt(split[0]);

			String precision = split[1];

			// X minutes/hours/days/weeks/months
			if (precision.equals("minutes")) {
				if (amount < 1 || amount > 59) {
					throw new IllegalArgumentException(expression + " isn't a valid every expression");
				}
				cron = "0 */" + amount + " * * * *";
			} else if (precision.equals("hours")) {
				if (amount < 1 || amount > 23) {
					throw new IllegalArgumentException(expression + " isn't a valid every expression");
				}
				cron = "0 0 */" + amount + " * * *";
			} else if (precision.equals("days")) {
				if (amount < 1 || amount > 31) {
					throw new IllegalArgumentException(expression + " isn't a valid every expression");
				}
				cron = "0 0 0 */" + amount + " * *";
			} else if (precision.equals("months")) {
				if (amount < 1 || amount > 11) {
					throw new IllegalArgumentException(expression + " isn't a valid every expression");
				}
				cron = "0 0 0 1 */" + amount + " *";
			}
		}

		if (cron == null) {
			throw new IllegalArgumentException(expression + " isn't a valid every expression");
		}

		return cron;
	}

}
