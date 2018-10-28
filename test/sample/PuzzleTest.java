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
        assertEquals("Output the numbers 1-20, print “fizz” if the number is divisible by 3, “buzz” if it is divisible by 5, and “fizzbuzz” if it is divisible by both",
                p.getDescription());
        assertEquals("Lab 1 - Puzzle 1", p.getName());
        assertEquals("Python", p.getLanguage());
        assertEquals(PuzzleType.DnD, p.getType());
        assertEquals(false, p.isIndentRequired());

        assertNotNull(p.getSolutions());
        assertLinesMatch(expectedSolution, p.getSolutions());
        assertNotNull(p.getDistractors());
        assertLinesMatch(expectedDistractors, p.getDistractors());
    }
    @Test
    //uses an XML with no indent, no name, no language, no description, and ensures it functions as desired
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

        Puzzle p = new DragNDropPuzzle((Element)document.getElementsByTagName("puzzle").item(0));
        assertEquals(PuzzleType.DnD, p.getType());
        assertEquals(false, p.isIndentRequired());
        assertEquals("Puzzle 1", p.getName());
        assertEquals("None Specified", p.getLanguage());
        assertEquals("", p.getDescription());
        Puzzle p2 = new MultipleChoicePuzzle((Element)document.getElementsByTagName("puzzle").item(1));
        assertEquals(PuzzleType.MC, p2.getType());
        assertEquals(false, p2.isIndentRequired());
        assertEquals("Puzzle 2", p2.getName());
        assertEquals("None Specified", p2.getLanguage());
        assertEquals("", p2.getDescription());
    }
}