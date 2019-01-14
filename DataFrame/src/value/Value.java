package value;

import exceptions.InconsistentTypeException;

public abstract class Value {
    public abstract String toString();
    public abstract Value add(Value x);
    public abstract Value sub(Value x);
    public abstract Value mul(Value x);
    public abstract Value div(Value x);
    public abstract Value pow(Value x);
    public abstract boolean eq(Value x);
    public abstract boolean lte(Value x);
    public abstract boolean gte(Value x);
    public abstract boolean neq(Value x);
    public abstract boolean equals(Object other);
    public abstract int hashCode();
    public abstract Value create(String s) throws InconsistentTypeException;
}
