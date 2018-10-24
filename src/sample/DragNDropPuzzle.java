package sample;

import org.w3c.dom.Element;

import java.util.ArrayList;

public class DragNDropPuzzle extends Puzzle {

    public DragNDropPuzzle(Element puzzleXML) {
        super(puzzleXML);
    }

    @Override
    Object checkSolution(Object providedSolution) {
        if (!(providedSolution instanceof ArrayList)) {
            System.err.println("Solution wasn't provided as ArrayList");
            return null;
        }
        ArrayList<String> givenSolution = (ArrayList<String>)providedSolution;

        if (getSolutions().size() != givenSolution.size()) {
            return false;
        }
        for (int i = 0; i < getSolutions().size(); i++) {
            if (!givenSolution.get(i).equals(getSolutions().get(i))) {
                return false;
            }
        }
        return true;
    }
}
