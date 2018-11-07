package sample;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DragNDropPuzzleTest: Test class for DragNDropPuzzle
 * @author Joshua Seguin, Iain Davidson
 * @since November 6th 2018
 *
 */
class DragNDropPuzzleTest {
    private static Puzzle p;

    /**
     * Setting up a DragNDrop puzzle from XML file
     */
    @BeforeAll
    static void setupSamplePuzzle() {
        File f = new File("testfiles/puzzlesamp.xml");
        assertTrue(f.exists());

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = null;
        try {
            document = documentBuilder.parse(f);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        p = new DragNDropPuzzle((Element)document.getElementsByTagName("puzzle").item(0));
    }

    /**
     * Checking a provided solution to the predefined puzzle
     */
    @Test
    void checkSolutionDragNDrop() {
        List<Block> providedSolution = new ArrayList<>();
        providedSolution.add(new Block("1","for num in range(1, 21):", p));
        providedSolution.add(new Block("2","if num % 3 == 0 and num % 5 == 0:", p));
        providedSolution.add(new Block("3", "print('FizzBuzz')", p));
        providedSolution.add(new Block("4", "elif num % 3 == 0:", p));
        providedSolution.add(new Block("5", "print('Fizz')", p));
        providedSolution.add(new Block("6", "elif num % 5 == 0:", p));
        providedSolution.add(new Block("7", "print('Buzz')", p));
        providedSolution.add(new Block("8", "else:", p));
        providedSolution.add(new Block("9", "print(num)", p));

        assertTrue((boolean) p.checkSolution(providedSolution));
    }

    /**
     * Checking incorrect solution to predefined puzzle
     */
    @Test
    void checkBadSolutionDragNDrop() {
        List<Block> providedSolution = new ArrayList<>();
        providedSolution.add(new Block("1","for num in range(1, 21):", p));
        providedSolution.add(new Block("2","if num % 3 == 0 and num % 5 == 0:", p));
        providedSolution.add(new Block("6", "elif num % 5 == 0:", p));
        providedSolution.add(new Block("7", "print('Buzz')", p));
        providedSolution.add(new Block("8", "else:", p));
        providedSolution.add(new Block("9", "print(num)", p));
        providedSolution.add(new Block("3", "print('FizzBuzz')", p));
        providedSolution.add(new Block("4", "elif num % 3 == 0:", p));
        providedSolution.add(new Block("5", "print('Fizz')", p));

        assertFalse((boolean) p.checkSolution(providedSolution));
    }
}