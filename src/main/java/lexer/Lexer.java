package lexer;

import exceptions.SyntaxError;
import tokens.Token;
import tokens.TokenType;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayDeque;

public class Lexer {
    private final CharReader charReader;
    private final AlnumUnderscoreLexer alnumUnderscoreLexer;
    private final NumLexer numLexer;
    private final KeywordLexer keywordLexer;
    private final TypeLexer typeLexer;
    private final OpLexer opLexer;
    private final ArrayDeque<Token> tokBuff = new ArrayDeque<>();

    public Lexer(BufferedReader reader) {
        charReader = new CharReader(reader);
        alnumUnderscoreLexer = new AlnumUnderscoreLexer(charReader);
        numLexer = new NumLexer(charReader);
        keywordLexer = new KeywordLexer(charReader);
        typeLexer = new TypeLexer(charReader);
        opLexer = new OpLexer(charReader);
    }

    /**
     * Looks ahead to and removes the next token from the buffer.
     *
     * @return the next token in the buffer.
     * @throws SyntaxError if there is a syntax error.
     * @throws IOException if the read operation causes an IO error.
     */
    public Token consume() throws SyntaxError, IOException {
        Token tok = lookahead();
        tokBuff.removeFirst();
        return tok;
    }

    /**
     * Looks ahead to the next token without removing it from the stream.
     *
     * @return the next token in the buffer.
     * @throws SyntaxError if there is a syntax error.
     * @throws IOException if the read operation causes an IO error.
     */
    public Token lookahead() throws SyntaxError, IOException {
        // Reads from the token buffer before extracting characters from the stream
        if (!tokBuff.isEmpty()) {
            return tokBuff.peekFirst();
        }
        Token tok;
        // Skip the white spaces
        charReader.skipSpaces();
        // Check if the token is EOF
        if (charReader.isEos(charReader.peek())) {
            tok = new Token(null, TokenType.EOF);
            tokBuff.addLast(tok);
            return tok;
        }
        // Check if the token is a keyword
        tok = keywordLexer.read();
        if (tok != null) {
            tokBuff.addLast(tok);
            return tok;
        }
        // Check if the token is a data type
        tok = typeLexer.read();
        if (tok != null) {
            tokBuff.addLast(tok);
            return tok;
        }
        // Check if the token is a number
        tok = numLexer.read();
        if (tok != null) {
            tokBuff.addLast(tok);
            return tok;
        }
        // Check if the token is an operator
        tok = opLexer.read();
        if (tok != null) {
            tokBuff.addLast(tok);
            return tok;
        }
        // Check if the token is id
        tok = alnumUnderscoreLexer.read();
        if (tok != null) {
            tok.setType(TokenType.ID);
            tokBuff.addLast(tok);
            return tok;
        }
        // Cannot read the next token
        throw new SyntaxError("Unable to get next token because of invalid syntax at '" +
                (char) charReader.peek() + "'", charReader.getCurrLine());
    }
}
