package sample;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Puzzle object is the object that contains the data for a single puzzle in a puzzle set
 * Data that a puzzle contains includes the description, solutions and distractors, among other details
 * @author Joshua Seguin, Iain Davidson
 * @since November 6th 2018
 *
 */
public abstract class Puzzle {

    private String name;
    private int index;
    private String language;
    private PuzzleType type;
    private boolean indentRequired;
    private String description;
    private List<Block> solutionSet;
    private List<Block> lines;
    private List<Block> distractors;
    private int tabWidth;
    private static Pattern distractorPattern = Pattern.compile("(\\d+)X(\\d+)");

    /**
     * Puzzle constructor which takes the UML data as an input and converts it to a puzzle object
     * @param puzzleXML
     */
    public Puzzle(Element puzzleXML){
        lines = new ArrayList<>();
        distractors = new ArrayList<>();

        // Index
        try{
            this.setIndex(Integer.parseInt(puzzleXML.getElementsByTagName("index").item(0).getTextContent()));
        } catch (NullPointerException e) {
            // Error state, considered bad XML
            System.err.println("Puzzle index not specified");
            // TODO: have this throw an exception that we deal with
        }
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
        String ptype = puzzleXML.getElementsByTagName("format").item(0).getTextContent();

        switch (ptype) {
            case "DnD":
                this.setType(PuzzleType.DnD);
                break;
            case "MC":
                this.setType((PuzzleType.MC));
                break;
            case "FiB":
                this.setType((PuzzleType.FiB));
                break;
            default:
                // Error state, considered bad XML
                System.err.println("Puzzle type not specified");
                // TODO: have this throw an exception that we deal with
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
            String id = solutionBlocks.item(i).getAttributes().getNamedItem("id").getNodeValue();
            int index = Integer.parseInt(id);
            this.lines.add(index-1, new Block(id, solutionBlocks.item(i).getTextContent(), this));
        }

        // Distractors
        Element distractorNodes = (Element)puzzleXML.getElementsByTagName("distractors").item(0);
        NodeList distractorBlocks = distractorNodes.getElementsByTagName("block");
        for (int i = 0; i < distractorBlocks.getLength(); i++) {
            String id = distractorBlocks.item(i).getAttributes().getNamedItem("id").getNodeValue();

            Matcher m = distractorPattern.matcher(id);
            int associatedID;
            if (m.find()) {
                associatedID = Integer.parseInt(m.group(1));
            }
            // If the pattern doesn't match, it should be a non-distractor block, with no need for association.
            else {
                associatedID = -1;
            }

            Block b = new Block(id, distractorBlocks.item(i).getTextContent(), this);

            // Add associated between both blocks
            // associatedID of 0 means no association
            if (associatedID > 0) {
                b.addAssociatedBlock(this.lines.get(associatedID - 1));
                this.lines.get(associatedID - 1).addAssociatedBlock(b);
            }

            this.distractors.add(b);
        }

        // Assume tabWidth (in space characters) is 4 for now.
        this.setTabWidth(4);
    }

    /**
     * abstract method that defines the method of checking if a solution is correct
     * @param providedSolution
     * @return
     */
    abstract Object checkSolution(List<Block> providedSolution);

    /**
     * getChoices returns a list of the choices presented to a user in a scrambled state
     * @return
     */
    public abstract List<List<Block>> getChoices();

    public Block getBlock(String id) {
        Block b = lines.stream()
                .filter(x -> id.equals(x.getID()))
                .findAny()
                .orElse(null);
        if (b == null) {
            b = distractors.stream()
                    .filter(x -> id.equals(x.getID()))
                    .findAny()
                    .orElse(null);
        }
        return b;
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

    public List<Block> getLines() {
        return lines;
    }

    public void setLines(List<Block> lines) {
        this.lines = lines;
    }

    public List<Block> getDistractors() {
        return distractors;
    }

    public void setDistractors(List<Block> distractors) {
        this.distractors = distractors;
    }

    public int getTabWidth() {
        return tabWidth;
    }

    public void setTabWidth(int tabWidth) {
        this.tabWidth = tabWidth;
    }

    public List<Block> getSolutionSet() {
        return solutionSet;
    }

    protected void setSolutionSet(List<Block> solutionSet) {
        this.solutionSet = solutionSet;
    }
}

enum PuzzleType{
    DnD, MC, FiB;
}
