package types;

import java.util.HashMap;

public class TypeTable {
    public final static TypeInfo INT = new TypeInfo("int", 4);
    public final static TypeInfo FLOAT = new TypeInfo("float", 4);
    public final static TypeInfo BOOL = new TypeInfo("bool", 1);
    // String-to-type map
    private final HashMap<String, TypeInfo> typeMap = new HashMap<>();
    // Type conversion map
    private final HashMap<TypeConv, TypeConv> typeConvMap = new HashMap<>();
    private final static TypeTable INSTANCE = new TypeTable();
    private static boolean init = false;

    private TypeTable() {
    }

    /**
     * Initializes the only instance of TypeTable if it has not been initialized and then returns it.
     *
     * @return a TypeTable object.
     */
    public static TypeTable getInstance() {
        if (!init) {
            // Add types to table
            INSTANCE.registerType(INT);
            INSTANCE.registerType(FLOAT);
            INSTANCE.registerType(BOOL);

            // Add pairs of types that can be converted from one to another
            INSTANCE.registerTypeConv(new TypeConv(INT, INT, true));
            INSTANCE.registerTypeConv(new TypeConv(INT, FLOAT, true));
            INSTANCE.registerTypeConv(new TypeConv(FLOAT, INT, true));
            INSTANCE.registerTypeConv(new TypeConv(FLOAT, FLOAT, true));

            init = true;
        }
        return INSTANCE;
    }

    /**
     * Adds a new data type to the table.
     *
     * @param type TypeInfo object that carries type data.
     */
    public void registerType(TypeInfo type) {
        typeMap.put(type.getId(), type);
    }

    /**
     * Gets the type associated with the given id.
     *
     * @param id id of the type.
     * @return a TypeInfo object associated with the given id.
     */
    public TypeInfo getType(String id) {
        return typeMap.get(id);
    }

    /**
     * Adds a new type conversion object to the type conversion table.
     *
     * @param typeConv TypeConv object that contains type conversion information.
     */
    private void registerTypeConv(TypeConv typeConv) {
        typeConvMap.put(typeConv, typeConv);
    }

    /**
     * Gets a type conversion object using the source and destination data type.
     *
     * @param srcType  the source data type.
     * @param destType the destination data type.
     * @return a TypeConv object if the two types are convertible and null otherwise.
     */
    public TypeConv getTypeConv(TypeInfo srcType, TypeInfo destType) {
        // dummy object
        TypeConv typeConv = new TypeConv(srcType, destType, false);
        return typeConvMap.get(typeConv);
    }
}
