package sample;

import org.w3c.dom.Element;

import java.util.*;

public class MultipleChoicePuzzle extends Puzzle {

    public MultipleChoicePuzzle(Element puzzleAtIndex) {
        super(puzzleAtIndex);
        List<Block> solution = new ArrayList<>();
        for (Block b : this.getLines()) {
            if (b.hasAssociatedBlocks()) {
                solution.add(b);
            }
        }
        this.setSolutionSet(Collections.unmodifiableList(solution));
    }

    @Override
    Object checkSolution(List<Block> providedSolution) {

        if (providedSolution.size() != getSolutionSet().size()) {
            return false;
        }
        for (int i = 0; i < getSolutionSet().size(); i++) {
            if (!providedSolution.get(i).equals(getSolutionSet().get(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     *
     * @param choiceQuantity
     * @return
     * array of possible answers. The first answer is the correct one
     */
    public List<List<Block>> buildAnswers(int choiceQuantity){
        List<List<Block>> answers = new ArrayList<>();
        answers.add(getSolutionSet());
        Random rand = new Random(this.hashCode());

        // Determine if possible to have that choiceQuantity

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
                answers.add(i + 1, answer);
            }
            if (retryCount == retryMax) {
                System.err.println("Could not find unique answer set within choice quantity");
                break;
            }
        }
        return answers;
    }
}
