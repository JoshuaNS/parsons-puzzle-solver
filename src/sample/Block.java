package sample;


import java.util.ArrayList;
import java.util.List;

/**
 * A block is a particular line of code that is present in a puzzle
 * A block may be part of a solution or it may be a distractor to another block
 *
 * @author Joshua Seguin, Iain Davidson
 * @since November 6th 2018
 *
 */
public class Block {
    private Puzzle associatedPuzzle;
    private String id;
    private List<Block> associatedBlocks;
    private int tab;
    private int tabGuess;
    private String[] lines;

    /**
     * Block constructor which takes block ID, the block text and associated puzzle
     *
     * @param id
     * @param textInput
     * @param associatedPuzzle
     */
    public Block(String id, String textInput, Puzzle associatedPuzzle) {
        this.associatedPuzzle = associatedPuzzle;
        int tabWidth;
        if (this.associatedPuzzle == null || associatedPuzzle.getTabWidth() == 0) {
            tabWidth = 4;
        } else {
            tabWidth = associatedPuzzle.getTabWidth();
        }
        String[] lines = textInput.split("\n", -1);

        // Calculate tab
        int minTab = calculateTab(lines[0]);
        for (int i = 1; i < lines.length; i++) {
            // If the line is only whitespace, don't count it in minTab calcuation
            if (lines[i].trim().length() > 0 && calculateTab(lines[i]) < minTab) {
                minTab = calculateTab(lines[i]);
            }
        }

        this.lines = new String[lines.length];
        this.tab = minTab;
        this.tabGuess = 0;
        this.id = id;
        this.associatedBlocks = new ArrayList<>();

        // Reduce tabs
        for (int i = 0; i < lines.length; i++) {
            String line = spaceToTab(lines[i], tabWidth);
            this.lines[i] = reduceTab(line, minTab);
        }
    }

    /**
     * Constructor with no associated puzzle
     *
     * @param id
     * @param textInput
     */
    public Block(String id, String textInput) {
        this(id, textInput, null);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Block)) {
            return false;
        }
        Block b = (Block) o;
        return this.id.equals(b.id) && this.associatedPuzzle == b.associatedPuzzle;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Converts the lines into a single string with newLine characters
     *
     * @return
     */
    public String getLines() {
        StringBuilder sb = new StringBuilder();
        for (String line : this.lines) {
            sb.append(line);
            sb.append('\n');
        }
        // Remove last newline
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    /**
     * Converts space in line to tabs
     *
     * @param line
     * @param tabWidth
     * @return
     */
    private static String spaceToTab(String line, int tabWidth) {
        int spaceCount = 0;
        char[] characters = line.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < characters.length; i++) {
            if (characters[i] == '\t') {
                sb.append(characters[i]);
            } else if (characters[i] == ' ') {
                spaceCount++;
                if (spaceCount % tabWidth == 0) {
                    sb.append('\t');
                }
            } else {
                sb.append(line.substring(i));
                break;
            }
        }
        return sb.toString();
    }

    /**
     * Counts the number of tabs in a line
     *
     * @param line
     * @return
     */
    private static int calculateTab(String line) {
        int tabCount = 0;

        for (char c : line.toCharArray()) {
            if (c == '\t') {
                tabCount++;
            } else {
                break;
            }
        }
        return tabCount;
    }

    /**
     * Remove leading tabs from a line up to the number defined by count
     *
     * @param line
     * @param count
     * @return
     */
    private static String reduceTab(String line, int count) {
        int tabIndex = 0;

        char[] characters = line.toCharArray();
        for (int i = 0; tabIndex < count && i < characters.length; i++) {
            if (characters[i] == '\t') {
                tabIndex++;
            } else {
                break;
            }
        }

        return line.substring(tabIndex);
    }

    public int getTab() {
        return tab;
    }

    public String getID() {
        return id;
    }

    public List<Block> getAssociatedBlocks() {
        return associatedBlocks;
    }

    public void addAssociatedBlock(Block b) {
        associatedBlocks.add(b);
    }

    public boolean hasAssociatedBlocks() {
        return associatedBlocks.size() != 0;
    }

    public int getSolutionTab() {
        return tabGuess;
    }

    public void setSolutionTab(int newTab) {
        tabGuess = newTab;
    }

    ;
}
