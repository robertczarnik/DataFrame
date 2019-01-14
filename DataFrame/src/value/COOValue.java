package value;

import java.util.Objects;

public class COOValue extends Value implements Cloneable {
    private final int index;
    private final Object value;

    public COOValue(int index,Object value){
        this.value=value;
        this.index=index;
    }

    public Object getValue() {
        return value;
    }

    public  int getIndex() {
        return index;
    }

    public COOValue(String s){
        String[] parts = s.split(".");
        index=Integer.parseInt(parts[0]);
        value=parts[1];
    }

    @Override
    public Value add(Value x) {
        return null;
    }

    @Override
    public Value sub(Value x) {
        return null;
    }

    @Override
    public Value mul(Value x) {
        return null;
    }

    @Override
    public Value div(Value x) {
        return null;
    }

    @Override
    public Value pow(Value x) {
        return null;
    }

    @Override
    public boolean eq(Value x) {
        return false;
    }

    @Override
    public boolean lte(Value x) {
        return false;
    }

    @Override
    public boolean gte(Value x) {
        return false;
    }

    @Override
    public boolean neq(Value x) {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof COOValue)) return false;
        COOValue cooValue = (COOValue) o;
        return getIndex() == cooValue.getIndex() &&
                Objects.equals(getValue(), cooValue.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIndex(), getValue());
    }

    @Override
    public String toString() {
        return index +
                "." + value;
    }

    @Override
    public Value create(String s) {
        return new COOValue(s);
    }

    @Override
    public COOValue clone() throws CloneNotSupportedException {
        return (COOValue) super.clone();
    }
}
