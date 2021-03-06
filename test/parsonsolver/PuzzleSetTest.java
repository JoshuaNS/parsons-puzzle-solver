package parsonsolver;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PuzzleSetTest: Test class for PuzzleSet
 * @author Joshua Seguin, Iain Davidson
 * @since November 6th 2018
 *
 */
class PuzzleSetTest {
    /**
     * Test of import of a sample xml file
     */
    @Test
    void importSamplePuzzle1() {
        PuzzleSet ps = new PuzzleSet("Lab 1");
        File f = new File("testfiles/puzzlesamp.pzl");
        try {
            ps.importPuzzleSet(f);
        } catch (InvalidInputFileException e) {
            fail(e);
        }
        ArrayList<Puzzle> puzzles = ps.getPuzzles();
        assertNotNull(puzzles.get(0));
        assertEquals("Lab 1 - Puzzle 1", puzzles.get(0).getName());
        assertNotNull(puzzles.get(1));
        assertEquals("Lab 1 - Puzzle 2", puzzles.get(1).getName());
        assertNotNull(puzzles.get(2));
        assertEquals("Lab 1 - Puzzle 3", puzzles.get(2).getName());
    }

    /**
     * Test of import with Name, sequential, random all not specified
     */
    @Test
    void importSamplePuzzleInvalid() throws InvalidInputFileException {
        PuzzleSet ps = new PuzzleSet();
        File f = new File("testfiles/puzzlesamperror2.pzl");

        ps.importPuzzleSet(f);

        assertEquals("Puzzle Set", ps.getName());
        assertFalse(ps.isRandomOrder());
        assertFalse(ps.isSequentialCompletion());
    }

    /**
     * Test of importing a puzzle set with the indices out of order
     * @throws InvalidInputFileException
     */
    @Test
    void importOutOfOrderPuzzleIndices() throws InvalidInputFileException {
        PuzzleSet ps = new PuzzleSet();
        File f = new File("testfiles/puzzlesamp_out_of_order.pzl");
        assertTrue(f.exists());
        ps.importPuzzleSet(f);
        assertEquals(3, ps.getPuzzles().size());
    }

    /**
     * Test of importing a puzzle set with a missing(skipped) puzzle index
     */
    @Test
    void importMissingPuzzleIndex() {
        PuzzleSet ps = new PuzzleSet();
        File f = new File("testfiles/puzzlesamperror_missing_index.pzl");
        assertTrue(f.exists());
        assertThrows(InvalidInputFileException.class, ()-> ps.importPuzzleSet(f));
    }

    /**
     * Test of operation of program
     * Operates a simulation of a puzzle import and solve for DnD and MC
     */
    @Test
    void operationTest() throws InterruptedException{
        System.out.print("Welcome to the puzzle solver. Please import a puzzle.\n");
        File f = new File("testfiles/puzzlesamp.pzl");
        System.out.print("Importing file......\n");
        PuzzleSet ps = null;
        try {
            ps = new PuzzleSet(f);
        } catch (InvalidInputFileException e) {
            e.printStackTrace();
        }
        System.out.print("Import successful.\nPuzzles found:\n");
        for (int i = 0; i < ps.getPuzzles().size(); i++){
            System.out.print(ps.getPuzzles().get(i).getName() +", index " +ps.getPuzzles().get(i).getIndex() +"\n");
        }
        System.out.print("Enter an index to open a puzzle:\n");
        int input = 1;
        System.out.print("Loading puzzle with index " +input +"...\n");
        Puzzle currentPuzz = ps.getPuzzle(input);
        //check that the puzzle is solvable before opening
        if(!currentPuzz.isSolvable()){
            System.out.print("Cannot open puzzle.\n\n");
        }
        System.out.print("Puzzle loaded.\n\n");
        currentPuzz.startPuzzle();

        if (currentPuzz.getType().equals(PuzzleType.DnD)) {
            System.out.print("Drag into the correct order:\n");
            List<Block> choices = currentPuzz.getChoices().get(0);
            for (int i = 0; i < choices.size(); i++) {
                System.out.print(choices.get(i).getLines() + "\n");
            }

            List<Block> providedSolution = new ArrayList<>();
            providedSolution.add(new Block("1", "for num in range(1, 21):", 0, currentPuzz));
            providedSolution.add(new Block("2", "if num % 3 == 0 and num % 5 == 0:", 1, currentPuzz));
            providedSolution.add(new Block("3", "print('FizzBuzz')", 2, currentPuzz));
            providedSolution.add(new Block("4", "elif num % 3 == 0:", 1, currentPuzz));
            providedSolution.add(new Block("5", "print('Fizz')", 2, currentPuzz));
            providedSolution.add(new Block("6", "elif num % 5 == 0:", 1, currentPuzz));
            providedSolution.add(new Block("7", "print('Buzz')", 2, currentPuzz));
            providedSolution.add(new Block("8", "else:", 1, currentPuzz));
            providedSolution.add(new Block("9", "print(num)", 2, currentPuzz));

            System.out.print("\nChecking solution...\n");
            boolean result = (boolean) currentPuzz.checkSolution(providedSolution);

            assertTrue(result);
            Thread.sleep(20);
            currentPuzz.endPuzzle();
            System.out.print("Congratulations! Puzzle solved.\n");
            System.out.print("Returning to menu...\n");
        }
        System.out.print("Enter an index to open a puzzle:\n");
        input = 3;
        System.out.print("Loading puzzle with index " +input +"...\n");
        currentPuzz = ps.getPuzzle(input);
        if(!currentPuzz.isSolvable()){
            System.out.print("Cannot open puzzle.\n\n");
        }
        System.out.print("Puzzle loaded.\n\n");
        currentPuzz.startPuzzle();

        if (currentPuzz.getType().equals(PuzzleType.FiB)){
            FillBlanksPuzzle fbPuzz = (FillBlanksPuzzle) currentPuzz;
            System.out.print("Choose the correct choice:\n");
            List<Block> lines = fbPuzz.getLines();
            int choiceAreaCount = 1;
            for (int i = 0; i < lines.size(); i++){
                if (lines.get(i).hasAssociatedBlocks()) {
                    System.out.println("<<AREA " + choiceAreaCount++ + ">>");
                }
                else {
                    System.out.print(lines.get(i).getLines() + "\n");
                }
            }
            System.out.println();
            System.out.println("Choices are:");
            List<Block> solutionSet = fbPuzz.getSolutionSet();
            choiceAreaCount = 1;
            for (int i = 0; i < solutionSet.size(); i++) {
                List<Block> choices = new ArrayList<>();
                choices.add(solutionSet.get(i));
                choices.addAll(solutionSet.get(i).getAssociatedBlocks());
                Collections.shuffle(choices);
                System.out.println("<<AREA " + choiceAreaCount++ + ">>:");
                for (Block b : choices) {
                    System.out.println(b.getLines());
                }
                System.out.println();
            }

            List<List<Block>> answers = new ArrayList<>();
            answers.add(currentPuzz.getSolutionSet());
            answers.addAll(currentPuzz.getChoices());

            // Select the correct choice
            int choice = answers.indexOf(currentPuzz.getSolutionSet());
            System.out.print("\nChecking solution...\n");
            boolean result = (boolean) fbPuzz.checkSolution(answers.get(choice));

            assertTrue(result);
            Thread.sleep(70);
            currentPuzz.endPuzzle();
            System.out.print("Congratulations! Puzzle solved.\n");
            System.out.print("Returning to menu...\n");
        }
        System.out.print("Enter an index to open a puzzle:\n");
        input = 2;
        System.out.print("Loading puzzle with index " +input +"...\n");
        currentPuzz = ps.getPuzzle(input);
        if(!currentPuzz.isSolvable()){
            System.out.print("Cannot open puzzle.\n\n");
        }
        System.out.print("Puzzle loaded.\n\n");
        currentPuzz.startPuzzle();
        if (currentPuzz.getType().equals(PuzzleType.MC)){
            MultipleChoicePuzzle mcPuzz = (MultipleChoicePuzzle) currentPuzz;
            System.out.print("Choose the correct choice:\n");

            List<List<Block>> answers = mcPuzz.getChoices();

            for (int i = 0; i < answers.size(); i++){
                System.out.print("\nAnswer " +(i+1) +"\n");
                for (int j = 0; j < answers.get(i).size(); j++) {
                    System.out.print(answers.get(i).get(j).getLines() +"\n");
                }
            }
            // Choosing the correct answer.
            int choice = answers.indexOf(currentPuzz.getSolutionSet());

            System.out.print("\nChecking solution...\n");
            boolean result = (boolean) mcPuzz.checkSolution(answers.get(choice));

            assertTrue(result);
            Thread.sleep(100);
            currentPuzz.endPuzzle();
            System.out.print("Congratulations! Puzzle solved.\n");
            System.out.print("Returning to menu...\n");
        }

        System.out.print("Enter an index to open a puzzle:\n");
        ArrayList<String> result = ps.exportResults();
        for(int i = 0; i<result.size(); i++){
            System.out.print(result.get(i));
        }
        input = 0;
        System.out.print("Exiting program.\n");
    }

    @Test
    void exportToXML() throws Exception {
        PuzzleSet ps = new PuzzleSet("Lab 1");
        File f1 = new File("testfiles/puzzlesamp.pzl");
        try {
            ps.importPuzzleSet(f1);
        } catch (InvalidInputFileException e) {
            fail(e);
        }

        File f2 = new File("out/test/exportTest.xml");

        ps.exportToXML(f2);
        DocumentBuilder factory = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        Document doc1 = factory.parse(f1);
        Document doc2 = factory.parse(f2);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer1 = new StringWriter();
        StringWriter writer2 = new StringWriter();
        transformer.transform(new DOMSource(doc1), new StreamResult(writer1));
        transformer.transform(new DOMSource(doc2), new StreamResult(writer2));
        String output1 = writer1.getBuffer().toString();
        String output2 = writer2.getBuffer().toString();


        // Don't worry about leading or trailing whitespace in the xml
        String[] lines1 = output1.split("\n");
        String[] lines2 = output2.split("\n");

        try {
            for (int i = 0; i < lines1.length; i++) {
                lines1[i] = lines1[i].trim();
                lines2[i] = lines2[i].trim();
            }
        } catch (IndexOutOfBoundsException e) {
            fail(e);
        }

        assertArrayEquals(lines1, lines2);
    }

    @Test
    void exportBadPuzzle() throws ParserConfigurationException {
        Puzzle p = new DragNDropPuzzle("BadPuzzle", 3); // Puzzle with no solutions
        DocumentBuilder factory = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document d = factory.newDocument();

        assertThrows(UnformedPuzzleException.class, ()-> p.exportToXML(d));
    }
}