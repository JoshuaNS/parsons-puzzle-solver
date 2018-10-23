package sample;

import java.util.*;
public class Puzzle {

    private String name;
    private int index;
    private String language;
    private PuzzleType type;
    private boolean indentRequired;
    private String description;
    private ArrayList<String> solutions;
    private ArrayList<String> distractors;
    private ArrayList<String> solution; //need to decide how we store solution

    public Puzzle(String name, int index){
        this.name = name;
        this.index = index;
    }

    public void importPuzzle(){
        //used to import a puzzle from a puzzle set
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public PuzzleType getType() {
        return type;
    }

    public void setType(PuzzleType type) {
        this.type = type;
    }

    public boolean isIndentRequired() {
        return indentRequired;
    }

    public void setIndentRequired(boolean indentRequired) {
        this.indentRequired = indentRequired;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getSolutions() {
        return solutions;
    }

    public void setSolutions(ArrayList<String> solutions) {
        this.solutions = solutions;
    }

    public ArrayList<String> getDistractors() {
        return distractors;
    }

    public void setDistractors(ArrayList<String> distractors) {
        this.distractors = distractors;
    }
}

enum PuzzleType{
    DnD, MC;
}