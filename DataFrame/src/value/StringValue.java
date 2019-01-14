package value;
import java.util.Objects;

public class StringValue extends Value implements Cloneable {
    String wartosc;

    public StringValue(String s){
        wartosc=s;
    }

    @Override
    public Value add(Value x) {
        if(x instanceof  StringValue) {
            this.wartosc = this.wartosc + ((StringValue)x).wartosc;
            return this;
        }else
            throw new IllegalArgumentException("Wrong type, required - StringValue, found - " + x.getClass().getSimpleName());
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
        if(x instanceof  StringValue) {
            return this.wartosc.length() == (((StringValue) x).wartosc.length());
        }else
            throw new IllegalArgumentException("Wrong type, required - StringValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public boolean lte(Value x) {
        if(x instanceof  StringValue) {
            return this.wartosc.length() <= (((StringValue) x).wartosc.length());
        }else
            throw new IllegalArgumentException("Wrong type, required - StringValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public boolean gte(Value x) {
        if(x instanceof  StringValue) {
            return this.wartosc.length() >= (((StringValue) x).wartosc.length());
        }else
            throw new IllegalArgumentException("Wrong type, required - StringValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public boolean neq(Value x) {
        if(x instanceof  StringValue) {
            return this.wartosc.length() != (((StringValue) x).wartosc.length());
        }else
            throw new IllegalArgumentException("Wrong type, required - StringValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StringValue)) return false;
        StringValue that = (StringValue) o;
        return Objects.equals(wartosc, that.wartosc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wartosc);
    }

    @Override
    public String toString() {
        return wartosc;
    }

    @Override
    public Value create(String s) {
        return new StringValue(s);
    }

    @Override
    public StringValue clone() throws CloneNotSupportedException {
        return (StringValue) super.clone();
    }

    public String getWartosc() {
        return wartosc;
    }
}
