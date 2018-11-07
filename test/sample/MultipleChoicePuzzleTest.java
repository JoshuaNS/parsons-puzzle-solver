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
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
/**
 * MultipleChoicePuzzleTest: Test class for MultipleChoicePuzzle
 * @author Joshua Seguin, Iain Davidson
 * @since November 6th 2018
 *
 */
class MultipleChoicePuzzleTest {
    private static MultipleChoicePuzzle p;
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

        p = new MultipleChoicePuzzle((Element)document.getElementsByTagName("puzzle").item(1));
    }

    /**
     * Checking the solution of the provided puzzle
     */
    @Test
    void checkSolutionMultipleChoice() {
        List<Block> answers = new ArrayList<>(p.getSolutionSet());

        assertTrue((boolean) p.checkSolution(answers));
    }

    /**
     * Checking invalid solution of the provided puzzle
     */
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

    /**
     * Check that result of buildAnswers contains the solution
     */
    @Test
    void buildAnswersContainsSolution() {
        List<List<Block>> answers = p.buildAnswers( 4);

        assertTrue(answers.contains(p.getSolutionSet()));
    }

    @Test
    void buildAnswersUniqueArray() {
        List<List<Block>> answers = p.buildAnswers(4);

        assertEquals(answers.size(), new HashSet<>(answers).size());
    }

    @Test
    void buildAnswersBadChoiceQuantity() {
        List<List<Block>> answers = p.buildAnswers(30);
        for (List<Block> answer : answers) {
            for (Block b : answer) {
                System.out.println(b.getLines());
            }
            System.out.println();
        }
    }
}