package sample;
/**
 * MultipleChoicePuzzleTest: Test class for MultipleChoicePuzzle
 * @author Joshua Seguin, Iain Davidson
 * @since November 12th 2018
 *
 */
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
     * Test of importing false answers
     */
    @Test
    public void importFalseAnswersSuccessful()
    {
        List<Block> solution = new ArrayList<>();
        solution.add(new Block("1", "for num in range(1, 21):", p));
        solution.add(new Block("2", "if num % 3 == 0 and num % 5 == 0:", p));
        solution.add(new Block("3", "print('FizzBuzz')", p));
        solution.add(new Block("4", "elif num % 3 == 0:", p));
        solution.add(new Block("5", "print('Fizz')", p));
        solution.add(new Block("6", "elif num % 5 == 0:", p));
        solution.add(new Block("7", "print('Buzz')", p));
        solution.add(new Block("8", "else:", p));
        solution.add(new Block("9", "print(num)", p));

        List<Block> answer1 = new ArrayList<>();
        answer1.add(p.getBlock("0X1"));
        answer1.add(p.getBlock("2"));
        answer1.add(p.getBlock("3"));
        answer1.add(p.getBlock("4"));
        answer1.add(p.getBlock("5"));
        answer1.add(p.getBlock("6"));
        answer1.add(p.getBlock("7"));
        answer1.add(p.getBlock("8"));
        answer1.add(p.getBlock("9"));

        List<Block> answer2 = new ArrayList<>();
        answer2.add(p.getBlock("1"));
        answer2.add(p.getBlock("0X2"));
        answer2.add(p.getBlock("3"));
        answer2.add(p.getBlock("0X3"));
        answer2.add(p.getBlock("5"));
        answer2.add(p.getBlock("0X4"));
        answer2.add(p.getBlock("7"));
        answer2.add(p.getBlock("8"));
        answer2.add(p.getBlock("9"));

        assertTrue(p.getChoices().contains(answer1));
        assertTrue(p.getChoices().contains(answer2));
        assertTrue(p.getChoices().contains(solution));
    }

    /**
     * Checking valid solution
     */
    @Test
    public void checkSolutionMultipleChoice() {
        List<Block> answers = new ArrayList<>(p.getSolutionSet());

        assertTrue((boolean)p.checkSolution(answers));
    }

    /**
     * Check various feedback
     */
    @Test
    public void checkFeedback() {
        List<Block> answers = new ArrayList<>(p.getSolutionSet());


        int startTime = (int) System.currentTimeMillis();
        assertEquals(p.getNumAttempts(), 0);
        assertFalse(p.isCompleted());
        assertTrue((boolean)p.checkSolution(answers));
        assertEquals(p.getNumAttempts(), 1);
        assertTrue(p.isCompleted());
        p.setTimeElapsed(p.getTimeElapsed() + startTime);
        assertTrue(p.getTimeElapsed() > 0);
    }

    /**
     * Checking invalid solution of the provided puzzle
     */
    @Test
    void checkBadSolutionMultipleChoice() {
        List<Block> answers = new ArrayList<>(p.getSolutionSet());
        answers.remove(0);

        assertEquals(p.getNumAttempts(), 0);
        assertFalse(p.isCompleted());
        assertFalse((boolean)p.checkSolution(answers));
        assertEquals(p.getNumAttempts(), 1);
        assertFalse(p.isCompleted());
    }
}