package lexer;

import exceptions.SyntaxError;
import tokens.Token;
import tokens.TokenType;

import java.io.IOException;

public class AlnumUnderscoreLexer {
    protected final CharReader charReader;

    public AlnumUnderscoreLexer(CharReader charReader) {
        this.charReader = charReader;
    }

    /**
     * Reads alphanumeric and underscore characters into a string and stores it in a token.
     * Grammar: ('_'|('a'-'z')|('A'-'Z'))('_'|('a'-'z')|('A'-'Z')|('0'-'9'))*
     *
     * @return a token containing a string of alphanumeric and underscore characters.
     * @throws IOException if the read operation causes an error.
     * @throws SyntaxError if there is an invalid character.
     */
    public Token read() throws IOException, SyntaxError {
        int c;

        // Check if the first character is end-of-stream or neither a letter nor '_'
        if (charReader.isEos(c = charReader.peek()) || (!Character.isAlphabetic(c) && c != '_')) {
            return null;
        }

        StringBuilder tokStr = new StringBuilder();
        boolean end = false;

        // Consume the character from the stream until it is a separator or a valid special character
        while (!charReader.isSep(c) && !end) {
            if (charReader.isAlnumUnderscore(c)) {
                tokStr.append((char) c);
                charReader.read();
            } else if (charReader.isSpecialChar(c)) {
                end = true;
            } else {
                throw new SyntaxError("Invalid character '" + c + "' after '" + tokStr + "'", charReader.getCurrLine());
            }
            c = charReader.peek();
        }

        // The string cannot be empty
        return new Token(tokStr.toString(), TokenType.UNKNOWN, charReader.getCurrLine());
    }
}
