package sample;

public class Block {
    private Puzzle associatedPuzzle;
    private String id;
    private int tab;
    private String[] lines;

    public Block(String id, String textInput, Puzzle associatedPuzzle) {
        this.associatedPuzzle = associatedPuzzle;
        String[] lines = textInput.split("\n");

        // Calculate tab
        int minTab = calculateTab(lines[0]);
        for (int i = 1; i < lines.length; i++) {
            if (calculateTab(lines[i]) < minTab) {
                minTab = calculateTab(lines[i]);
            }
        }

        this.lines = new String[lines.length];
        this.tab = minTab;
        this.id = id;

        // Reduce tabs
        for (int i = 0; i < lines.length; i++) {
            this.lines[i] = reduceTab(lines[i], minTab);
        }
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

    private int calculateTab(String line) {
        int tabCount = 0;
        int spaceCount = 0;
        for (char c : line.toCharArray()) {
            if (c == '\t') {
                tabCount++;
            }
            else if (c == ' ') {
                spaceCount++;
                if (spaceCount % associatedPuzzle.getTabWidth() == 0) {
                    tabCount++;
                }
            }
            else {
                break;
            }
        }
        return tabCount;
    }

    private String reduceTab(String line, int count) {
        int tabIndex = 0;
        int spaceCount = 0;

        char[] characters = line.toCharArray();
        for (int i = 0; tabIndex < count && i < characters.length; i++) {
            if (characters[i] == '\t') {
                tabIndex++;
            }
            else if (characters[i] == ' ') {
                spaceCount++;
                if (spaceCount % associatedPuzzle.getTabWidth() == 0) {
                    tabIndex++;
                }
            }
            else {
                break;
            }
        }
        // If space count isn't 0, spaces must have been used as indenting characters, so remove those
        if (spaceCount != 0) {
            return line.substring(spaceCount);
        }
        // Otherwise, use tab characters must be used, or there were no prepending whitespace characeters
        else {
            return line.substring(tabIndex);
        }
    }

    public int getTab() {
        return tab;
    }

    public String getID() {
        return id;
    }
}
