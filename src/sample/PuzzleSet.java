package sample;

import java.util.*;
public class PuzzleSet {
    private String name;
    private boolean sequentialCompletion;
    private boolean randomOrder;
    private ArrayList<Puzzle> puzzles;

    public PuzzleSet (String name){
        this.name = name;
    }

    public void importPuzzleSet(){
        //this is where puzzle set xml file is read
        //we could have a importPuzzle in Puzzle to parse individual puzzles
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSequentialCompletion() {
        return sequentialCompletion;
    }

    public void setSequentialCompletion(boolean sequentialCompletion) {
        this.sequentialCompletion = sequentialCompletion;
    }

    public boolean isRandomOrder() {
        return randomOrder;
    }

    public void setRandomOrder(boolean randomOrder) {
        this.randomOrder = randomOrder;
    }

    public ArrayList<Puzzle> getPuzzles() {
        return puzzles;
    }

    public void setPuzzles(ArrayList<Puzzle> puzzles) {
        this.puzzles = puzzles;
    }
}
