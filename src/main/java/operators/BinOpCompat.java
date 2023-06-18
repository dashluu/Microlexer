package operators;

import tokens.TokenType;
import types.TypeInfo;

// Binary operator compatibility between two data types
public class BinOpCompat extends OpCompat {
    private final TypeInfo leftType;
    private final TypeInfo rightType;

    public BinOpCompat(TokenType id, TypeInfo leftType, TypeInfo rightType) {
        super(id, OpCompatType.BINARY);
        this.leftType = leftType;
        this.rightType = rightType;
    }

    public TypeInfo getLeftType() {
        return leftType;
    }

    public TypeInfo getRightType() {
        return rightType;
    }

    @Override
    public int hashCode() {
        String hashStr = id + leftType.getId() + rightType.getId();
        return hashStr.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj) || !(obj instanceof BinOpCompat binOpTypeCompat)) {
            return false;
        }
        return leftType.equals(binOpTypeCompat.leftType) &&
                rightType.equals(binOpTypeCompat.rightType);
    }
}
