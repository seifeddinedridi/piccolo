package org.piccolo.context;

public class Cursor {

    private int currentLine;
    private int currentColumn;
    private int currentLineColumnStartIndex;
    private char previousChar;

    private Cursor(int currentLine, int currentColumn, int currentLineColumnStartIndex, char previousChar) {
        this.currentLine = currentLine;
        this.currentColumn = currentColumn;
        this.currentLineColumnStartIndex = currentLineColumnStartIndex;
        this.previousChar = previousChar;
    }

    public Cursor() {
        this.currentLine = 0;
        this.currentColumn = 0;
        this.currentLineColumnStartIndex = 0;
        this.previousChar = 0;
    }

    public void nextColumn(char currentChar) {
        if (System.lineSeparator().equals(String.valueOf(currentChar))
                || System.lineSeparator().equals(String.valueOf(new char[]{previousChar, currentChar}))) {
            nextLine();
        }
        previousChar = currentChar;
        currentColumn++;
    }

    public void previousColumn() {
        currentColumn--;
    }

    private void nextLine() {
        currentLineColumnStartIndex = currentColumn;
        currentLine++;
    }

    public int getCurrentLine() {
        return currentLine + 1;
    }

    public int getCurrentColumn() {
        return currentColumn;
    }

    public int getColumnOffset() {
        return currentColumn - currentLineColumnStartIndex;
    }

    public Cursor copy() {
        return new Cursor(currentLine, currentColumn, currentLineColumnStartIndex, previousChar);
    }

    public void copyFrom(Cursor other) {
        this.currentColumn = other.currentColumn;
        this.currentLine = other.currentLine;
        this.currentLineColumnStartIndex = other.currentLineColumnStartIndex;
        this.previousChar = other.previousChar;
    }
}
