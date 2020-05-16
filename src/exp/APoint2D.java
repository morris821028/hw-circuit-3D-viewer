package exp;

public class APoint2D {
	final long x;
	final long y;

	APoint2D(long x, long y) {
		this.x = x;
		this.y = y;
	}

	APoint2D(APoint2D pt) {
		this(pt.x, pt.y);
	}

	APoint2D sub(APoint2D pt) {
		return new APoint2D(x - pt.x, y - pt.y);
	}
}
