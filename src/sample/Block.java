package sample;


import java.util.ArrayList;
import java.util.List;

public class Block {
    private Puzzle associatedPuzzle;
    private String id;
    private List<Block> associatedBlocks;
    private int tab;
    private String[] lines;

    public Block(String id, String textInput, Puzzle associatedPuzzle) {
        this.associatedPuzzle = associatedPuzzle;
        int tabWidth;
        if (this.associatedPuzzle == null) {
            tabWidth = 4;
        }
        else {
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
        this.id = id;
        this.associatedBlocks = new ArrayList<>();

        // Reduce tabs
        for (int i = 0; i < lines.length; i++) {
            String line = spaceToTab(lines[i], tabWidth);
            this.lines[i] = reduceTab(line, minTab);
        }
    }
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

    private static String spaceToTab(String line, int tabWidth) {
        int spaceCount = 0;
        char[] characters = line.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < characters.length; i++) {
            if (characters[i] == '\t') {
                sb.append(characters[i]);
            }
            else if (characters[i] == ' ') {
                spaceCount++;
                if (spaceCount % tabWidth == 0) {
                    sb.append('\t');
                }
            }
            else {
                sb.append(line.substring(i));
                break;
            }
        }
        return sb.toString();
    }
    private static int calculateTab(String line) {
        int tabCount = 0;

        for (char c : line.toCharArray()) {
            if (c == '\t') {
                tabCount++;
            }
            else {
                break;
            }
        }
        return tabCount;
    }

    private static String reduceTab(String line, int count) {
        int tabIndex = 0;

        char[] characters = line.toCharArray();
        for (int i = 0; tabIndex < count && i < characters.length; i++) {
            if (characters[i] == '\t') {
                tabIndex++;
            }
            else {
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
}
