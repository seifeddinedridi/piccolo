package org.piccolo.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParsingUtils {

    private static final List<String> VALID_TYPES = Arrays.asList("int", "string", "void");
    private static final List<String> RESERVED_KEYWORDS = new ArrayList<>(VALID_TYPES);

    static {
        RESERVED_KEYWORDS.add("return");
    }

    public static boolean isType(String token) {
        return VALID_TYPES.contains(token);
    }

    public static boolean isIdentifier(String token) {
        return token.matches("^[a-zA-Z]\\w*");
    }

    public static boolean isReservedKeyword(String token) {
        return RESERVED_KEYWORDS.contains(token);
    }

    public static boolean isSeparator(char c) {
        return c == ' ' || c == ',' || c == '(';
    }

    public static boolean isExpressionTerminator(char c) {
        return c == ';';
    }

    public static boolean isNumber(String token) {
        try {
            Integer.parseInt(token);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isOperator(char currentChar) {
        return currentChar == '=' || currentChar == '+' || currentChar == '-' || currentChar == '*'
                || currentChar == '/'
                || currentChar == '%';
    }

    public static boolean canSkip(char character) {
        return character == ' ' || character == '\n' || character == '\r';
    }

    public static boolean isValidExpressionCharacter(char character) {
        return character != '{' && character != '}';
    }
}
