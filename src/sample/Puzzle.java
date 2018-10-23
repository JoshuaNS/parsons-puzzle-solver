package sample;

import org.w3c.dom.Document;
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
        try{
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(puzzleFile);

            // PuzzleSet index
            this.setIndex(puzzleSetIndex);

            // Find the puzzle with this index
            Node puzzleAtIndex;

            NodeList indiciesNL = document.getElementsByTagName("index");
            for (int i = 0; i < indiciesNL.getLength(); i++) {
                if (Integer.parseInt(indiciesNL.item(i).getTextContent()) == this.getIndex()) {
                    puzzleAtIndex = indiciesNL.item(i).getParentNode();
                    break;
                }
            }

            // Name

            // Language

            // Puzzle Type

            // Keep indentation setting

            // Description

            // Solution

            // Distractors

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
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