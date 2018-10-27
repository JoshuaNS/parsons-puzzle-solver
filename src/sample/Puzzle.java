package sample;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.*;

public abstract class Puzzle {

    private String name;
    private int index;
    private String language;
    private PuzzleType type;
    private boolean indentRequired;
    private String description;
    private ArrayList<String> solutions;
    private ArrayList<String> distractors;
    private ArrayList<String> falseAnswers;

    public Puzzle(Element puzzleXML){
        solutions = new ArrayList<>();
        distractors = new ArrayList<>();

        // Index
        this.setIndex(Integer.parseInt(puzzleXML.getElementsByTagName("index").item(0).getTextContent()));
        // Name
        try{
            this.setName(puzzleXML.getElementsByTagName("name").item(0).getTextContent());
        } catch (NullPointerException e){
            //if the name is not provided, just use the index
            this.setName("Puzzle "+Integer.toString(this.getIndex()));
        }

        // Language
        try{
            this.setLanguage(puzzleXML.getElementsByTagName("lang").item(0).getTextContent());
        } catch (NullPointerException e){
            //default string if no language is specified
            this.setLanguage("None Specified");
        }

        // Puzzle Type
        try{
            String ptype = puzzleXML.getElementsByTagName("format").item(0).getTextContent();
            if (ptype.equals("DnD")) {
                this.setType(PuzzleType.DnD);
            }
            else if (ptype.equals("MC")) {
                this.setType(PuzzleType.MC);
            }
        } catch (NullPointerException e) {
            //check for falseAnswers parameter to determine if its MC
            if (this.getFalseAnswers() != null){
                this.setType(PuzzleType.MC);
            } else{
                this.setType(PuzzleType.DnD);
            }
        }

        // Keep indentation setting
        try{
            this.setIndentRequired(puzzleXML.getElementsByTagName("indent").item(0).getTextContent().equals("true"));
        } catch (NullPointerException e){
            this.setIndentRequired(false);
        }

        // Description
        try{
            this.setDescription(puzzleXML.getElementsByTagName("description").item(0).getTextContent());
        } catch (NullPointerException e){
            //if no description provided, initialize but keep blank
            this.setDescription("");
        }

        // Solution
        Element solutionNodes = (Element)puzzleXML.getElementsByTagName("solution").item(0);
        NodeList solutionBlocks = solutionNodes.getElementsByTagName("block");
        for (int i = 0; i < solutionBlocks.getLength(); i++) {
            int index = Integer.parseInt(solutionBlocks.item(i).getAttributes().getNamedItem("id").getNodeValue());
            this.solutions.add(index-1, solutionBlocks.item(i).getTextContent());
        }

        // Distractors
        Element distractorNodes = (Element)puzzleXML.getElementsByTagName("distractors").item(0);
        NodeList distractorBlocks = distractorNodes.getElementsByTagName("block");
        for (int i = 0; i < distractorBlocks.getLength(); i++) {
            int index = Integer.parseInt(distractorBlocks.item(i).getAttributes().getNamedItem("id").getNodeValue().replaceAll("[^0-9]", ""));
            this.distractors.add(index-1, distractorBlocks.item(i).getTextContent());
        }
    }

    abstract Object checkSolution(Object providedSolution);

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

    public ArrayList<String> getFalseAnswers() {
        return falseAnswers;
    }

    public void setFalseAnswers(ArrayList<String> falseAnswers) {
        this.falseAnswers = falseAnswers;
    }

}

enum PuzzleType{
    DnD, MC;
}