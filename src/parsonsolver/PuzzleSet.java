package parsonsolver;

import org.omg.CORBA.DynAnyPackage.Invalid;
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
    public PuzzleSet (File puzzleFile) throws InvalidInputFileException{
        puzzles = new ArrayList<>();
        importPuzzleSet(puzzleFile);
    }

    /**
     * Sets the values of a puzzleSet using the data from an XML file
     * @param puzzleFile
     */
    public void importPuzzleSet(File puzzleFile) throws InvalidInputFileException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        Document document = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(puzzleFile);
        } catch (SAXException e) {
            throw new InvalidInputFileException();
        } catch (IOException e) {
            throw new InvalidInputFileException();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        // PuzzleSet settings

        // Name
        NodeList setNameNL = document.getElementsByTagName("setName");
        if (setNameNL.getLength() > 0) {
            this.setName(setNameNL.item(0).getTextContent());
        }
        else {
            this.setName("Puzzle Set");
        }


        // Random order setting
        NodeList orderNL = document.getElementsByTagName("randomOrder");
        if (orderNL.getLength() > 0) {
            this.setRandomOrder(orderNL.item(0).getTextContent().equals("true"));
        }
        else {
            this.setRandomOrder(false);
        }


        // Sequential puzzle completion setting
        NodeList seqNL = document.getElementsByTagName("sequentialComp");
        if (seqNL.getLength() > 0) {
            String seqComp = seqNL.item(0).getTextContent();
            this.setSequentialCompletion(seqComp.equals("true"));
        }
        else {
            //set to false if not specified
            this.setSequentialCompletion(false);
        }

        // Import each puzzle
        NodeList puzzleXMLNodes = document.getElementsByTagName("index");
        if (puzzleXMLNodes.getLength() > 0) {
            for (int i = 0; i < puzzleXMLNodes.getLength(); i++) {
                int index = Integer.parseInt(puzzleXMLNodes.item(i).getTextContent());
                //TODO Issue if index is not unique

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
                    default:
                        throw new InvalidInputFileException();
                }
            }
        }
        else {
            throw new InvalidInputFileException();
        }
    }

    public ArrayList<String> exportResults(){
        ArrayList<String> results = new ArrayList<>();
        results.add("Puzzle Set '" +name +"' results:\n");
        for (int i = 0; i<getPuzzles().size(); i++){
            Puzzle puzz = getPuzzle(i+1);
            results.add("\n\t" +puzz.getName());
            if (puzz.isCompleted()) {
                results.add("\tPuzzle Status: Completed\n");
            } else {
                results.add("\tPuzzle Status: Incomplete\n");
            }
            results.add("\tNumber of Attempts: " +puzz.getNumAttempts() +"\n");
            double time = (double)puzz.getTimeElapsed()/1000;
            results.add("\tElapsed Time: " +time +" seconds\n");
        }
        return results;
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