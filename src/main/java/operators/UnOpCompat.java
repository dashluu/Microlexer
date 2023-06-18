package operators;

import tokens.TokenType;
import types.TypeInfo;

// Unary operator compatibility
public class UnOpCompat extends OpCompat {
    private final TypeInfo operandType;

    public UnOpCompat(TokenType id, TypeInfo operandType) {
        super(id, OpCompatType.UNARY);
        this.operandType = operandType;
    }

    public TypeInfo getOperandType() {
        return operandType;
    }

    @Override
    public int hashCode() {
        String hashStr = id + operandType.getId();
        return hashStr.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj) || !(obj instanceof UnOpCompat unOpTypeCompat)) {
            return false;
        }
        return operandType.equals(unOpTypeCompat.operandType);
    }
}
