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

class DragNDropPuzzleTest {
    private static Puzzle p;
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
    @Test
    void checkSolutionDragNDrop() {
        List<String> providedSolution = new ArrayList<>();
        providedSolution.add("for num in range(1, 21):");
        providedSolution.add("if num % 3 == 0 and num % 5 == 0:");
        providedSolution.add("print('FizzBuzz')");
        providedSolution.add("elif num % 3 == 0:");
        providedSolution.add("print('Fizz')");
        providedSolution.add("elif num % 5 == 0:");
        providedSolution.add("print('Buzz')");
        providedSolution.add("else:");
        providedSolution.add("print(num)");

        assertTrue((boolean) p.checkSolution(providedSolution));
    }

    @Test
    void checkBadSolutionDragNDrop() {
        List<String> providedSolution = new ArrayList<>();
        providedSolution.add("for num in range(1, 21):");
        providedSolution.add("elif num % 5 == 0:");
        providedSolution.add("print('Buzz')");
        providedSolution.add("else:");
        providedSolution.add("print(num)");
        providedSolution.add("if num % 3 == 0 and num % 5 == 0:");
        providedSolution.add("print('FizzBuzz')");
        providedSolution.add("elif num % 3 == 0:");
        providedSolution.add("print('Fizz')");

        assertFalse((boolean) p.checkSolution(providedSolution));
    }
}