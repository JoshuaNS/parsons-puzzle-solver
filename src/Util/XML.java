package Util;

public class XML {

    public static String unescapeXMLAttribute(String input) {
        if (input == null) {
            return null;
        }

        String output = input.replaceAll("&lt;", "<");
        output = output.replaceAll("&gt;", ">");
        output = output.replaceAll("&amp;", "&");
        output = output.replaceAll("&apos;", "'");
        output = output.replaceAll("&quot;", "\"");

        return output;
    }
}
