package exp;

import java.util.ArrayList;
import java.util.List;

/**
 * http://www.csie.ntnu.edu.tw/~u91029/Triangulation.html#3
 *
 */
public class PolygonTriangulation {
	final int n;
	final APoint2D[] p;
	final boolean isClockwise;

	public PolygonTriangulation(APoint2D[] pts) {
		this.n = pts.length;
		this.p = pts;

		long area = 0;
		for (int i = 0, j = n - 1; i < n; j = i++) {
			area += p[i].x * p[j].y - p[i].y * p[j].x;
		}
		this.isClockwise = area > 0;
	}

	int[] l;
	int[] r;
	Boolean[] ear;

	private void initCircular() {
		l = new int[n];
		r = new int[n];

		for (int i = 0; i < n; i++) {
			l[i] = i - 1;
			r[i] = i + 1;
		}
		l[0] = n - 1;
		r[n - 1] = 0;
	}

	private double cross(APoint2D v0, APoint2D v1) {
		return (double) v0.x * v1.y - (double) v0.y * v1.x;
	}

	private double cross(APoint2D p0, APoint2D p1, APoint2D p2) {
		return (double) (p1.x - p0.x) * (p2.y - p0.y) - (double) (p1.y - p0.y) * (p2.x - p0.x);
	}

	private boolean pointInTriangle(APoint2D p, APoint2D p0, APoint2D p1, APoint2D p2) {
		APoint2D v0 = p0.sub(p);
		APoint2D v1 = p1.sub(p);
		APoint2D v2 = p2.sub(p);

		double c0 = cross(v0, v1);
		double c1 = cross(v1, v2);
		double c2 = cross(v2, v0);
		return (c0 > 0 && c1 > 0 && c2 > 0) || (c0 < 0 && c1 < 0 && c2 < 0);
	}

	private int sign(double v) {
		if (Math.abs(v) > 0)
			return v > 0 ? 1 : -1;
		return 0;
	}

	private boolean intersection(APoint2D a, APoint2D b, APoint2D c, APoint2D d) { // seg(a, b) and seg(c, d)
	    return sign(cross(a, c, b)) * sign(cross(a, b, d)) > 0
	    && sign(cross(c, a, d)) * sign(cross(c, d, b)) > 0;
	}

	private boolean isEar(int x) {
		double area = cross(p[r[x]], p[x], p[l[x]]);
		boolean isClockwise = area > 0;
		System.out.printf("isEar %d %b %f %s %s %s\n", x, isClockwise, cross(p[l[x]], p[x], p[r[x]]), p[l[x]], p[x], p[r[x]]);
		if (isClockwise != this.isClockwise && area != 0)
			return false;
		for (int i = r[r[x]]; i != l[x]; i = r[i]) {
			if (pointInTriangle(p[i], p[x], p[l[x]], p[r[x]])) {
				System.out.printf("inside %d %s\n", i, p[i]);
				return false;
			}
		}

		for (int i = r[r[x]]; i != l[x]; i = r[i]) {
			if (intersection(p[i], p[r[i]], p[r[x]], p[x]))
				return false;
			if (intersection(p[i], p[r[i]], p[l[x]], p[x]))
				return false;
			if (intersection(p[i], p[r[i]], p[l[x]], p[r[x]]))
				return false;
		}
		return true;
	}

	public List<ATriple<Integer, Integer, Integer>> triangulation() {
		initCircular();
		System.out.printf("isClockwise %b\n", isClockwise);

		List<ATriple<Integer, Integer, Integer>> ret = new ArrayList<>(n - 2);
		ear = new Boolean[n];

		int i = 0;
		for (int k = 0; k < n - 3; k++) {
			while (true) {
				if (ear[i] == null)
					ear[i] = isEar(i);
				if (ear[i])
					break;
				i = r[i];
			}

			ret.add(new ATriple<>(l[i], i, r[i]));
			System.out.printf("%d - %d - %d\n", l[i], i, r[i]);

			ear[r[i]] = ear[l[i]] = null;
			r[l[i]] = r[i];
			l[r[i]] = l[i];
			i = r[i];
		}
		ret.add(new ATriple<>(l[i], i, r[i]));
		assert ret.size() == n - 2;
		return ret;
	}
}
