package sample;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * A PuzzleSet Object is an object that contains multiple puzzle objects
 * When the UI is instantiated a puzzleSet is opened by reading an XML file
 *
 * @author Joshua Seguin, Iain Davidson
 * @since November 6th 2018
 *
 */
public class PuzzleSet {
    private String name;
    private boolean sequentialCompletion;
    private boolean randomOrder;
    private ArrayList<Puzzle> puzzles;

    /**
     * Puzzle set constructor that is created by passing in the name of the set
     * @param name
     */
    public PuzzleSet (String name){
        this.name = name;
        puzzles = new ArrayList<>();
    }

    /**
     * Default Constructor
     */
    public PuzzleSet (){
        puzzles = new ArrayList<>();
    }

    /**
     * Constructor that imports an XML file using importPuzzleSet
     * @param puzzleFile
     */
    public PuzzleSet (File puzzleFile){
        puzzles = new ArrayList<>();
        importPuzzleSet(puzzleFile);
    }

    /**
     * Sets the values of a puzzleSet using the data from an XML file
     * @param puzzleFile
     */
    public void importPuzzleSet(File puzzleFile){
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = null;
        try {
            document = documentBuilder.parse(puzzleFile);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // PuzzleSet settings
        // Name
        // Need to iterate through first layer of children since getElementsByTagName would return inner Puzzle names
        NodeList nodes = document.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++){
            if (nodes.item(i).getNodeName().equals("name")){
                this.setName(nodes.item(i).getTextContent());
            }
        }
        //name

        try{
            this.setName(document.getElementsByTagName("setName").item(0).getTextContent());
        } catch (NullPointerException e) {
            //if no name specified, call it Puzzle Set
            this.setName("Puzzle Set");
        }

        // Random order setting
        try{
            String order = document.getElementsByTagName("randomOrder").item(0).getTextContent();
            this.setRandomOrder(order.equals("true"));
        } catch (NullPointerException e){
            //set to false if not specified
            this.setRandomOrder(false);
        }

        // Sequential puzzle completion setting
        try{
            String seqComp = document.getElementsByTagName("sequentialComp").item(0).getTextContent();
            this.setSequentialCompletion(seqComp.equals("true"));
        } catch (NullPointerException e){
            //set to false if not specified
            this.setSequentialCompletion(false);
        }

        // Import each puzzle
        NodeList puzzleXMLNodes = document.getElementsByTagName("index");

        for (int i = 0; i < puzzleXMLNodes.getLength(); i++) {
            int index = Integer.parseInt(puzzleXMLNodes.item(i).getTextContent());
            //TODO Issue if index is not unique
            if (puzzleXMLNodes.item(i).getParentNode() instanceof Element) {
                Element puzzleAtIndex = (Element)puzzleXMLNodes.item(i).getParentNode();
                switch (puzzleAtIndex.getElementsByTagName("format").item(0).getTextContent()) {
                    case "DnD":
                        puzzles.add(index-1, new DragNDropPuzzle(puzzleAtIndex));
                        break;
                    case "MC":
                        puzzles.add(index-1, new MultipleChoicePuzzle(puzzleAtIndex));
                        break;
                    case "FiB":
                        puzzles.add(index-1, new FillBlanksPuzzle(puzzleAtIndex));
                        break;
                }

            }
            else {
                System.err.println("Puzzle parent node not an Element.");
            }
        }
    }

    /**
     * Returns the puzzle with the given index
     * @param index
     * @return
     */
    //todo: no check for puzzles with the same index
    public Puzzle getPuzzle(int index) {
        return this.getPuzzles().get(index-1);
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
