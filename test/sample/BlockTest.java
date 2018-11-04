package sample;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockTest {

    @Test
    void importConvertSpaceToTab() {
        String s = "    if (X) {";
        Block b = new Block("1", s);
        assertEquals("\tif (X) {", b.getLines());
    }
    @Test
    void importNoPrependingWhitespace() {
        String s = "for (int i = 0; i < 10; i++)";
        Block b = new Block("1", s);
        assertEquals(s, b.getLines());
    }


    @Test
    void importManyTab() {
        String s = "\t\t\tint x;";
        Block b = new Block("1", s);

        assertEquals(3, b.getTab());
        assertEquals("int x;", b.getLines());
    }

    @Test
    void importNoPrependMultiLine() {
        String s1 = "int i = 0;\nint x = 5;\ndouble y = 3.2";
        String s2 = "int i = 0;\nint x = 5;\ndouble y = 3.2\n\n";
        Block b1 = new Block("1", s1);
        Block b2 = new Block("2", s2);

        assertEquals(s1, b1.getLines());
        assertEquals(s2, b2.getLines());
    }

    @Test
    void importBlockWithEmptyLines() {
        String s = "\tThis is a test\n\n\n\tFor mulitple lines";
        Block b = new Block("1", s);

        assertEquals(1, b.getTab());
        assertEquals("This is a test\n\n\nFor mulitple lines", b.getLines());
    }

    @Test
    void importInnerTabLarger() {
        String s = "\tfor (int i = 0; i < 10; i++) {\n\t\tprintf(\"%d\\n\", i);\n\t}";
        Block b = new Block("1", s);

        assertEquals(1, b.getTab());
        assertEquals("for (int i = 0; i < 10; i++) {\n\tprintf(\"%d\\n\", i);\n}", b.getLines());
    }
    @Test
    void importOuterTabLarger() {
        String s = "\t\t\tABC\n\tD\n\t\t\tEFG";
        Block b = new Block("1", s);

        assertEquals(1, b.getTab());
        assertEquals("\t\tABC\nD\n\t\tEFG", b.getLines());
    }
    @Test
    void emptyBlock() {
        Block b = new Block("1", "");
        assertEquals("", b.getLines());
    }
}