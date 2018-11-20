package sample;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;


/**
 * A UI element which displays detailed information on a given Puzzle
 * @author Travis
 */
public class PuzzleSelectPanel extends GridPane {
    /**
     * Initialize the element with the data from a puzzle
     * @param puzzle The puzzle to be displayed
     */
    public PuzzleSelectPanel(Puzzle puzzle){
        this.setStyle("-fx-background-color: aliceblue; -fx-border-color: black;");
        this.setUserData(puzzle.getIndex());
        this.setPadding(new Insets(5));

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setFillWidth(true);
        column1.setHgrow(Priority.SOMETIMES);
        ColumnConstraints column2 = new ColumnConstraints();
        this.getColumnConstraints().add(column1);
        this.getColumnConstraints().add(column2);

        Label puzzleName = new Label(puzzle.getName());
        puzzleName.setFont(new Font("System Bold",16));
        this.add(puzzleName,0,0);

        Label puzzleDescription = new Label(puzzle.getDescription());
        this.add(puzzleDescription,0,1);

        GridPane puzzleInfo = new GridPane();
        ColumnConstraints columnPuzzle1 = new ColumnConstraints();
        columnPuzzle1.setMinWidth(120);
        ColumnConstraints columnPuzzle2 = new ColumnConstraints();
        columnPuzzle2.setMinWidth(100);
        puzzleInfo.getColumnConstraints().add(columnPuzzle1);
        puzzleInfo.getColumnConstraints().add(columnPuzzle2);
        String typeText = "";
        switch(puzzle.getType()){
            case DnD:
                typeText = "Drag and Drop";
                break;
            case FiB:
                typeText = "Fill in the Blanks";
                break;
            case MC:
                typeText = "Multiple Choice";
                break;
        }
        String completedText = "Not Completed";
        if(puzzle.isCompleted()) {
            completedText = "Complete";
        }
        puzzleInfo.add(new Label("Type: " + typeText),0,0);
        puzzleInfo.add(new Label("Language: " + puzzle.getLanguage()),0,1);
        puzzleInfo.add(new Label("Attempts: " + puzzle.getNumAttempts()),1,0);
        puzzleInfo.add(new Label(completedText), 1, 1);
        puzzleInfo.setHgap(5);
        puzzleInfo.setVgap(5);
        puzzleInfo.setPadding(new Insets(5));
        GridPane.setRowSpan(puzzleInfo,2);
        this.add(puzzleInfo,1,0);
    }
}
