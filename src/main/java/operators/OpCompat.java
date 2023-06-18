package operators;

import tokens.TokenType;

// Base class for operator compatibility
public abstract class OpCompat {
    protected final TokenType id;
    protected final OpCompatType compatType;

    public OpCompat(TokenType id, OpCompatType compatType) {
        this.id = id;
        this.compatType = compatType;
    }

    public TokenType getId() {
        return id;
    }

    public OpCompatType getCompatType() {
        return compatType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof OpCompat opCompat)) {
            return false;
        }
        return id == opCompat.id && compatType == opCompat.compatType;
    }
}
