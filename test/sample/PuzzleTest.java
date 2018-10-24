package sample;

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

public class PuzzleTest {

    @Test
    void importSamplePuzzle1() {
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

        Puzzle p = new DragNDropPuzzle((Element)document.getElementsByTagName("puzzle").item(0));

        List<String> expectedSolution = new ArrayList<>();
        expectedSolution.add("for num in range(1, 21):");
        expectedSolution.add("if num % 3 == 0 and num % 5 == 0:");
        expectedSolution.add("print('FizzBuzz')");
        expectedSolution.add("elif num % 3 == 0:");
        expectedSolution.add("print('Fizz')");
        expectedSolution.add("elif num % 5 == 0:");
        expectedSolution.add("print('Buzz')");
        expectedSolution.add("else:");
        expectedSolution.add("print(num)");

        List<String> expectedDistractors = new ArrayList<>();
        expectedDistractors.add("for num in range(0, 21):");
        expectedDistractors.add("if num % 3 == 0:");
        expectedDistractors.add("elif num % 5 == 0:");
        expectedDistractors.add("elif num % 3 == 0 and num % 5 == 0");

        assertEquals(p.getIndex(), 1);
        assertEquals(p.getDescription(), "Output the numbers 1-20, print “fizz” if the number is divisible by 3, “buzz” if it is divisible by 5, and “fizzbuzz” if it is divisible by both");
        assertEquals(p.getName(),"Lab 1 - Puzzle 1");
        assertEquals(p.getLanguage(), "Python");
        assertEquals(p.getType(), PuzzleType.DnD);
        assertEquals(p.isIndentRequired(), false);

        assertNotNull(p.getSolutions());
        assertLinesMatch(expectedSolution, p.getSolutions());
        assertNotNull(p.getDistractors());
        assertLinesMatch(expectedDistractors, p.getDistractors());
    }
    @Test
    //uses an XML with no types, no indent, no name, no language, no description, and ensures it functions as desired
    void importSamplePuzzleMissingParams() {
        File f = new File("testfiles/puzzlesamperror1.xml");
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

        Puzzle p = new Puzzle((Element)document.getElementsByTagName("puzzle").item(0));
        assertEquals(p.getType(), PuzzleType.DnD);
        assertEquals(p.isIndentRequired(), false);
        assertEquals(p.getName(), "Puzzle 1");
        assertEquals(p.getLanguage(), "None Specified");
        assertEquals(p.getDescription(), "");
        Puzzle p2 = new Puzzle((Element)document.getElementsByTagName("puzzle").item(1));
        assertEquals(p2.getType(), PuzzleType.MC);
        assertEquals(p2.isIndentRequired(), false);
        assertEquals(p2.getName(), "Puzzle 2");
        assertEquals(p2.getLanguage(), "None Specified");
        assertEquals(p2.getDescription(), "");
    }
}