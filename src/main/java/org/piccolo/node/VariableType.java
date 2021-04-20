package org.piccolo.node;

public enum VariableType {
    VOID("void"),
    INTEGER("int"),
    STRING("string");

    private final String type;

    VariableType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static VariableType fromString(String type) {
        for (var o : VariableType.values()) {
            if (o.type.equals(type)) {
                return o;
            }
        }
        throw new IllegalArgumentException("Incorrect enum value: " + type);
    }
}
