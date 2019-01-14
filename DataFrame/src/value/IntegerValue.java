package value;
import java.util.Objects;

public class IntegerValue extends Value implements Cloneable{
    Integer wartosc;

    private IntegerValue(String s){
        wartosc=Integer.parseInt(s);
    }

    public IntegerValue(int s){
        wartosc=s;
    }

    @Override
    public Value add(Value x) {
        if(x instanceof IntegerValue){
            this.wartosc = this.wartosc + ((IntegerValue)x).wartosc;
            return this;
        }else
            throw new IllegalArgumentException("Wrong type, required - IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public Value sub(Value x) {
        if(x instanceof IntegerValue){
            this.wartosc = this.wartosc - ((IntegerValue) x).wartosc;
            return this;
        }else
            throw new IllegalArgumentException("Wrong type, required - IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public Value mul(Value x) {
        if(x instanceof IntegerValue){
            this.wartosc = this.wartosc * ((IntegerValue) x).wartosc;
            return this;
        }else
            throw new IllegalArgumentException("Wrong type, required - IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public Value div(Value x) { // liczba jest zaokraglona do liczby calkowitej zeby zachowac spojnosc
        if(x instanceof IntegerValue){
            this.wartosc = Math.toIntExact(Math.round( (double)this.wartosc / ((IntegerValue) x).wartosc));
            return this;
        }else
            throw new IllegalArgumentException("Wrong type, required - IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public Value pow(Value x) { // przy podniesieniu do potegi ujemnej wynik jest zaokraglony do liczby calkowitej
        if(x instanceof IntegerValue){
            if(((IntegerValue) x).wartosc > 0) {
                for (int i = 1; i < ((IntegerValue) x).wartosc; i++)
                    this.wartosc *= this.wartosc;
                return this;
            }else if(((IntegerValue) x).wartosc == 0) {
                this.wartosc = 1;
                return this;
            }else {
                for (int i = 1; i < ((IntegerValue) x).wartosc; i++)
                    this.wartosc *= this.wartosc;
                this.wartosc = Math.toIntExact(Math.round(1.0 / this.wartosc));
                return this;
            }

        }else
            throw new IllegalArgumentException("Wrong type, required - IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public boolean eq(Value x) {
        if(x instanceof IntegerValue){
            return this.wartosc.equals(((IntegerValue) x).wartosc);
        }else
            throw new IllegalArgumentException("Wrong type, required - IntegerValue, found - " + x.getClass().getSimpleName());

    }

    @Override
    public boolean lte(Value x) {
        if(x instanceof IntegerValue){
            return this.wartosc <= (((IntegerValue) x).wartosc);
        }else
            throw new IllegalArgumentException("Wrong type, required - IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public boolean gte(Value x) {
        if(x instanceof IntegerValue){
            return this.wartosc >= (((IntegerValue) x).wartosc);
        }else
            throw new IllegalArgumentException("Wrong type, required - IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public boolean neq(Value x) {
        if(x instanceof IntegerValue){
            return !this.wartosc.equals(((IntegerValue) x).wartosc);
        }else
            throw new IllegalArgumentException("Wrong type, required - IntegerValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntegerValue)) return false;
        IntegerValue that = (IntegerValue) o;
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
        return new IntegerValue(s);
    }

    @Override
    public IntegerValue clone() throws CloneNotSupportedException {
        return (IntegerValue) super.clone();
    }

    public Integer getWartosc() {
        return wartosc;
    }
}
