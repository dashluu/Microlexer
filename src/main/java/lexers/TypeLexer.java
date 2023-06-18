package lexers;

import exceptions.SyntaxError;
import tokens.Token;
import tokens.TokenType;
import types.TypeTable;

import java.io.IOException;

public class TypeLexer extends AlnumUnderscoreLexer {
    private final TypeTable typeTable = TypeTable.getInstance();

    public TypeLexer(CharReader charReader) {
        super(charReader);
    }

    /**
     * Reads a data type token.
     *
     * @return a token that stores a data type.
     * @throws IOException if the read operation causes an error.
     * @throws SyntaxError if there is a syntax error.
     */
    @Override
    public Token read() throws IOException, SyntaxError {
        // Use the super class to read an alphanumeric-underscore token
        Token tok = super.read();
        if (tok == null) {
            return null;
        }
        if (typeTable.getType(tok.getValue()) == null) {
            // If the type is not found, put the read token back
            charReader.putBack(tok.getValue());
            return null;
        }
        tok.setType(TokenType.TYPE_ID);
        return tok;
    }
}
