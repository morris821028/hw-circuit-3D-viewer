package exp;

import java.util.List;

import javafx.scene.shape.TriangleMesh;

public class AExtrudedShape extends TriangleMesh {
	AExtrudedShape(APoint2D[] p, double height) {
		initMesh(p, height);
	}

	void initMesh(APoint2D[] p, double height) {
		// @formatter:off
		float[] texCoords = { 1, 1, // idx t0
				1, 0, // idx t1
				0, 1, // idx t2
				0, 0 // idx t3
		};

		/**
		 * <tt>
		 * points:
		 * 1      3
		 *  -------   texture:
		 *  |\    |  1,1    1,0
		 *  | \   |    -------
		 *  |  \  |    |     |
		 *  |   \ |    |     |
		 *  |    \|    -------
		 *  -------  0,1    0,0
		 * 0      2
		 *
		 * texture[3] 0,0 maps to vertex 2
		 * texture[2] 0,1 maps to vertex 0
		 * texture[0] 1,1 maps to vertex 1
		 * texture[1] 1,0 maps to vertex 3
		 * 
		 * Two triangles define rectangular faces:
		 * p0, t0, p1, t1, p2, t2 // First triangle of a textured rectangle 
		 * p0, t0, p2, t2, p3, t3 // Second triangle of a textured rectangle
		 * </tt>
		 */
//		int[] faces = {
//	            2, 2, 1, 1, 0, 0,
//	            2, 2, 3, 3, 1, 1
//	        };
		// @formatter:on

		this.getTexCoords().setAll(texCoords);

		int n = p.length;
		List<ATriple<Integer, Integer, Integer>> indexList = new PolygonTriangulation(p).triangulation();
		float[] points = new float[(2 * n) * 3];
		for (int i = 0; i < n; i++) {
			// background
			points[i * 3 + 0] = p[i].x;
			points[i * 3 + 1] = p[i].y;
			points[i * 3 + 2] = 0;
			// foreground
			points[i * 3 + 0 + n * 3] = p[i].x;
			points[i * 3 + 1 + n * 3] = p[i].y;
			points[i * 3 + 2 + n * 3] = (float) height;
		}

		this.getPoints().setAll(points);

		int[] faces = new int[((n - 2) * 2 + n * 2) * 6 * 2];
		int k = 0;
		for (int i = 0; i < n - 2; i++) {
			int a = indexList.get(i).first;
			int b = indexList.get(i).second;
			int c = indexList.get(i).third;

			faces[k++] = a;
			faces[k++] = 0;
			faces[k++] = b;
			faces[k++] = 1;
			faces[k++] = c;
			faces[k++] = 2;

			faces[k++] = c;
			faces[k++] = 0;
			faces[k++] = b;
			faces[k++] = 1;
			faces[k++] = a;
			faces[k++] = 2;

			faces[k++] = a + n;
			faces[k++] = 2;
			faces[k++] = b + n;
			faces[k++] = 1;
			faces[k++] = c + n;
			faces[k++] = 0;

			faces[k++] = c + n;
			faces[k++] = 2;
			faces[k++] = b + n;
			faces[k++] = 1;
			faces[k++] = a + n;
			faces[k++] = 0;
		}

		for (int i = 0; i < n; i++) {
			int a = i;
			int b = (i + 1) % n;
			int c = a + n;
			int d = b + n;

			faces[k++] = a;
			faces[k++] = 2;
			faces[k++] = b;
			faces[k++] = 1;
			faces[k++] = c;
			faces[k++] = 0;

			faces[k++] = c;
			faces[k++] = 2;
			faces[k++] = b;
			faces[k++] = 1;
			faces[k++] = a;
			faces[k++] = 0;

			faces[k++] = c;
			faces[k++] = 2;
			faces[k++] = b;
			faces[k++] = 1;
			faces[k++] = d;
			faces[k++] = 0;

			faces[k++] = d;
			faces[k++] = 2;
			faces[k++] = b;
			faces[k++] = 1;
			faces[k++] = c;
			faces[k++] = 0;
		}

		this.getFaces().setAll(faces);
	}
}
