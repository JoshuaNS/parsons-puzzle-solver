package sample;

import org.w3c.dom.Element;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
}
