import lexers.Lexer;
import exceptions.SyntaxError;
import tokens.Token;
import tokens.TokenType;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {
    private ArrayList<Token> extractToks(String input) throws SyntaxError, IOException {
        BufferedReader reader = new BufferedReader(new StringReader(input));
        Lexer lexer = new Lexer(reader);
        ArrayList<Token> tokList = new ArrayList<>();
        Token tok;
        while ((tok = lexer.consume()) != null && tok.getType() != TokenType.EOF) {
            tokList.add(tok);
        }
        return tokList;
    }

    @Test
    public void testValidInput1() {
        String input = "52+-(-25.)-(32.4-+.0e0)/.9*.";
        ArrayList<Token> expected = new ArrayList<>();
        expected.add(new Token("52", TokenType.INT_LITERAL));
        expected.add(new Token("+", TokenType.ADD));
        expected.add(new Token("-", TokenType.SUB));
        expected.add(new Token("(", TokenType.LPAREN));
        expected.add(new Token("-", TokenType.SUB));
        expected.add(new Token("25.0", TokenType.FLOAT_LITERAL));
        expected.add(new Token(")", TokenType.RPAREN));
        expected.add(new Token("-", TokenType.SUB));
        expected.add(new Token("(", TokenType.LPAREN));
        expected.add(new Token("32.4", TokenType.FLOAT_LITERAL));
        expected.add(new Token("-", TokenType.SUB));
        expected.add(new Token("+", TokenType.ADD));
        expected.add(new Token("0.0e0", TokenType.FLOAT_LITERAL));
        expected.add(new Token(")", TokenType.RPAREN));
        expected.add(new Token("/", TokenType.DIV));
        expected.add(new Token("0.9", TokenType.FLOAT_LITERAL));
        expected.add(new Token("*", TokenType.MULT));
        expected.add(new Token(".", TokenType.DOT));
        try {
            ArrayList<Token> actual = extractToks(input);
            assertEquals(expected, actual);
        } catch (SyntaxError | IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testValidInput2() {
        String input = "  var b=b +\t-.78e9+.5 *  a/a  *((2.e-1-67.+71e3*21)))\t";
        ArrayList<Token> expected = new ArrayList<>();
        expected.add(new Token("var", TokenType.VAR_DECL));
        expected.add(new Token("b", TokenType.ID));
        expected.add(new Token("=", TokenType.ASSIGNMENT));
        expected.add(new Token("b", TokenType.ID));
        expected.add(new Token("+", TokenType.ADD));
        expected.add(new Token("-", TokenType.SUB));
        expected.add(new Token("0.78e9", TokenType.FLOAT_LITERAL));
        expected.add(new Token("+", TokenType.ADD));
        expected.add(new Token("0.5", TokenType.FLOAT_LITERAL));
        expected.add(new Token("*", TokenType.MULT));
        expected.add(new Token("a", TokenType.ID));
        expected.add(new Token("/", TokenType.DIV));
        expected.add(new Token("a", TokenType.ID));
        expected.add(new Token("*", TokenType.MULT));
        expected.add(new Token("(", TokenType.LPAREN));
        expected.add(new Token("(", TokenType.LPAREN));
        expected.add(new Token("2.0e-1", TokenType.FLOAT_LITERAL));
        expected.add(new Token("-", TokenType.SUB));
        expected.add(new Token("67.0", TokenType.FLOAT_LITERAL));
        expected.add(new Token("+", TokenType.ADD));
        expected.add(new Token("71e3", TokenType.INT_LITERAL));
        expected.add(new Token("*", TokenType.MULT));
        expected.add(new Token("21", TokenType.INT_LITERAL));
        expected.add(new Token(")", TokenType.RPAREN));
        expected.add(new Token(")", TokenType.RPAREN));
        expected.add(new Token(")", TokenType.RPAREN));
        try {
            ArrayList<Token> actual = extractToks(input);
            assertEquals(expected, actual);
        } catch (SyntaxError | IOException e) {
            e.printStackTrace();
            fail();
        }
    }
}