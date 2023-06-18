package lexers;

import operators.OpTable;
import tokens.Token;
import tokens.TokenType;

import java.io.IOException;

public class OpLexer {
    private final CharReader charReader;
    private final OpTable opTable = OpTable.getInstance();

    public OpLexer(CharReader charReader) {
        this.charReader = charReader;
    }

    /**
     * Reads an operator (can be multiple characters) and creates a token that stores the operator if the operation
     * succeeds.
     *
     * @return a token that stores the operator.
     * @throws IOException if the read operation causes an error.
     */
    public Token read() throws IOException {
        int c;
        StringBuilder tokStr = new StringBuilder();
        String tmpStr;
        TokenType tmpTokType, opId = TokenType.UNKNOWN;
        boolean end = false;

        while (!charReader.isEos(c = charReader.peek()) && !end) {
            tmpStr = tokStr.toString() + (char) c;
            // Find the token type corresponding to the string
            tmpTokType = opTable.getId(tmpStr);
            end = tmpTokType == null;
            if (!end) {
                tokStr.append((char) c);
                // Set the operator id
                opId = tmpTokType;
                charReader.read();
            }
        }
        if (tokStr.isEmpty()) {
            return null;
        }
        return new Token(tokStr.toString(), opId, charReader.getCurrLine());
    }
}
