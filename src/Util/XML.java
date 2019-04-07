package Util;

public class XML {
    public static String escapeXMLAttribute(String input) {
        return input;
//        if (input == null) {
//            return null;
//        }
//
//        StringBuilder sb = new StringBuilder();
//
//        for (char c : input.toCharArray()) {
//            switch (c) {
//                case '<': sb.append("&lt;"); break;
//                case '>': sb.append("&gt;"); break;
//                case '&': sb.append("&amp;"); break;
//                case '\'': sb.append("&apos;"); break;
//                case '"': sb.append("&quot;"); break;
//                default: sb.append(c);
//            }
//        }
//        return sb.toString();
    }

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
