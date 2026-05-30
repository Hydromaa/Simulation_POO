package View;

import Model.Grid;
import Model.Sheep;
import Model.SimulationListener;
import Model.SimulationManager;
import Model.SimulationObservable;
import Model.SimulationRunner;
import Model.Wolf;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class ViewSimulation extends Application implements SimulationListener {

    public static SimulationManager manager;
    public static SimulationRunner runner;

    private static final int CANVAS_SIZE = 900;

    private Canvas canvas = new Canvas(CANVAS_SIZE, CANVAS_SIZE);

    private Label label_Turn = new Label("Tour : ");
    private Label label_Number_Turn = new Label("0");

    @Override
    public void start(Stage stage) {
        manager.addSimulationListener(this);

        VBox controls = initControls();

        HBox root = new HBox();
        root.getChildren().addAll(canvas, controls);

        Scene scene = new Scene(root, CANVAS_SIZE, CANVAS_SIZE);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Simulation");
        stage.setWidth(1280);
        stage.setHeight(900);
        stage.show();
        stage.setOnCloseRequest(event -> {
            runner.stop();
        });
        draw();
    }

    private VBox initControls() {

        VBox controls = new VBox();

        controls.setSpacing(50);
        //Padding = Espace autour du conteneur
        controls.setPadding(new Insets(30, 10, 10, 10));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        //Min, max, défaut
        Slider sliderSpeed = new Slider(100, 2000, 1000);
        sliderSpeed.valueProperty().addListener((obs, oldVal, newVal) -> {
            runner.setDelay(newVal.intValue());
        });
        sliderSpeed.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(controls, Priority.ALWAYS);

        controls.getChildren().addAll(
                initBtnBox(),
                initAddButtons(),
                initRemoveButtons(),
                initResetButton(),
                spacer,
                initTurnBox(),
                new Label("Vitesse"),
                sliderSpeed
        );

        return controls;
    }

    private HBox initBtnBox() {

        HBox btnBox = new HBox();

        Button btnStart = new Button("Start");
        Button btnStop = new Button("Stop");
        btnStart.setOnAction(e -> runner.start());
        btnStop.setOnAction(e -> runner.stop());
        //Taille max en largeur possible des deux boutons : Valeur max
        btnStart.setMaxWidth(Double.MAX_VALUE);
        btnStop.setMaxWidth(Double.MAX_VALUE);

        //Spacing = Espace entre les enfants
        btnBox.setSpacing(10);
        btnBox.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(btnStart, Priority.ALWAYS);
        HBox.setHgrow(btnStop, Priority.ALWAYS);
        btnBox.getChildren().addAll(btnStart, btnStop);

        return btnBox;
    }

    private HBox initTurnBox() {

        HBox turnBox = new HBox();

        turnBox.setSpacing(10);
        turnBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(label_Turn, Priority.ALWAYS);
        HBox.setHgrow(label_Number_Turn, Priority.ALWAYS);
        turnBox.getChildren().addAll(label_Turn, label_Number_Turn);
        label_Turn.getStyleClass().add("label-turn-text");
        label_Number_Turn.getStyleClass().add("label-turn-number");
        return turnBox;
    }

    private HBox initAddButtons() {

        HBox addBox = new HBox();

        Button btnAddSheep = new Button("Ajouter un mouton");
        Button btnAddWolf = new Button("Ajouter un loup");

        btnAddSheep.setOnAction(e -> {
            try {
                manager.addEntityAtRandom(Sheep.createDefault());
                draw();
            } catch (IllegalStateException ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Grille pleine");
                alert.setHeaderText(null);
                alert.setContentText("Impossible d'ajouter une entité, la grille est trop remplie !");
                alert.show();
            }
        });

        btnAddWolf.setOnAction(e -> {
            try {
                manager.addEntityAtRandom(Wolf.createDefault());
                draw();
            } catch (IllegalStateException ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Grille pleine");
                alert.setHeaderText(null);
                alert.setContentText("Impossible d'ajouter une entité, la grille est trop remplie !");
                alert.show();
            }
        });

        btnAddSheep.setMaxWidth(Double.MAX_VALUE);
        btnAddWolf.setMaxWidth(Double.MAX_VALUE);

        addBox.setSpacing(10);
        HBox.setHgrow(addBox, Priority.ALWAYS);
        addBox.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(btnAddSheep, Priority.ALWAYS);
        HBox.setHgrow(btnAddWolf, Priority.ALWAYS);
        addBox.getChildren().addAll(btnAddSheep, btnAddWolf);

        return addBox;
    }

    private HBox initRemoveButtons() {

        HBox removeBox = new HBox();

        Button btnRemoveSheep = new Button("Retirer un mouton");
        Button btnRemoveWolf = new Button("Retirer un loup");

        btnRemoveSheep.setOnAction(e -> {
            manager.removeRandomEntity(Sheep.class);
            draw();
        });

        btnRemoveWolf.setOnAction(e -> {
            manager.removeRandomEntity(Wolf.class);
            draw();
        });

        btnRemoveSheep.setMaxWidth(Double.MAX_VALUE);
        btnRemoveWolf.setMaxWidth(Double.MAX_VALUE);

        removeBox.setSpacing(10);
        HBox.setHgrow(removeBox, Priority.ALWAYS);
        removeBox.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(btnRemoveSheep, Priority.ALWAYS);
        HBox.setHgrow(btnRemoveWolf, Priority.ALWAYS);
        removeBox.getChildren().addAll(btnRemoveSheep, btnRemoveWolf);

        return removeBox;

    }

    private HBox initResetButton() {

        HBox resetBox = new HBox();

        Button btnReset = new Button("Reset");

        btnReset.setOnAction(e -> {
            manager.removeAllEntity();
            draw();
        });

        HBox.setHgrow(btnReset, Priority.ALWAYS);
        btnReset.setMaxWidth(Double.MAX_VALUE);

        resetBox.getChildren().add(btnReset);

        return resetBox;
    }

    private void draw() {

        Grid grid = ((SimulationObservable) manager).getGrid();
        GraphicsContext gc = canvas.getGraphicsContext2D();

        double cellSize = (double) CANVAS_SIZE / grid.getLength();

        for (int i = 0; i < grid.getLength(); i++) {
            for (int j = 0; j < grid.getWidth(); j++) {
                if (grid.getCell(i, j).isFree()) {
                    int grassLevel = grid.getCell(i, j).getGrassLevel();
                    int r = 139 + (int) ((34 - 139) * grassLevel / 100.0);
                    int g = 90 + (int) ((139 - 90) * grassLevel / 100.0);
                    int b = 43 + (int) ((34 - 43) * grassLevel / 100.0);
                    gc.setFill(Color.rgb(r, g, b));
                } else if (grid.getCell(i, j).getOccupant() instanceof Wolf) {
                    gc.setFill(Color.RED);
                } else if (grid.getCell(i, j).getOccupant() instanceof Sheep) {
                    gc.setFill(Color.BLUE);
                }
                gc.setStroke(Color.DARKGREEN);
                gc.setLineWidth(0.5);
                gc.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                gc.strokeRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }
    }

    @Override
    public void onTurnEnded(int turn) {
        Platform.runLater(() -> {
            label_Number_Turn.setText(String.valueOf(turn));
            draw();
        });
    }

    @Override
    public void onSimulationEnded() {
        System.out.println("Simulation terminée !");
    }

}
