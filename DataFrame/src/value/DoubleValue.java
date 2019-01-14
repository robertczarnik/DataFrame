package value;

import java.util.Objects;

public class DoubleValue extends Value implements Cloneable {
    Double wartosc;

    private DoubleValue(String s) {
        wartosc=Double.parseDouble(s);
    }

    public DoubleValue(double s){
        wartosc=s;
    }

    @Override
    public Value add(Value x) {
        if(x instanceof  DoubleValue) {
            this.wartosc = this.wartosc + ((DoubleValue) x).wartosc;
            return this;
        } else if(x instanceof  IntegerValue){
            this.wartosc += ((IntegerValue)x).wartosc;
            return this;
        } else if(x instanceof  FloatValue){
            this.wartosc += ((FloatValue)x).wartosc;
            return this;
        }else
            throw new IllegalArgumentException("Wrong type, required - DoubleValue or FloatValue or IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public Value sub(Value x) {
        if(x instanceof  DoubleValue) {
            this.wartosc = this.wartosc - ((DoubleValue) x).wartosc;
            return this;
        } else if(x instanceof  IntegerValue){
            this.wartosc -= ((IntegerValue)x).wartosc;
            return this;
        } else if(x instanceof  FloatValue){
            this.wartosc -= ((FloatValue)x).wartosc;
            return this;
        }else
            throw new IllegalArgumentException("Wrong type, required - DoubleValue or FloatValue or IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public Value mul(Value x) {
        if(x instanceof  DoubleValue) {
            this.wartosc = this.wartosc * ((DoubleValue) x).wartosc;
            return this;
        } else if(x instanceof  IntegerValue){
            this.wartosc *= ((IntegerValue)x).wartosc;
            return this;
        } else if(x instanceof  FloatValue){
            this.wartosc *= ((FloatValue)x).wartosc;
            return this;
        }else
            throw new IllegalArgumentException("Wrong type, required - DoubleValue or FloatValue or IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public Value div(Value x) {
        if(x instanceof  DoubleValue) {
            this.wartosc = this.wartosc / ((DoubleValue) x).wartosc;
            return this;
        } else if(x instanceof  IntegerValue){
            this.wartosc /= ((IntegerValue)x).wartosc;
            return this;
        } else if(x instanceof  FloatValue){
            this.wartosc /= ((FloatValue)x).wartosc;
            return this;
        }else
            throw new IllegalArgumentException("Wrong type, required - DoubleValue or FloatValue or IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public Value pow(Value x) {
        if(x instanceof  DoubleValue) {
            this.wartosc = Math.pow(this.wartosc,((DoubleValue) x).wartosc);
            return this;
        } else if(x instanceof  IntegerValue){
            this.wartosc = Math.pow(this.wartosc,((IntegerValue) x).wartosc);
            return this;
        } else if(x instanceof  FloatValue){
            this.wartosc = Math.pow(this.wartosc,((FloatValue) x).wartosc);
            return this;
        }else
            throw new IllegalArgumentException("Wrong type, required - DoubleValue or FloatValue or IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public boolean eq(Value x) {
        if(x instanceof  DoubleValue) {
            return this.wartosc.equals(((DoubleValue) x).wartosc);
        }else
            throw new IllegalArgumentException("Wrong type, required - DoubleValue or FloatValue or IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public boolean lte(Value x) {
        if(x instanceof  DoubleValue) {
            return this.wartosc <= (((DoubleValue) x).wartosc);
        }else
            throw new IllegalArgumentException("Wrong type, required - DoubleValue or FloatValue or IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public boolean gte(Value x) {
        if(x instanceof  DoubleValue) {
            return this.wartosc >= (((DoubleValue) x).wartosc);
        }else
            throw new IllegalArgumentException("Wrong type, required - DoubleValue or FloatValue or IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public boolean neq(Value x) {
        if(x instanceof  DoubleValue) {
            return !this.wartosc.equals(((DoubleValue) x).wartosc);
        }else
            throw new IllegalArgumentException("Wrong type, required - DoubleValue or FloatValue or IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoubleValue)) return false;
        DoubleValue that = (DoubleValue) o;
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
        return new DoubleValue(s);
    }

    @Override
    public DoubleValue clone() throws CloneNotSupportedException {
        return (DoubleValue) super.clone();
    }

    public Double getWartosc() {
        return wartosc;
    }
}
