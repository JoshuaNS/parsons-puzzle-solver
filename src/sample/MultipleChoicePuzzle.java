package sample;

import org.w3c.dom.Element;

public class MultipleChoicePuzzle extends Puzzle {

    public MultipleChoicePuzzle(Element puzzleAtIndex) {
        super(puzzleAtIndex);
    }

    @Override
    Object checkSolution(Object providedSolution) {
        return null;
    }
}
