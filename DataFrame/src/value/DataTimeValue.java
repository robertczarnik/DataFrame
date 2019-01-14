package value;
import java.util.Objects;

// format "2018-10-23 11:35"

public class DataTimeValue extends Value implements Cloneable {
    int year;
     int month;
     int day;
     int hour;
     int min;
     int sec;

    public String getDate() {
        return year+"-"+month+"-"+day;
    }

    public DataTimeValue(String s){
        String[] parts = s.split(" ");
        String[] data = new String[3];
        String[] time = new String[3];

        if(parts.length>0){
            data=parts[0].split("-");
        }

        if(parts.length>1){
            time=parts[1].split(":");
        }

        if(data[0]!=null)
            year = Integer.parseInt(data[0]);
        else
            year = -1;

        if(data[1]!=null)
            month = Integer.parseInt(data[1]);
        else
            month = -1;

        if(data[2]!=null)
            day = Integer.parseInt(data[2]);
        else
            day = -1;

        if(time[0]!=null)
            hour = Integer.parseInt(time[0]);
        else
            hour = -1;

        if(time[1]!=null)
            min = Integer.parseInt(time[1]);
        else
            min = -1;

        if(time[2]!=null)
            sec = Integer.parseInt(time[2]);
        else
            sec = -1;
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
        if(x instanceof  DataTimeValue) {
            return (this.year == (((DataTimeValue) x).year) && this.month == (((DataTimeValue) x).month) && this.day == (((DataTimeValue) x).day) && this.hour == (((DataTimeValue) x).hour) && this.min == (((DataTimeValue) x).min) && this.sec == (((DataTimeValue) x).sec));
        }else
            throw new IllegalArgumentException("Wrong type, required - DataTimeValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public boolean lte(Value x) {
        if(x instanceof  DataTimeValue) {
            if(this.year < (((DataTimeValue) x).year)){
                return true;
            }else if(this.year == (((DataTimeValue) x).year)){
                if(this.month < (((DataTimeValue) x).month)){
                    return true;
                }else if(this.month == (((DataTimeValue) x).month)){
                    if(this.day < (((DataTimeValue) x).day)) {
                        return true;
                    }else if(this.day == (((DataTimeValue) x).day)){
                        if(this.hour < (((DataTimeValue) x).hour)) {
                            return true;
                        }else if(this.hour == (((DataTimeValue) x).hour)){
                            if(this.min < (((DataTimeValue) x).min)) {
                                return true;
                            }else if(this.min == (((DataTimeValue) x).min)){
                                return this.sec <= (((DataTimeValue) x).sec);

                            }
                        }
                    }
                }
            }
            return false;
        }else
            throw new IllegalArgumentException("Wrong type, required - DataTimeValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public boolean gte(Value x) {
        if(x instanceof  DataTimeValue) {
            if(this.year > (((DataTimeValue) x).year)){
                return true;
            }else if(this.year == (((DataTimeValue) x).year)){
                if(this.month > (((DataTimeValue) x).month)){
                    return true;
                }else if(this.month == (((DataTimeValue) x).month)){
                    if(this.day > (((DataTimeValue) x).day)) {
                        return true;
                    }else if(this.day == (((DataTimeValue) x).day)){
                        if(this.hour > (((DataTimeValue) x).hour)) {
                            return true;
                        }else if(this.hour == (((DataTimeValue) x).hour)){
                            if(this.min > (((DataTimeValue) x).min)) {
                                return true;
                            }else if(this.min == (((DataTimeValue) x).min)){
                                return this.sec >= (((DataTimeValue) x).sec);

                            }
                        }
                    }
                }
            }
            return false;
        }else
            throw new IllegalArgumentException("Wrong type, required - DataTimeValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public boolean neq(Value x) {
        if(x instanceof  DataTimeValue) {
            return (this.year != (((DataTimeValue) x).year) || this.month != (((DataTimeValue) x).month) || this.day != (((DataTimeValue) x).day) || this.hour != (((DataTimeValue) x).hour) || this.min != (((DataTimeValue) x).min) || this.sec != (((DataTimeValue) x).sec));
        }else
            throw new IllegalArgumentException("Wrong type, required - DataTimeValue, found - " + x.getClass().getSimpleName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataTimeValue)) return false;
        DataTimeValue that = (DataTimeValue) o;
        return year == that.year &&
                month == that.month &&
                day == that.day &&
                hour == that.hour &&
                min == that.min &&
                sec == that.sec;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day, hour, min, sec);
    }

    @Override
    public String toString() {
        if(year!=-1){
            return  year +
                    "-" + month +
                    "-" + day;
        }

        if(hour!=-1){
            return " " + hour +
                    ":" + min +
                    ":" + sec;
        }
        return  "brak daty i czasu";
    }

    @Override
    public Value create(String s) {
        return new DataTimeValue(s);
    }

    @Override
    public DataTimeValue clone() throws CloneNotSupportedException {
        return (DataTimeValue) super.clone();
    }


}
