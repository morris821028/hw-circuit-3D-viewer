package exp;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

public class BoardLoader {
	private final List<Box> mBoxDevs;
	private final List<Box> mBoxPins;

	public BoardLoader() {
		mBoxPins = new ArrayList<>();
		mBoxDevs = new ArrayList<>();

		// Create the Material
		PhongMaterial pinMaterial = new PhongMaterial();
		pinMaterial.setDiffuseColor(Color.web("#80ff0080"));
		pinMaterial.setSpecularColor(Color.INDIANRED);

		PhongMaterial pkgMaterial = new PhongMaterial();
		pkgMaterial.setDiffuseColor(Color.web("#006400ff"));
		pkgMaterial.setSpecularColor(Color.INDIANRED);

		PhongMaterial dieMaterial = new PhongMaterial();
		dieMaterial.setDiffuseColor(Color.web("#a52a2a80"));
		dieMaterial.setSpecularColor(Color.INDIANRED);

		for (int i = -2; i <= 2; i++) {
			for (int j = -2; j <= 2; j++) {
				Box pin = new Box(5, 10, 2);

				pin.setTranslateX(i * 20);
				pin.setTranslateY(j * 20);
				pin.setTranslateZ(-2 / 2 - 10);
				pin.setDrawMode(DrawMode.FILL);

				pin.setMaterial(pinMaterial);
				mBoxPins.add(pin);
			}
		}

		Box pkg = new Box(100, 100, 10);
		pkg.setTranslateZ(-10 / 2);
		pkg.setMaterial(pkgMaterial);

		Box dieA = new Box(40, 80, 6);
		dieA.setTranslateX(-40 / 2 - 5);
		dieA.setTranslateZ(-6 / 2 - 2 - 10);
		dieA.setMaterial(dieMaterial);
		Box dieB = new Box(40, 80, 6);
		dieB.setTranslateX(40 / 2 + 5);
		dieB.setTranslateZ(-6 / 2 - 2 - 10);
		dieB.setMaterial(dieMaterial);
		mBoxDevs.add(pkg);
		mBoxDevs.add(dieA);
		mBoxDevs.add(dieB);
	}

	public void load(Group sc) {
		sc.getChildren().addAll(mBoxPins);
		sc.getChildren().addAll(mBoxDevs);
	}

	public double getHeight() {
		return 100 * 20;
	}

	public double getWidth() {
		return 100 * 20;
	}

	public MeshView createOctagonView(double r, double height) {
		// @formatter:off
		float[] points = {
			50, 0, 0,
			45, 10, 0,
			55, 10, 0};

		float[] texCoords = {
			0.5f, 0.5f,
			0.0f, 1.0f,
			1.0f, 1.0f};

		int[] faces = { 0, 0, 2, 2, 1, 1, 0, 0, 1, 1, 2, 2 };
		// @formatter:on

		// Create a TriangleMesh
		TriangleMesh mesh = new TriangleMesh();
		mesh.getPoints().addAll(points);
		mesh.getTexCoords().addAll(texCoords);
		mesh.getFaces().addAll(faces);

		// Create a NeshView
		MeshView meshView = new MeshView();
		meshView.setMesh(mesh);

		return meshView;
	}
}
