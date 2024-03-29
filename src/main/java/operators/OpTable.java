package operators;

import tokens.TokenType;
import types.TypeInfo;
import types.TypeTable;

import java.util.HashMap;
import java.util.HashSet;

public class OpTable {
    private final HashMap<String, TokenType> opMap = new HashMap<>();
    private final HashSet<TokenType> prefixSet = new HashSet<>();
    private final HashSet<TokenType> infixSet = new HashSet<>();
    private final HashSet<TokenType> postfixSet = new HashSet<>();
    // Precedence table
    private final HashMap<TokenType, Integer> precedMap = new HashMap<>();
    // Associativity table, true means left-to-right, false means right-to-left
    private final HashMap<TokenType, Boolean> associativityMap = new HashMap<>();
    // This table stores the data type compatibility for each operator
    // When an operator is applied, it is used to check if the operands' data types are compatible
    // If they are, it finds the data type of the result after applying the operator
    private final HashMap<OpCompat, TypeInfo> compatMap = new HashMap<>();
    private final static OpTable INSTANCE = new OpTable();
    private static boolean init = false;

    private OpTable() {
    }

    /**
     * Initializes the only instance of OpTable if it has not been initialized and then returns it.
     *
     * @return an OpTable object.
     */
    public static OpTable getInstance() {
        if (!init) {
            // Add operators to table
            INSTANCE.opMap.put("+", TokenType.ADD);
            INSTANCE.opMap.put("-", TokenType.SUB);
            INSTANCE.opMap.put("*", TokenType.MULT);
            INSTANCE.opMap.put("/", TokenType.DIV);
            INSTANCE.opMap.put("%", TokenType.MOD);
            INSTANCE.opMap.put(".", TokenType.DOT);
            INSTANCE.opMap.put(":", TokenType.COLON);
            INSTANCE.opMap.put("=", TokenType.ASSIGNMENT);
            INSTANCE.opMap.put("as", TokenType.TYPE_CONV);
            INSTANCE.opMap.put("(", TokenType.LPAREN);
            INSTANCE.opMap.put(")", TokenType.RPAREN);
            INSTANCE.opMap.put("{", TokenType.LBRACKETS);
            INSTANCE.opMap.put("}", TokenType.RBRACKETS);
            INSTANCE.opMap.put(";", TokenType.SEMICOLON);

            // Initialize prefix table
            INSTANCE.prefixSet.add(TokenType.ADD);
            INSTANCE.prefixSet.add(TokenType.SUB);

            // Initialize infix table
            INSTANCE.infixSet.add(TokenType.ADD);
            INSTANCE.infixSet.add(TokenType.SUB);
            INSTANCE.infixSet.add(TokenType.MULT);
            INSTANCE.infixSet.add(TokenType.DIV);
            INSTANCE.infixSet.add(TokenType.MOD);
            INSTANCE.infixSet.add(TokenType.ASSIGNMENT);

            // Initialize postfix table
            INSTANCE.postfixSet.add(TokenType.TYPE_CONV);

            // Initialize precedence table
            INSTANCE.precedMap.put(TokenType.ADD, 10);
            INSTANCE.precedMap.put(TokenType.SUB, 10);
            INSTANCE.precedMap.put(TokenType.MULT, 20);
            INSTANCE.precedMap.put(TokenType.DIV, 20);
            INSTANCE.precedMap.put(TokenType.ASSIGNMENT, 5);

            // Initialize associativity table
            INSTANCE.associativityMap.put(TokenType.ADD, true);
            INSTANCE.associativityMap.put(TokenType.SUB, true);
            INSTANCE.associativityMap.put(TokenType.MULT, true);
            INSTANCE.associativityMap.put(TokenType.DIV, true);
            INSTANCE.associativityMap.put(TokenType.ASSIGNMENT, false);

            // Initialize operator type compatibility table
            TypeTable typeTable = TypeTable.getInstance();
            TypeInfo intType = TypeTable.INT;
            TypeInfo floatType = TypeTable.FLOAT;
            TypeInfo boolType = TypeTable.BOOL;
            // Unary operators
            INSTANCE.registerCompat(new UnOpCompat(TokenType.ADD, intType), intType);
            INSTANCE.registerCompat(new UnOpCompat(TokenType.ADD, floatType), floatType);
            INSTANCE.registerCompat(new UnOpCompat(TokenType.SUB, intType), intType);
            INSTANCE.registerCompat(new UnOpCompat(TokenType.SUB, floatType), floatType);
            // Binary operators
            INSTANCE.registerCompat(new BinOpCompat(TokenType.ADD, intType, intType), intType);
            INSTANCE.registerCompat(new BinOpCompat(TokenType.ADD, intType, floatType), floatType);
            INSTANCE.registerCompat(new BinOpCompat(TokenType.ADD, floatType, intType), floatType);
            INSTANCE.registerCompat(new BinOpCompat(TokenType.ADD, floatType, floatType), floatType);
            INSTANCE.registerCompat(new BinOpCompat(TokenType.SUB, intType, intType), intType);
            INSTANCE.registerCompat(new BinOpCompat(TokenType.SUB, intType, floatType), floatType);
            INSTANCE.registerCompat(new BinOpCompat(TokenType.SUB, floatType, intType), floatType);
            INSTANCE.registerCompat(new BinOpCompat(TokenType.SUB, floatType, floatType), floatType);
            INSTANCE.registerCompat(new BinOpCompat(TokenType.MULT, intType, intType), intType);
            INSTANCE.registerCompat(new BinOpCompat(TokenType.MULT, intType, floatType), floatType);
            INSTANCE.registerCompat(new BinOpCompat(TokenType.MULT, floatType, intType), floatType);
            INSTANCE.registerCompat(new BinOpCompat(TokenType.MULT, floatType, floatType), floatType);
            INSTANCE.registerCompat(new BinOpCompat(TokenType.DIV, intType, intType), intType);
            INSTANCE.registerCompat(new BinOpCompat(TokenType.DIV, intType, floatType), floatType);
            INSTANCE.registerCompat(new BinOpCompat(TokenType.DIV, floatType, intType), floatType);
            INSTANCE.registerCompat(new BinOpCompat(TokenType.DIV, floatType, floatType), floatType);
            INSTANCE.registerCompat(new BinOpCompat(TokenType.ASSIGNMENT, intType, intType), intType);
            INSTANCE.registerCompat(new BinOpCompat(TokenType.ASSIGNMENT, intType, floatType), intType);
            INSTANCE.registerCompat(new BinOpCompat(TokenType.ASSIGNMENT, floatType, intType), floatType);
            INSTANCE.registerCompat(new BinOpCompat(TokenType.ASSIGNMENT, floatType, floatType), floatType);
            INSTANCE.registerCompat(new BinOpCompat(TokenType.ASSIGNMENT, boolType, boolType), boolType);

            init = true;
        }
        return INSTANCE;
    }

    /**
     * Gets the operator's id associated with the given string.
     *
     * @param opStr a string associated with an operator.
     * @return a TokenType object as the operator's id if it exists, otherwise, return null.
     */
    public TokenType getId(String opStr) {
        return opMap.get(opStr);
    }

    /**
     * Checks if a token is a prefix operator.
     *
     * @param id operator's id.
     * @return true if the token is a prefix operator and false otherwise.
     */
    public boolean isPrefix(TokenType id) {
        return prefixSet.contains(id);
    }

    /**
     * Checks if a token is an infix operator.
     *
     * @param id operator's id.
     * @return true if the token is an infix operator and false otherwise.
     */
    public boolean isInfix(TokenType id) {
        return infixSet.contains(id);
    }

    /**
     * Checks if a token is a postfix operator.
     *
     * @param id operator's id.
     * @return true if the token is a postfix operator and false otherwise.
     */
    public boolean isPostfix(TokenType id) {
        return postfixSet.contains(id);
    }

    /**
     * Gets the precedence of the given operator.
     *
     * @param id operator's id.
     * @return an int value representing the operator precedence.
     */
    public int getPreced(TokenType id) {
        Integer preced = precedMap.get(id);
        return preced == null ? -1 : preced;
    }

    /**
     * Gets the associativity of the given operator.
     *
     * @param id operator's id.
     * @return true if the operator left-to-right, otherwise, return false.
     */
    public boolean getAssociativity(TokenType id) {
        return associativityMap.get(id);
    }

    /**
     * Compares the precedences of two operators.
     *
     * @param id1 the first operator's id.
     * @param id2 the second operator's id.
     * @return 1 if the first operator has higher priority, otherwise, return -1.
     */
    public int cmpPreced(TokenType id1, TokenType id2) {
        int preced1 = getPreced(id1);
        int preced2 = getPreced(id2);
        if (preced1 != preced2) {
            // If the two precedences are not the same,
            // return 1 if the first operator has higher precedence, otherwise, return -1
            return Integer.compare(preced1, preced2);
        }
        // Get the associativity of the first operator
        boolean leftToRight = getAssociativity(id1);
        // Return 1 if the first operator is left-to-right, otherwise, return -1
        return leftToRight ? 1 : -1;
    }

    /**
     * Maps an operator compatibility(OpCompat) object to a data type.
     *
     * @param opCompat       the object that stores operator compatibility.
     * @param resultDataType the result's data type after applying the operator.
     */
    private void registerCompat(OpCompat opCompat, TypeInfo resultDataType) {
        INSTANCE.compatMap.put(opCompat, resultDataType);
    }

    /**
     * Gets the result's data type after applying operator to operands with specific data types.
     *
     * @param opCompact the object that stores operator compatibility.
     * @return the result's data type.
     */
    public TypeInfo getCompatDataType(OpCompat opCompact) {
        return compatMap.get(opCompact);
    }
}
