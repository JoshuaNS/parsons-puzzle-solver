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
public class PuzzleSet {
    private String name;
    private boolean sequentialCompletion;
    private boolean randomOrder;
    private ArrayList<Puzzle> puzzles;

    public PuzzleSet (String name){
        this.name = name;
    }

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

        // Random order setting
        String order = document.getElementsByTagName("randomOrder").item(0).getTextContent();
        this.setRandomOrder(order.equals("true"));

        // Sequential puzzle completion setting
        String seqComp = document.getElementsByTagName("sequentialComp").item(0).getTextContent();
        this.setSequentialCompletion(seqComp.equals("true"));

        // Import each puzzle
        NodeList puzzleXMLNodes = document.getElementsByTagName("index");

        for (int i = 0; i < puzzleXMLNodes.getLength(); i++) {
            int index = Integer.parseInt(puzzleXMLNodes.item(i).getTextContent());
            if (puzzleXMLNodes.item(i).getParentNode() instanceof Element) {
                Element puzzleAtIndex = (Element)puzzleXMLNodes.item(i).getParentNode();
                puzzles.add(index-1, new Puzzle(puzzleAtIndex));
            }
            else {
                System.err.println("Puzzle parent node not an Element.");
            }
        }
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
