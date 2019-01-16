package parsonsolver;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BlockTest: Test class for Block
 * @author Joshua Seguin, Iain Davidson
 * @since November 6th 2018
 *
 */
class BlockTest {

    /**
     * Test of space/tab conversion upon block import
     */
    @Test
    void importConvertSpaceToTab() {
        String s = "    if (X) {";
        Block b = new Block("1", s);
        assertEquals("if (X) {", b.getLines());
        assertEquals(1, b.getTab());
    }

    /**
     * Test of prepending whitespace upon import
     */
    @Test
    void importNoPrependingWhitespace() {
        String s = "for (int i = 0; i < 10; i++)";
        Block b = new Block("1", s);
        assertEquals(s, b.getLines());
    }

    /**
     * Test of tab conversion upon import
     */
    @Test
    void importManyTab() {
        String s = "\t\t\tint x;";
        Block b = new Block("1", s);

        assertEquals(3, b.getTab());
        assertEquals("int x;", b.getLines());
    }

    /**
     * Test of multiline string input
     */
    @Test
    void importNoPrependMultiLine() {
        String s = "int i = 0;\nint x = 5;\ndouble y = 3.2\n\n";
        Block b = new Block("1", s);

        assertEquals(s, b.getLines());
    }

    /**
     * Test of import with multiple empty lines
     */
    @Test
    void importBlockWithEmptyLines() {
        String s = "\tThis is a test\n\n\n\tFor mulitple lines";
        Block b = new Block("1", s);

        assertEquals(1, b.getTab());
        assertEquals("This is a test\n\n\nFor mulitple lines", b.getLines());
    }

    /**
     * Test of supporting inner tab input
     */
    @Test
    void importInnerTabLarger() {
        String s = "\tfor (int i = 0; i < 10; i++) {\n\t\tprintf(\"%d\\n\", i);\n\t}";
        Block b = new Block("1", s);

        assertEquals(1, b.getTab());
        assertEquals("for (int i = 0; i < 10; i++) {\n\tprintf(\"%d\\n\", i);\n}", b.getLines());
    }

    /**
     * Test of supporting outer tab input
     */
    @Test
    void importOuterTabLarger() {
        String s = "\t\t\tABC\n\tD\n\t\t\tEFG";
        Block b = new Block("1", s);

        assertEquals(1, b.getTab());
        assertEquals("\t\tABC\nD\n\t\tEFG", b.getLines());
    }

    /**
     * Test of input with empty block
     */
    @Test
    void emptyBlock() {
        Block b = new Block("1", "");
        assertEquals("", b.getLines());
    }
}