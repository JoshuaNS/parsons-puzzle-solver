package sample;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;
import java.io.File;

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

    public Puzzle(File puzzleFile, int puzzleSetIndex){
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

        // PuzzleSet index
        this.setIndex(puzzleSetIndex);

        // Find the puzzle with this index
        Element puzzleAtIndex = null;

        NodeList indiciesNL = document.getElementsByTagName("index");
        for (int i = 0; i < indiciesNL.getLength(); i++) {
            if (Integer.parseInt(indiciesNL.item(i).getTextContent()) == this.getIndex()) {
                if (indiciesNL.item(i).getParentNode() instanceof Element){
                    puzzleAtIndex = (Element)indiciesNL.item(i).getParentNode();
                }
                else {
                    System.err.println("Puzzle parent node not an Element.");
                    // error
                }

                break;
            }
        }

        // Name
        this.setName(puzzleAtIndex.getElementsByTagName("name").item(0).getTextContent());

        // Language
        this.setLanguage(puzzleAtIndex.getElementsByTagName("language").item(0).getTextContent());

        // Puzzle Type
        String ptype = puzzleAtIndex.getElementsByTagName("format").item(0).getTextContent();
        if (ptype.equals("DnD")) {
            this.setType(PuzzleType.DnD);
        }
        else if (ptype.equals("MC")) {
            this.setType(PuzzleType.MC);
        }

        // Keep indentation setting
        this.setIndentRequired(puzzleAtIndex.getElementsByTagName("indent").item(0).getTextContent().equals("true"));

        // Description
        this.setDescription(puzzleAtIndex.getElementsByTagName("description").item(0).getTextContent());

        // Solution
        Element solutionNodes = (Element)puzzleAtIndex.getElementsByTagName("solution");
        NodeList solutionBlocks = solutionNodes.getElementsByTagName("block");
        for (int i = 0; i < solutionBlocks.getLength(); i++) {
            int index = Integer.parseInt(solutionBlocks.item(i).getAttributes().getNamedItem("id").getNodeValue());
            this.solution.add(index, solutionBlocks.item(i).getNodeValue());
        }

        // Distractors
        Element distractorNodes = (Element)puzzleAtIndex.getElementsByTagName("distractors");
        NodeList distractorBlocks = distractorNodes.getElementsByTagName("block");
        for (int i = 0; i < distractorBlocks.getLength(); i++) {
            int index = Integer.parseInt(distractorBlocks.item(i).getAttributes().getNamedItem("id").getNodeValue().replaceAll("[^0-9]", ""));
            this.distractors.add(index, distractorBlocks.item(i).getNodeValue());
        }
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