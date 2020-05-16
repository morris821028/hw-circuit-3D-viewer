package exp;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class SampleApp extends Application {
	private final Xform world = new Xform();
	private Scene scene;
	private BoardLoader board;
	private Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
	private Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
	private Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);
	final double cameraDistance = 200;
	private Translate trans = new Translate(0, 0, -cameraDistance);
	final Group axisGroup = new Group();

	public SubScene createContent() throws Exception {
		// board/ic/package
		board = new BoardLoader();

		// Create and position camera
		PerspectiveCamera camera = new PerspectiveCamera(true);
		camera.getTransforms().addAll(rotateX, rotateY, trans);
		camera.setNearClip(0.01);
		camera.setFarClip(5000);

		// Build the Scene Graph
		world.getChildren().add(camera);
//		buildAxes();
		board.load(world);

		// Use a SubScene
		SubScene subScene = new SubScene(world, 800, 600, true, SceneAntialiasing.BALANCED);
		subScene.setFill(Color.TRANSPARENT);
		subScene.setCamera(camera);

		return subScene;
	}

	private void buildAxes() {
		System.out.println("buildAxes()");
		final PhongMaterial redMaterial = new PhongMaterial();
		redMaterial.setDiffuseColor(Color.DARKRED);
		redMaterial.setSpecularColor(Color.RED);

		final PhongMaterial greenMaterial = new PhongMaterial();
		greenMaterial.setDiffuseColor(Color.DARKGREEN);
		greenMaterial.setSpecularColor(Color.GREEN);

		final PhongMaterial blueMaterial = new PhongMaterial();
		blueMaterial.setDiffuseColor(Color.DARKBLUE);
		blueMaterial.setSpecularColor(Color.BLUE);

		final Box xAxis = new Box(240.0, 1, 1);
		final Box yAxis = new Box(1, 240.0, 1);
		final Box zAxis = new Box(1, 1, 240.0);

		xAxis.setMaterial(redMaterial);
		yAxis.setMaterial(greenMaterial);
		zAxis.setMaterial(blueMaterial);

		axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
		world.getChildren().addAll(axisGroup);
	}

	private double mousePosX, mousePosY = 0;

	private void handleMouseEvents() {
		scene.setOnMousePressed((MouseEvent me) -> {
			mousePosX = me.getSceneX();
			mousePosY = me.getSceneY();
		});

		scene.setOnMouseDragged((MouseEvent me) -> {
			double dx = (mousePosX - me.getSceneX());
			double dy = (mousePosY - me.getSceneY());
			if (me.isPrimaryButtonDown()) {
				rotateX.setAngle(rotateX.getAngle() - (dy / 1.0) * (Math.PI / 180));
				rotateY.setAngle(rotateY.getAngle() - (dx / 1.0) * (Math.PI / 180));
			}
			mousePosX = me.getSceneX();
			mousePosY = me.getSceneY();
		});

		scene.setOnScroll((ScrollEvent e) -> {
			double wheelY = e.getDeltaY() * 0.1;
			trans.setZ(trans.getZ() + wheelY);
		});

		scene.setOnKeyPressed((KeyEvent e) -> {
			if (e.getCode() == KeyCode.W) {
				trans.setZ(trans.getZ() + 10);
			} else if (e.getCode() == KeyCode.A) {
				trans.setX(trans.getX() - 10);
			} else if (e.getCode() == KeyCode.S) {
				trans.setZ(trans.getZ() - 10);
			} else if (e.getCode() == KeyCode.D) {
				trans.setX(trans.getX() + 10);
			} else if (e.getCode() == KeyCode.Q) {
				trans.setY(trans.getY() - 10);
			} else if (e.getCode() == KeyCode.E) {
				trans.setY(trans.getY() + 10);
			} else if (e.getCode() == KeyCode.R) {
				resetView();
			}
		});
	}

	void resetView() {
		trans.setX(0);
		trans.setX(0);
		trans.setZ(-cameraDistance);

		rotateX.setAngle(0);
		rotateY.setAngle(0);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setResizable(false);

		BorderPane sRoot = new BorderPane(createContent());
		ToolBar toolbar = new ToolBar();
		toolbar.getItems().addAll(new Button("Selected"), new Button("Connectivity"), new Separator());
		sRoot.setTop(toolbar);

		scene = new Scene(sRoot);

		handleMouseEvents();
		primaryStage.setScene(scene);
		primaryStage.setTitle("3D Viewer");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}