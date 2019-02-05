package parsonsolver;

import org.junit.jupiter.api.Test;

import java.awt.dnd.InvalidDnDOperationException;
import java.io.File;
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
        File f = new File("testfiles/puzzlesamp.xml");
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
        File f = new File("testfiles/puzzlesamperror2.xml");

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
        File f = new File("testfiles/puzzlesamp_out_of_order.xml");
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
        File f = new File("testfiles/puzzlesamperror_missing_index.xml");
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
        File f = new File("testfiles/puzzlesamp.xml");
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

        if (currentPuzz.getType().equals(PuzzleType.DnD)){
            System.out.print("Drag into the correct order:\n");
            List<Block> choices = currentPuzz.getChoices().get(0);
            for (int i = 0; i < choices.size(); i++){
                System.out.print(choices.get(i).getLines() +"\n");
            }

            List<Block> providedSolution = new ArrayList<>();
            providedSolution.add(new Block("1","for num in range(1, 21):", currentPuzz));
            providedSolution.add(new Block("2","if num % 3 == 0 and num % 5 == 0:", currentPuzz));
            providedSolution.add(new Block("3","print('FizzBuzz')", currentPuzz));
            providedSolution.add(new Block("4","elif num % 3 == 0:", currentPuzz));
            providedSolution.add(new Block("5","print('Fizz')", currentPuzz));
            providedSolution.add(new Block("6","elif num % 5 == 0:", currentPuzz));
            providedSolution.add(new Block("7","print('Buzz')", currentPuzz));
            providedSolution.add(new Block("8","else:", currentPuzz));
            providedSolution.add(new Block("9","print(num)", currentPuzz));

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
}