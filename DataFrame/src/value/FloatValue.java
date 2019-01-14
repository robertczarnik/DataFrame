package value;

import java.util.Objects;

public class FloatValue extends Value implements Cloneable {
    Float wartosc;

    private FloatValue(String s){
        wartosc=Float.parseFloat(s);
    }

    public FloatValue(float s){
        wartosc=s;
    }

    @Override
    public Value add(Value x) {
        if(x instanceof  FloatValue) {
            this.wartosc = this.wartosc + ((FloatValue) x).wartosc;
            return this;
        } else if(x instanceof  IntegerValue){
            this.wartosc += ((IntegerValue)x).wartosc;
            return this;
        }else
            throw new IllegalArgumentException("Wrong type, required - FloatValue or IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public Value sub(Value x) {
        if(x instanceof  FloatValue) {
            this.wartosc = this.wartosc - ((FloatValue) x).wartosc;
            return this;
        } else if(x instanceof  IntegerValue){
            this.wartosc -= ((IntegerValue)x).wartosc;
            return this;
        }else
            throw new IllegalArgumentException("Wrong type, required - FloatValue or IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public Value mul(Value x) {
        if(x instanceof  FloatValue) {
            this.wartosc = this.wartosc * ((FloatValue) x).wartosc;
            return this;
        } else if(x instanceof  IntegerValue){
            this.wartosc *= ((IntegerValue)x).wartosc;
            return this;
        }else
            throw new IllegalArgumentException("Wrong type, required - FloatValue or IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public Value div(Value x) {
        if(x instanceof  FloatValue) {
            this.wartosc = this.wartosc / ((FloatValue) x).wartosc;
            return this;
        } else if(x instanceof  IntegerValue){
            this.wartosc /= ((IntegerValue)x).wartosc;
            return this;
        }else
            throw new IllegalArgumentException("Wrong type, required - FloatValue or IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public Value pow(Value x) {
        if(x instanceof  FloatValue) {
            this.wartosc = (float)Math.pow(this.wartosc,((FloatValue) x).wartosc);
            return this;
        } else if(x instanceof  IntegerValue){
            this.wartosc = (float)Math.pow(this.wartosc,((IntegerValue) x).wartosc);
            return this;
        }else
            throw new IllegalArgumentException("Wrong type, required - FloatValue or IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public boolean eq(Value x) {
        if(x instanceof  FloatValue) {
            return this.wartosc.equals(((FloatValue) x).wartosc);
        }else
            throw new IllegalArgumentException("Wrong type, required - FloatValue or IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public boolean lte(Value x) {
        if(x instanceof  FloatValue) {
            return this.wartosc <= (((FloatValue) x).wartosc);
        }else
            throw new IllegalArgumentException("Wrong type, required - FloatValue or IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public boolean gte(Value x) {
        if(x instanceof  FloatValue) {
            return this.wartosc >= (((FloatValue) x).wartosc);
        }else
            throw new IllegalArgumentException("Wrong type, required - FloatValue or IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public boolean neq(Value x) {
        if(x instanceof  FloatValue) {
            return !this.wartosc.equals(((FloatValue) x).wartosc);
        }else
            throw new IllegalArgumentException("Wrong type, required - FloatValue or IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FloatValue)) return false;
        FloatValue that = (FloatValue) o;
        return Objects.equals(wartosc, that.wartosc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wartosc);
    }

    @Override
    public String toString() {
        return wartosc.toString();
    }

    @Override
    public Value create(String s) {
        return new FloatValue(s);
    }

    @Override
    public FloatValue clone() throws CloneNotSupportedException {
        return (FloatValue) super.clone();
    }

    public Float getWartosc() {
        return wartosc;
    }
}
