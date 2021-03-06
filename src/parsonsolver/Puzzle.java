package parsonsolver;

import Util.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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

    //statistics parameters
    private long puzzleStartTime = -1;
    private long timeElapsed = 0; //time elapsed in milliseconds
    private boolean isCompleted = false;
    private int numAttempts = 0;

    /**
     * Puzzle constructor for creating a new puzzle using the creator
     * @param name
     */
    public Puzzle(String name, int index, PuzzleType type){
        this.name = name;
        this.index = index;
        this.type = type;
        this.setTabWidth(4);
    }

    /**
     * Puzzle constructor which takes the UML data as an input and converts it to a puzzle object
     * @param puzzleXML
     */
    public Puzzle(Element puzzleXML) throws InvalidInputFileException {
        lines = new ArrayList<>();
        distractors = new ArrayList<>();

        // Index
        NodeList indexNL = puzzleXML.getElementsByTagName("index");
        if (indexNL.getLength() > 0) {
            this.setIndex(Integer.parseInt(indexNL.item(0).getTextContent()));
        }
        else {
            throw new InvalidInputFileException();
        }

        // Name
        NodeList nameNL = puzzleXML.getElementsByTagName("name");
        if (nameNL.getLength() > 0) {
            this.setName(XML.unescapeXMLAttribute(nameNL.item(0).getTextContent()));
        }
        else {
            //if the name is not provided, just use the index
            this.setName("Puzzle "+Integer.toString(this.getIndex()));
        }

        // Language
        NodeList languageNL = puzzleXML.getElementsByTagName("lang");
        if (languageNL.getLength() > 0) {
            this.setLanguage(XML.unescapeXMLAttribute(languageNL.item(0).getTextContent()));
        }
        else {
            this.setLanguage("None Specified");
        }

        // Puzzle Type
        NodeList typeNL = puzzleXML.getElementsByTagName("format");
        if (typeNL.getLength() > 0) {
            String ptype = typeNL.item(0).getTextContent();
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
                    throw new InvalidInputFileException();
            }
        }
        else {
            throw new InvalidInputFileException();
        }

        // Keep indentation setting
        NodeList indentNL = puzzleXML.getElementsByTagName("indent");
        if (indentNL.getLength() > 0) {
            this.setIndentRequired(indentNL.item(0).getTextContent().equals("true"));
        }
        else {
            this.setIndentRequired(false);
        }

        // Description
        NodeList descNL = puzzleXML.getElementsByTagName("description");
        if (descNL.getLength() > 0) {
            this.setDescription(XML.unescapeXMLAttribute(descNL.item(0).getTextContent()));
        }
        else {
            //if no description provided, initialize but keep blank
            this.setDescription("");
        }

        // Assume tabWidth (in space characters) is 4 for now.
        this.setTabWidth(4);

        // Solution
        NodeList solnNL = puzzleXML.getElementsByTagName("solution");
        if (solnNL.getLength() > 0) {
            Element solutionNodes = (Element)solnNL.item(0);
            NodeList solutionBlocks = solutionNodes.getElementsByTagName("block");

            for (int i = 0; i < solutionBlocks.getLength(); i++) {
                String id = solutionBlocks.item(i).getAttributes().getNamedItem("id").getNodeValue();
                int index = Integer.parseInt(id);
                this.lines.add(index-1, new Block(id, XML.unescapeXMLAttribute(solutionBlocks.item(i).getTextContent()), this));
            }
        }
        else {
            throw new InvalidInputFileException();
        }


        // Distractors
        NodeList distNL = puzzleXML.getElementsByTagName("distractors");
        if (distNL.getLength() > 0) {
            Element distractorNodes = (Element)distNL.item(0);
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

                Block b = new Block(id, XML.unescapeXMLAttribute(distractorBlocks.item(i).getTextContent()), this);

                // Add associated between both blocks
                // associatedID of 0 means no association
                if (associatedID > 0) {
                    b.addAssociatedBlock(this.lines.get(associatedID - 1));
                    this.lines.get(associatedID - 1).addAssociatedBlock(b);
                }

                this.distractors.add(b);
            }
        }
        // There may not be any distractors, acceptable XML if not included
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

    /**
     * Invoke this method when a puzzle starts execution to set the start time
     */
    public void startPuzzle(){
        if(!isCompleted){   //we only count time if they are still working on the puzzle
            puzzleStartTime = System.currentTimeMillis();
        } else {
            puzzleStartTime = -1;//if it's been completed, then we dont add any time
        }
    }

    /**
     * Invoke this method when a puzzle is exited to stop the time
     */
    public void endPuzzle(){
        if (puzzleStartTime >= 0) {
            timeElapsed = timeElapsed + (System.currentTimeMillis() - puzzleStartTime);
        }
    }

    /**
     * Increment the number of attempts taken
     */
    public void incAttempts(){
        this.setNumAttempts(this.getNumAttempts() + 1);
    }

    /**
     * Reset the number of attempts taken
     */
    public void resetAttempts(){
        this.setNumAttempts(0);
    }

    /**
     * Returns true if the puzzle is solvable, i.e. has enough information to allow a user to solve it
     * This is so that we don't allow a puzzle to be opened in the solver if it is not solvable
     * @return isSolvable
     */
    public boolean isSolvable(){
        return (getLines() != null && getSolutionSet() != null && getType() != null);
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

    /**
     * Gets a randomized list of all solution lines and distractors
     * @return A shuffled List containing all Blocks in the puzzle
     */
    public List<Block> getBlocksSet() {
        List<Block> choices = new ArrayList<>();
        choices.addAll(this.getLines());
        choices.addAll(this.getDistractors());
        Collections.shuffle(choices);
        return choices;
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(long timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public int getNumAttempts() {
        return numAttempts;
    }

    public void setNumAttempts(int numAttempts) {
        this.numAttempts = numAttempts;
    }

    protected abstract List<Node> exportExtraToXML(Document document) throws UnformedPuzzleException;

    public Node exportToXML(Document document) throws UnformedPuzzleException{
        Element root = document.createElement("puzzle");
        // Export common puzzle stuff
        Element nameN = document.createElement("name");
        Element indexN = document.createElement("index");
        Element langN = document.createElement("lang");
        Element formatN = document.createElement("format");
        Element indentN = document.createElement("indent");
        Element descriptionN = document.createElement("description");

        try {
            nameN.appendChild(document.createTextNode(getName().toString()));
            root.appendChild(nameN);
        } catch (NullPointerException e) {
            nameN.appendChild(document.createTextNode("No Name"));
        }

        if (getIndex() > 0) {
            indexN.appendChild(document.createTextNode(String.valueOf(getIndex())));
            root.appendChild(indexN);
        } else {
            throw new UnformedPuzzleException("No index");
        }

        try {
            langN.appendChild(document.createTextNode(getLanguage().toString()));
            root.appendChild(langN);
        } catch (NullPointerException e) {
            // Lang is not necessary
        }

        try {
            formatN.appendChild(document.createTextNode(getType().name()));
            root.appendChild(formatN);
        } catch (NullPointerException e) {
            throw new UnformedPuzzleException("No format");
        }

        try {
            indentN.appendChild(document.createTextNode(String.valueOf(isIndentRequired())));
            root.appendChild(indentN);
        } catch (NullPointerException e) {
            // Indent not necessary;
        }

        try {
            descriptionN.appendChild(document.createTextNode(getDescription().toString()));
            root.appendChild(descriptionN);
        } catch (NullPointerException e) {
            descriptionN.appendChild(document.createTextNode(""));
        }

        try {
            // Solution
            Node solutionN = document.createElement("solution");
            for (Block b : getLines()) {
                Element blockN = document.createElement("block");
                blockN.setAttribute("id", b.getID());
                //String escapedLines = StringEscapeUtils.escapeXml10(b.getLinesTabbed()); // Is this necessary now?
                blockN.appendChild(document.createTextNode(b.getLinesTabbed().toString()));
                solutionN.appendChild(blockN);
            }
            root.appendChild(solutionN);
        } catch (NullPointerException e) {
            throw new UnformedPuzzleException("No solution");
        }

        try {
            // Distractors
            Node distractorsN = document.createElement("distractors");

            for (Block b : getDistractors()) {
                Element blockN = document.createElement("block");
                blockN.setAttribute("id", b.getID());
                blockN.appendChild(document.createTextNode(b.getLinesTabbed().toString()));
                distractorsN.appendChild(blockN);
            }
            root.appendChild(distractorsN);
        } catch (NullPointerException e) {
            // Distractors not necessary
        }

        // Append the rest
        List<Node> puzzleSpecificXML = this.exportExtraToXML(document);

        if (puzzleSpecificXML != null) {
            for (Node n : puzzleSpecificXML) {
                root.appendChild(n);
            }
        }
        return root;
    }

}

enum PuzzleType {
    DnD("Drag and Drop"), MC("Multiple Choice"), FiB("Fill in the Blanks");

    private String text;

    PuzzleType(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
