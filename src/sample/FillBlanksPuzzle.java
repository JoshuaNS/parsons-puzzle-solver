package sample;

import org.w3c.dom.Element;

import java.util.*;

/**
 * Child class for Multiple Choice Puzzles
 *
 * @author Joshua Seguin, Iain Davidson
 * @since November 6th 2018
 *
 */
public class FillBlanksPuzzle extends Puzzle {

    /**
     * Constructor which takes in a puzzle xml object
     * Sets the solution which is the list of blocks that have associated distractors
     * @param puzzleAtIndex
     */
    public FillBlanksPuzzle(Element puzzleAtIndex) {
        super(puzzleAtIndex);
        List<Block> solution = new ArrayList<>();
        for (Block b : this.getLines()) {
            if (b.hasAssociatedBlocks()) {
                solution.add(b);
            }
        }
        this.setSolutionSet(Collections.unmodifiableList(solution));
    }

    /**
     * Method that defines the method of checking if a solution is correct
     * @param providedSolution
     * @return
     */
    @Override
    Object checkSolution(List<Block> providedSolution) {
        //when checking solution, add 1 to number of attempts
        this.setNumAttempts(this.getNumAttempts() + 1);

        if (providedSolution.size() != getSolutionSet().size()) {
            return false;
        }
        for (int i = 0; i < getSolutionSet().size(); i++) {
            if (!providedSolution.get(i).equals(getSolutionSet().get(i))) {
                return false;
            }
        }
        this.setCompleted(true);
        return true;
    }

    /**
     * array of possible answers, presented in a scrambled state
     * @return
     */
    @Override
    public List<List<Block>> getChoices(){
        List<List<Block>> answers = new ArrayList<>();
        Random rand = new Random(this.hashCode());
	    int choiceQuantity = 5;
        answers.add(getSolutionSet());
        List<List<Block>> possibilities = new ArrayList<>();
        List<Block> unassociatedDistractors = new ArrayList<>();
        for (Block b : getDistractors()) {
            if (!b.hasAssociatedBlocks()){
                unassociatedDistractors.add(b);
            }
        }

        // Each list of possibilites contains all possible lines that could make up that line (correct block or distractor)
        for (Block b : getSolutionSet()) {
            List<Block> arr = new ArrayList<>();
            arr.add(b);
            arr.addAll(b.getAssociatedBlocks());
            // Unassociated distractors can be in any position
            arr.addAll(unassociatedDistractors);

            possibilities.add(arr);

        }
        int retryMax = 5;
        int retryCount = 0;

        // Fill the remaining potential answers with randomly generated false answers, ensuring uniqueness
        for (int i = 0; i < choiceQuantity - 1; i++) {
            List<Block> answer = new ArrayList<>();
            for (int j = 0; j < getSolutionSet().size(); j++) {
                answer.add(j, possibilities.get(j).get(rand.nextInt(possibilities.get(j).size())));
            }

            if (answers.contains(answer)) {
                i--;
                retryCount++;
            }
            else {
                answers.add(i+1, answer);
            }
            if (retryCount == retryMax) {
                System.err.println("Could not find unique answer set within choice quantity");
                break;
            }
        }
        Collections.shuffle(answers);
        return answers;
    }

}
