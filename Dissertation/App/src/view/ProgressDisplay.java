package src.View;

import java.util.List;
import javafx.application.Application;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import src.Controller.Matching;
import src.Model.Standard;

/**
 * 
 * This class main to display a progress bar for user.
 * 
 * @author Yi-Ming Chen
 * @version 2020-08-04
 * @reference https://reurl.cc/odem4q
 */

public class ProgressDisplay extends Application {
    private Matching matching;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.initModality((Modality.APPLICATION_MODAL));
        final Label label = new Label("Detecting:");
        final ProgressBar progressBar = new ProgressBar(0);

        final Button startButton = new Button("Start");
        final Button cancelButton = new Button("Cancel");
        final Button closeButton = new Button("Close");

        final Label statusLabel = new Label();
        statusLabel.setMinWidth(250);
        statusLabel.setTextFill(Color.BLUE);

        startButton.setDisable(true);
        progressBar.setProgress(0);
        cancelButton.setDisable(false);

        matching = new Matching();

        /**
         * progress Bar property
         */
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(matching.progressProperty());

        /**
         * text property
         */
        statusLabel.textProperty().unbind();
        statusLabel.textProperty().bind(matching.messageProperty());

        // When running tasks
        matching.addEventHandler(WorkerStateEvent.WORKER_STATE_RUNNING, new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                closeButton.setDisable(true);
                cancelButton.setDisable(false);
            }
        });
        // When finished tasks
        matching.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                closeButton.setDisable(false);
                cancelButton.setDisable(true);
                List<Standard> items = matching.getValue();
                statusLabel.textProperty().unbind();
                statusLabel.setText("Detection finished: " + items.size() + " items");
            }
        });

        // Start the Task.
        Thread th = new Thread(matching);
        th.start();

        /**
         * The cancel button
         */
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startButton.setDisable(false);
                cancelButton.setDisable(true);
                closeButton.setDisable(false);
                matching.cancel(true);
                progressBar.progressProperty().unbind();
                statusLabel.textProperty().unbind();
                progressBar.setProgress(0);
            }
        });

        /**
         * The close button
         */
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                matching.cancel(true);
                primaryStage.close();
                progressBar.progressProperty().unbind();
                statusLabel.textProperty().unbind();
                progressBar.setProgress(0);
            }
        });
        FlowPane root = new FlowPane();
        root.setPadding(new Insets(10));
        root.setHgap(10);
        root.getChildren().addAll(label, progressBar, statusLabel, cancelButton, closeButton);
        Scene scene = new Scene(root, 500, 120, Color.WHITE);
        primaryStage.setTitle("Progress Window");
        primaryStage.setScene(scene);
        primaryStage.showAndWait();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}