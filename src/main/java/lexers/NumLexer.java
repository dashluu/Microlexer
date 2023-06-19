package lexers;

import exceptions.SyntaxError;
import tokens.Token;
import tokens.TokenType;

import java.io.IOException;

public class NumLexer {
    private final CharReader charReader;

    public NumLexer(CharReader charReader) {
        this.charReader = charReader;
    }

    /**
     * Reads a string from the stream to check if it matches the given string. Otherwise, it puts everything that has
     * been read back to the buffer.
     *
     * @param strToMatch the string to match.
     * @return true if the read string matches the given string and false otherwise.
     * @throws IOException if there is an error while reading.
     */
    private boolean readStr(String strToMatch) throws IOException {
        int c;
        int i = 0;
        boolean end = false;
        StringBuilder buffer = new StringBuilder();
        while (i < strToMatch.length() && !charReader.isEos(c = charReader.peek()) && !end) {
            end = (char) c != strToMatch.charAt(i);
            if (!end) {
                // If the current character matches, update the temp buffer
                buffer.append((char) c);
                ++i;
                charReader.read();
            }
        }
        if (!strToMatch.contentEquals(buffer)) {
            // Put back what has been read if what's in the buffer does not match the expected string
            if (!buffer.isEmpty()) {
                charReader.putBack(buffer.toString());
            }
            return false;
        }
        return true;
    }

    /**
     * Reads a sequence of digits.
     * Grammar: ('0'-'9')+
     *
     * @return a string containing a sequence of digits.
     * @throws IOException if the read operation causes an error.
     */
    private String readDigits() throws IOException {
        int c;
        StringBuilder digits = new StringBuilder();
        while (!charReader.isEos(c = charReader.peek()) && Character.isDigit(c)) {
            digits.append((char) c);
            charReader.read();
        }
        if (digits.isEmpty()) {
            return null;
        }
        return digits.toString();
    }

    /**
     * Reads the fraction part in a numeric expression.
     *
     * @param fillFrac true if the fraction must be filled with 0s when it is missing and false otherwise.
     * @return a string containing the fraction and null otherwise.
     * @throws IOException if the read operation causes an error.
     */
    private String readFrac(boolean fillFrac) throws IOException {
        if (!readStr(".")) {
            return null;
        }
        String digits = readDigits();
        if (digits == null) {
            if (fillFrac) {
                digits = "0";
            } else {
                charReader.putBack(".");
                return null;
            }
        }
        return "." + digits;
    }

    /**
     * Reads the optional exponent in a numeric expression.
     *
     * @return a string containing the optional exponent.
     * @throws IOException if the read operation causes an error.
     * @throws SyntaxError if there is a syntax error.
     */
    private String readExp() throws IOException, SyntaxError {
        // Read e
        if (!readStr("e")) {
            return null;
        }

        StringBuilder exp = new StringBuilder("e");

        // Read +/-
        if (readStr("+")) {
            exp.append("+");
        } else if (readStr("-")) {
            exp.append("-");
        }

        // Read digits
        String digits = readDigits();
        if (digits == null) {
            throw new SyntaxError("Expected a sequence of digits after '" + exp.charAt(exp.length() - 1) + "'",
                    charReader.getCurrLine());
        }

        exp.append(digits);
        return exp.toString();
    }

    /**
     * Reads a numeric expression.
     *
     * @return a token whose string is the numeric expression.
     * @throws IOException if the read operation causes an error.
     * @throws SyntaxError if there is a syntax error.
     */
    public Token read() throws IOException, SyntaxError {
        StringBuilder tokStr = new StringBuilder();

        // Read sequence of digits
        String digits = readDigits();
        boolean hasDigits = digits != null;

        if (hasDigits) {
            tokStr.append(digits);
        } else {
            tokStr.append("0");
        }

        // Read fraction part
        String frac = readFrac(hasDigits);
        boolean isFp = frac != null;
        if (isFp) {
            tokStr.append(frac);
        } else if (!hasDigits) {
            return null;
        }

        // Read optional exponent
        String exp = readExp();
        if (exp != null) {
            tokStr.append(exp);
        }

        TokenType literalType = (isFp ? TokenType.FLOAT_LITERAL : TokenType.INT_LITERAL);
        return new Token(tokStr.toString(), literalType, charReader.getCurrLine());
    }
}
