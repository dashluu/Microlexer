package lexer;

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
     * Reads a string and stores it in a new token if it matches the given string. Otherwise, it puts everything
     * that has been read back to the buffer.
     *
     * @param strToMatch the string to match.
     * @param tokType    the type of the token to assign if one is present.
     * @return a token containing the string if the operation succeeds and null otherwise.
     * @throws IOException if there is an error while reading.
     */
    private Token readStr(String strToMatch, TokenType tokType) throws IOException {
        int c;
        int i = 0;
        boolean end = false;
        StringBuilder buffer = new StringBuilder();

        while (i < strToMatch.length() && !charReader.isEos(c = charReader.peek()) && !end) {
            end = (char) c != strToMatch.charAt(i);
            if (!end) {
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
            return null;
        }

        return new Token(strToMatch, tokType, charReader.getCurrLine());
    }

    /**
     * Reads a sequence of digits, adds them to a string and creates a token that contains
     * the string if successful.
     * Grammar: ('0'-'9')+
     *
     * @return a token containing a sequence of digits.
     * @throws IOException if the read operation causes an error.
     */
    private Token readDigits() throws IOException {
        int c;
        StringBuilder tokStr = new StringBuilder();

        while (!charReader.isEos(c = charReader.peek()) && Character.isDigit(c)) {
            tokStr.append((char) c);
            charReader.read();
        }

        if (tokStr.isEmpty()) {
            return null;
        }

        return new Token(tokStr.toString(), TokenType.INT_LITERAL, charReader.getCurrLine());
    }

    /**
     * Reads a floating-point number and stores it in a new token if one exists.
     * Grammar: ('0'-'9')+|('0'-'9')+'.'('0'-'9')*|('0'-'9')*'.'('0'-'9')+
     *
     * @return a token containing the floating-point number.
     * @throws IOException if the read operation causes an error.
     */
    private Token readNum() throws IOException {
        StringBuilder tokStr = new StringBuilder();

        // Reads the exponent part
        Token expTok = readDigits();
        boolean missingExp = expTok == null;
        if (missingExp) {
            tokStr.append("0");
        } else {
            tokStr.append(expTok.getValue());
        }

        // Reads '.'
        Token decPtTok = readStr(".", TokenType.UNKNOWN);
        boolean missingDecPt = decPtTok == null;
        if (!missingDecPt) {
            tokStr.append(".");
        }

        if (missingExp && missingDecPt) {
            // Missing both exponent and decimal point
            return null;
        }

        // Reads the fraction part if there is a decimal point
        if (!missingDecPt) {
            Token fracTok = readDigits();
            if (fracTok == null) {
                if (expTok == null) {
                    // Both the fraction and exponent are missing
                    // Put back the decimal point since this is not a valid floating number
                    charReader.putBack(".");
                    return null;
                } else {
                    tokStr.append("0");
                }
            } else {
                tokStr.append(fracTok.getValue());
            }
        }

        TokenType tokType = missingDecPt ? TokenType.INT_LITERAL : TokenType.FLOAT_LITERAL;
        return new Token(tokStr.toString(), tokType, charReader.getCurrLine());
    }

    /**
     * Reads a scientific floating-point number and stores it in a new token if one exists.
     * Grammar: (('0'-'9')+|('0'-'9')+'.'('0'-'9')*|('0'-'9')*'.'('0'-'9')+)('e'('+'|'-')?('0'-'9')+)?
     *
     * @return a token containing the scientific floating-point number.
     * @throws SyntaxError if the numeric expression is invalid.
     * @throws IOException if the read operation causes an error.
     */
    public Token read() throws IOException, SyntaxError {
        // Read a floating-point number
        Token tmpTok = readNum();
        if (tmpTok == null) {
            return null;
        }

        int c;
        StringBuilder tokStr = new StringBuilder();
        TokenType tokType = tmpTok.getType();
        tokStr.append(tmpTok.getValue());

        // Read 'e'
        tmpTok = readStr("e", TokenType.UNKNOWN);
        if (tmpTok == null) {
            if (charReader.isEos(c = charReader.peek()) || charReader.isSpace(c) || charReader.isSpecialChar(c) && c != '.') {
                return new Token(tokStr.toString(), tokType, charReader.getCurrLine());
            } else {
                throw new SyntaxError("Invalid numeric expression after '" + tokStr + "'", charReader.getCurrLine());
            }
        }
        tokStr.append("e");

        // Read +/-
        tmpTok = readStr("+", TokenType.UNKNOWN);
        if (tmpTok == null) {
            // Reads '-' if '+' is not present
            tmpTok = readStr("-", TokenType.UNKNOWN);
            if (tmpTok != null) {
                tokStr.append(tmpTok.getValue());
            }
        } else {
            tokStr.append(tmpTok.getValue());
        }

        // Read sequence of digits combined with +/- to form the power after 'e'
        tmpTok = readDigits();
        if (tmpTok == null) {
            throw new SyntaxError("Invalid numeric expression after '" + tokStr + "'", charReader.getCurrLine());
        }
        tokStr.append(tmpTok.getValue());
        return new Token(tokStr.toString(), TokenType.FLOAT_LITERAL, charReader.getCurrLine());
    }
}
