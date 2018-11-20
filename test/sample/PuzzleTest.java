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
/**
 * PuzzleTest: Test class for Puzzle
 * @author Joshua Seguin, Iain Davidson
 * @since November 6th 2018
 *
 */
public class PuzzleTest {

    /**
     * Test import of a single puzzle using a full puzzle set file
     */
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

        Puzzle p = null;
        try {
            p = new DragNDropPuzzle((Element)document.getElementsByTagName("puzzle").item(0));
        } catch (InvalidInputFileException e) {
            e.printStackTrace();
        }

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
        assertEquals("Output the numbers 1-20, print \"fizz\" if the number is divisible by 3, \"buzz\" if it is divisible by 5, and \"fizzbuzz\" if it is divisible by both",
                p.getDescription());
        assertEquals("Lab 1 - Puzzle 1", p.getName());
        assertEquals("Python", p.getLanguage());
        assertEquals(PuzzleType.DnD, p.getType());
        assertEquals(false, p.isIndentRequired());

        assertNotNull(p.getLines());
        for (int i = 0; i < p.getLines().size(); i++) {
            assertEquals(expectedSolution.get(i), p.getLines().get(i).getLines());
        }
        assertNotNull(p.getDistractors());
        for (int i = 0; i < p.getDistractors().size(); i++) {
            assertEquals(expectedDistractors.get(i), p.getDistractors().get(i).getLines());
        }
    }

    /**
     * Test of puzzle import with no indent, no name, no language, no description, and ensures it functions as desired
     */
    @Test
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

        Puzzle p = null;
        try {
            p = new DragNDropPuzzle((Element)document.getElementsByTagName("puzzle").item(0));
        } catch (InvalidInputFileException e) {
            e.printStackTrace();
        }
        assertEquals(PuzzleType.DnD, p.getType());
        assertEquals(false, p.isIndentRequired());
        assertEquals("Puzzle 1", p.getName());
        assertEquals("None Specified", p.getLanguage());
        assertEquals("", p.getDescription());
        Puzzle p2 = null;
        try {
            p2 = new MultipleChoicePuzzle((Element)document.getElementsByTagName("puzzle").item(1));
        } catch (InvalidInputFileException e) {
            assertTrue(false);
        }
        assertEquals(PuzzleType.MC, p2.getType());
        assertEquals(false, p2.isIndentRequired());
        assertEquals("Puzzle 2", p2.getName());
        assertEquals("None Specified", p2.getLanguage());
        assertEquals("", p2.getDescription());
    }
}