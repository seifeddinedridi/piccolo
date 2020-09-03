package org.piccolo.parsing.context;

public class Cursor {

    private int currentLine;
    private int currentColumn;
    private int previousLineColumnStartIndex;
    private char previousChar;

    private Cursor(int currentLine, int currentColumn, int previousLineColumnStartIndex, char previousChar) {
        this.currentLine = currentLine;
        this.currentColumn = currentColumn;
        this.previousLineColumnStartIndex = previousLineColumnStartIndex;
        this.previousChar = previousChar;
    }

    public Cursor() {
        this.currentLine = 0;
        this.currentColumn = 0;
        this.previousLineColumnStartIndex = 0;
        this.previousChar = 0;
    }

    public void nextColumn(char currentChar) {
        if (System.lineSeparator().equals(String.valueOf(currentChar)) || System.lineSeparator()
            .equals(String.valueOf(new char[]{previousChar, currentChar}))) {
            nextLine();
        }
        previousChar = currentChar;
        currentColumn++;
    }

    public void previousColumn() {
        currentColumn--;
    }

    public void nextLine() {
        previousLineColumnStartIndex = currentColumn;
        currentLine++;
    }

    public int getCurrentLine() {
        return currentLine + 1;
    }

    public int getCurrentColumn() {
        return currentColumn;
    }

    public int getColumnOffset() {
        return currentColumn - previousLineColumnStartIndex;
    }

    public Cursor copy() {
        return new Cursor(currentLine, currentColumn, previousLineColumnStartIndex, previousChar);
    }

    public void copyFrom(Cursor other) {
        this.currentColumn = other.currentColumn;
        this.currentLine = other.currentLine;
        this.previousLineColumnStartIndex = other.previousLineColumnStartIndex;
        this.previousChar = other.previousChar;
    }
}
