package parsonsolver;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
/**
 * FillBlanksPuzzleTest: Test class for FillBlanksPuzzle
 * @author Joshua Seguin, Iain Davidson
 * @since November 6th 2018
 *
 */
class FillBlanksPuzzleTest {
    private static FillBlanksPuzzle p;
    /**
     * Test of import of a sample xml file
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

        try {
            p = new FillBlanksPuzzle((Element)document.getElementsByTagName("puzzle").item(2));
        } catch (InvalidInputFileException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void resetPuzzle() {
        p.resetAttempts();
        p.setCompleted(false);
        p.setTimeElapsed(0);
    }

    /**
     * Checking the solution of the provided puzzle
     */
    @Test
    void checkSolutionFillInBlanks() {
        List<Block> answers = new ArrayList<>(p.getSolutionSet());

        assertTrue((boolean) p.checkSolution(answers));
    }

    /**
     * Checking various feedback
     */
    @Test
    void checkFeedback()  throws InterruptedException{
        List<Block> answers = new ArrayList<>(p.getSolutionSet());

        long startTime = System.currentTimeMillis();
        assertEquals(p.getNumAttempts(), 0);
        assertFalse(p.isCompleted());

        assertTrue((boolean) p.checkSolution(answers));

        assertEquals(p.getNumAttempts(), 1);
        assertTrue(p.isCompleted());
        Thread.sleep(50);
        p.setTimeElapsed(p.getTimeElapsed() + (System.currentTimeMillis() - startTime));
        assertTrue(p.getTimeElapsed() > 0);
    }

    /**
     * Checking invalid solution of the provided puzzle
     */
    @Test
    void checkBadSolutionFillInBlanks() throws InterruptedException{
        setupSamplePuzzle();
        ArrayList<Block> answers = new ArrayList<>(p.getSolutionSet());

        // Replace one of the answers with a distractor
        Block distractorForLineTwo = answers.get(1).getAssociatedBlocks().get(0);
        answers.remove(1);
        answers.add(1, distractorForLineTwo);

        p.startPuzzle();
        Thread.sleep(50); //wait because we want to ensure that the elapsed time is not 0
        assertEquals(new Block("2X1", "if num % 3 == 0:", p), distractorForLineTwo);
        assertEquals(0, p.getNumAttempts());
        assertFalse(p.isCompleted());
        assertFalse((boolean) p.checkSolution(answers));
        assertEquals(1, p.getNumAttempts());
        assertFalse(p.isCompleted());
        p.endPuzzle();
        assertTrue(p.getTimeElapsed() > 0);
    }


    @Test
    void getChoicesUniqueArray() {
        List<List<Block>> answers = p.getChoices();

        assertEquals(answers.size(), new HashSet<>(answers).size());
    }
}