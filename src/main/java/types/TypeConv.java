package types;

// Type conversion
public class TypeConv {
    // The source type
    private final TypeInfo srcType;
    // The destination type
    private final TypeInfo destType;
    // Whether the type conversion is explicit
    private final boolean explicit;

    public TypeConv(TypeInfo srcType, TypeInfo destType, boolean explicit) {
        this.srcType = srcType;
        this.destType = destType;
        this.explicit = explicit;
    }

    public TypeInfo getSrcType() {
        return srcType;
    }

    public TypeInfo getDestType() {
        return destType;
    }

    public boolean isExplicit() {
        return explicit;
    }

    @Override
    public int hashCode() {
        String hashStr = srcType.getId() + destType.getId();
        return hashStr.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TypeConv typeConv)) {
            return false;
        }
        return srcType.equals(typeConv.srcType) && destType.equals(typeConv.destType);
    }
}
