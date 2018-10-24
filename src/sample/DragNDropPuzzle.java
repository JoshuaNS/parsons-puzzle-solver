package sample;

import org.w3c.dom.Element;

public class DragNDropPuzzle extends Puzzle {

    public DragNDropPuzzle(Element puzzleXML) {
        super(puzzleXML);
    }

    @Override
    Object checkSolution(Object providedSolution) {
        return null;
    }
}
