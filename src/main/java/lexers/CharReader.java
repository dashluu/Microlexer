package lexers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayDeque;

public class CharReader {
    private final ArrayDeque<Integer> buff = new ArrayDeque<>();
    private final BufferedReader reader;
    private final static int EOS = -1;
    private final static String SPECIAL_CHARS = "(){}+-*/%~!&|<>=,.;:_";
    private int currLine = 1;

    public CharReader(BufferedReader reader) {
        this.reader = reader;
    }

    /**
     * Checks if the input character is end-of-stream.
     *
     * @param c the input character.
     * @return true if it is end-of-stream and false otherwise.
     */
    public boolean isEos(int c) {
        return c == EOS;
    }

    /**
     * Determines if the character is a space.
     *
     * @param c the character to be checked.
     * @return true if the character is a space and false otherwise.
     */
    public boolean isSpace(int c) {
        return Character.isWhitespace(c);
    }

    /**
     * Determines if the character is an alphanumeric or an underscore.
     *
     * @param c the character to be checked.
     * @return true if the character is an alphanumeric or an underscore and false otherwise.
     */
    public boolean isAlnumUnderscore(int c) {
        return Character.isAlphabetic(c) || Character.isDigit(c) || c == '_';
    }

    /**
     * Determines if the character is a valid special character.
     *
     * @param c the character to be checked.
     * @return true if the character is a valid special character and false otherwise.
     */
    public boolean isSpecialChar(int c) {
        return SPECIAL_CHARS.indexOf(c) >= 0;
    }

    /**
     * Determines if the character is a valid separator.
     *
     * @param c the character to be checked.
     * @return true if the character is a valid separator and false otherwise.
     */
    public boolean isSep(int c) {
        return c == EOS || isSpace(c) || c == ';';
    }

    /**
     * Gets the current line number.
     *
     * @return an integer as the current line number.
     */
    public int getCurrLine() {
        return currLine;
    }

    /**
     * Skips the spaces until a non-space character is encountered.
     *
     * @throws IOException if the read operation causes an IO error.
     */
    public void skipSpaces() throws IOException {
        int c;
        while (!isEos(c = peek()) && isSpace(c)) {
            read();
        }
    }

    /**
     * Peeks without extracting a character from the internal buffer. If the internal buffer is empty, read a character
     * from the stream into the buffer.
     *
     * @return the peeked character(as an int).
     * @throws IOException if there is an error while reading from the stream.
     */
    public int peek() throws IOException {
        if (buff.isEmpty()) {
            buff.addLast(reader.read());
        }
        // This buffer will never be empty
        assert !buff.isEmpty();
        return buff.peekFirst();
    }

    /**
     * Peeks and extracts the first character in the internal buffer.
     *
     * @return the extracted character if there is any.
     * @throws IOException if there is an error while reading from the stream.
     */
    public int read() throws IOException {
        int c = peek();
        if (buff.pop() == '\n') {
            // If '\n' is popped, increment the current line number
            ++currLine;
        }
        return c;
    }

    /**
     * Puts back a valid string into the internal buffer.
     *
     * @param str the string to be put back.
     */
    public void putBack(String str) throws IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("Null string cannot be put back into the lexer buffer");
        }
        if (str.isEmpty()) {
            throw new IllegalArgumentException("Empty string cannot be put back into the lexer buffer");
        }
        int c;
        for (int i = str.length() - 1; i >= 0; --i) {
            c = str.charAt(i);
            if (c == '\n') {
                // If '\n' is pushed, decrement the current line number
                --currLine;
            }
            buff.addFirst(c);
        }
    }
}
