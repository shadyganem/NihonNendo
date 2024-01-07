import java.time.LocalDate;

public class MyCandle {

	private final double open, high, low, close;
	private final LocalDate localDate;
	
	public MyCandle(double o, double h, double l, double c, LocalDate ld) {
		open = o;
		high = h;
		low = l;
		close = c;
		this.localDate = ld;
	}

	public double getOpen() {
		return open;
	}

	public double getHigh() {
		return high;
	}

	public double getLow() {
		return low;
	}

	public double getClose() {
		return close;
	}

	public LocalDate getLocalDate() {
		return localDate;
	}
}
