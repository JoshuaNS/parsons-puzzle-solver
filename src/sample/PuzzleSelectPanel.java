package sample;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

public class PuzzleSelectPanel extends GridPane {
    public PuzzleSelectPanel(Puzzle p){
        this.setStyle("-fx-background-color: aliceblue; -fx-border-color: black;");
        this.setUserData(p.getIndex());
        this.setPadding(new Insets(5));

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setFillWidth(true);
        column1.setHgrow(Priority.SOMETIMES);
        this.getColumnConstraints().add(column1);
        this.getColumnConstraints().add(new ColumnConstraints());

        Label puzzleName = new Label(p.getName());
        puzzleName.setFont(new Font("System Bold",16));
        this.add(puzzleName,0,0);

        Label puzzleDescription = new Label(p.getDescription());
        this.add(puzzleDescription,0,1);

        GridPane puzzleInfo = new GridPane();
        puzzleInfo.add(new Label("Type: " + p.getType()),0,0);
        puzzleInfo.add(new Label("Language: " + p.getLanguage()),0,1);
        puzzleInfo.add(new Label("Attempts: " + p.getNumAttempts()),1,0);
        puzzleInfo.add(new Label("Completed: " + p.isCompleted()),1,1);
        puzzleInfo.setHgap(5);
        puzzleInfo.setVgap(5);
        puzzleInfo.setPadding(new Insets(5));
        GridPane.setRowSpan(puzzleInfo,2);
        this.add(puzzleInfo,1,0);
    }
}
