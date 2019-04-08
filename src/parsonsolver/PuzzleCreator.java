package parsonsolver;

import java.util.ArrayList;
import java.util.List;

/**
 * The PuzzleCreator contains methods which will be used for a system to create puzzle xmls
 *
 * @author Iain Davidson
 * @since January 20th 2019
 *
 */

public class PuzzleCreator {

    private PuzzleSet currentSet;
    private Puzzle currentPuzzle;

    /**
     * Edit a pre-existing Puzzle Set
     * @param set
     */
    public void openSet (PuzzleSet set){
        this.setCurrentSet(set);
    }

    /**
     * Begin on a brand new Puzzle Set
     * @param name
     */
    public void createNewSet(String name){
        this.currentSet = new PuzzleSet(name);
    }

    /**
     * Removes current set and current puzzle data when closing the session
     */
    public void closeSession(){
        currentSet = null;
        currentPuzzle = null;
    }

    /**
     * Removes current puzzle data when done editing
     */
    public void closeEdit(){
        currentPuzzle = null;
    }

    /**
     * Create a new Puzzle in the current Puzzle Set
     * When a new Puzzle is created, it is automatically opened in the editor
     * @param name
     * @param type
     */
    public void createNewPuzzle(String name, PuzzleType type){
        int index = currentSet.getPuzzles().size() + 1;
        Puzzle p;
        if (type.equals(PuzzleType.DnD)){
            p = new DragNDropPuzzle(name, index);
        } else if (type.equals(PuzzleType.FiB)){
            p = new FillBlanksPuzzle(name, index);
        } else if (type.equals(PuzzleType.MC)){
            p = new MultipleChoicePuzzle(name, index);
        } else {
            return;
        }
        this.setCurrentPuzzle(p);
        currentSet.getPuzzles().add(p);
    }

    /**
     * Convert a group of lines a list of blocks
     * isDistractor indicates if we should prepend X to the block id
     * @param lines
     * @param isDistractor
     * @return
     */
    public List<Block> convertLines (List<String> lines, boolean isDistractor){
        List<Block> blocks = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++){
            Block b;
            if (isDistractor){
                b = new Block("0X" +(String.valueOf(i + 1)), lines.get(i), currentPuzzle);
            } else {
                b = new Block(String.valueOf(i + 1), lines.get(i), currentPuzzle);
            }
            blocks.add(b);
        }
        return blocks;
    }

    /**
     * open a puzzle in the puzzle set for editing
     * @param index
     */
    public void openPuzzle (int index){
        if (currentSet.getPuzzles().size() >= index && index > 0) {
            this.setCurrentPuzzle(currentSet.getPuzzle(index));
        }
    }

    /**
     * Export the puzzle set as an xml file
     */
    public void exportSet(){
        //todo export puzzle set as XML
    }

    public PuzzleSet getCurrentSet() {
        return currentSet;
    }

    public void setCurrentSet(PuzzleSet currentSet) {
        this.currentSet = currentSet;
    }

    public Puzzle getCurrentPuzzle() {
        return currentPuzzle;
    }

    public void setCurrentPuzzle(Puzzle currentPuzzle) {
        this.currentPuzzle = currentPuzzle;
    }


}
