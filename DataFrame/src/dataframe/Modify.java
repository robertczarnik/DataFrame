package dataframe;

import value.*;
import java.util.ArrayList;

public interface Modify {
    void add(Value x); // dodawanie wartosci Value
    void add(String col); // dodawanie kolumny z tej samej DataFrame
    void add(ArrayList<Value> x, String colName); // dodawanie kolumny z innej DataFrame
    void sub(Value x);
    void sub(String col);
    void sub(ArrayList<Value> x, String colName);
    void mul(Value x);
    void mul(String col);
    void mul(ArrayList<Value> x, String colName);
    void div(Value x);
    void div(String col);
    void div(ArrayList<Value> x, String colName);
    void pow(Value x);
    void pow(String col);
    void pow(ArrayList<Value> x, String colName);
}
