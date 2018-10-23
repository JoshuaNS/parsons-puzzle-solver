package sample;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
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
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(puzzleFile);

        // PuzzleSet settings
        // Name
        // Need to iterate through first layer of children since getElementsByTagName would return inner Puzzle names
        NodeList nodes = document.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++){
            if (nodes.item(i).getNodeName().equals("name")){
                this.name = nodes.item(i).getTextContent();
            }
        }

        // Random order setting
        String order = document.getElementsByTagName("randomOrder").item(0).getTextContent();
        this.randomOrder = order.equals("true");

        // Sequential puzzle completion setting
        String seqComp = document.getElementsByTagName("sequentialComp").item(0).getTextContent();
        this.sequentialCompletion = seqComp.equals("true");
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
