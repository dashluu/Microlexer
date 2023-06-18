package lexer;

import exceptions.SyntaxError;
import keywords.KeywordTable;
import tokens.Token;
import tokens.TokenType;

import java.io.IOException;

public class KeywordLexer extends AlnumUnderscoreLexer {
    private final KeywordTable keywordTable = KeywordTable.getInstance();

    public KeywordLexer(CharReader charReader) {
        super(charReader);
    }

    /**
     * Reads a keyword containing only alphanumerics and underscores.
     *
     * @return a token that stores the keyword.
     * @throws IOException if the read operation causes an error.
     * @throws SyntaxError if there is a syntax error.
     */
    @Override
    public Token read() throws IOException, SyntaxError {
        // Use the super class to read an alphanumeric-underscore token
        Token tok = super.read();
        TokenType keywordId;
        if (tok == null) {
            return null;
        }
        if ((keywordId = keywordTable.getId(tok.getValue())) == null) {
            // If the keyword is not found, put the read token back
            charReader.putBack(tok.getValue());
            return null;
        }
        tok.setType(keywordId);
        return tok;
    }
}
