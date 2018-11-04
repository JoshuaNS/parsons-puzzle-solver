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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MultipleChoicePuzzleTest {
    private static MultipleChoicePuzzle p;
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

        p = new MultipleChoicePuzzle((Element)document.getElementsByTagName("puzzle").item(1));
    }
    @Test
    void checkSolutionMultipleChoice() {
        List<Block> answers = new ArrayList<>(p.getSolutionSet());

        assertTrue((boolean) p.checkSolution(answers));
    }

    @Test
    void checkBadSolutionMultipleChoice() {
        ArrayList<Block> answers = new ArrayList<>(p.getSolutionSet());

        // Replace one of the answers with a distractor
        Block distractorForLineTwo = answers.get(1).getAssociatedBlocks().get(0);
        answers.remove(1);
        answers.add(1, distractorForLineTwo);

        assertEquals(new Block("2X1", "if num % 3 == 0:", p), distractorForLineTwo);
        assertFalse((boolean) p.checkSolution(answers));
    }
}