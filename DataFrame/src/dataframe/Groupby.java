package dataframe;

import exceptions.InconsistentTypeException;

import java.io.IOException;

public interface Groupby {
    DataFrame max() throws IOException, InconsistentTypeException;
    DataFrame min() throws IOException, InconsistentTypeException;
    DataFrame sum() throws IOException, InconsistentTypeException;
    DataFrame mean() throws IOException, InconsistentTypeException; // srednia arytmetyczna
    DataFrame std() throws IOException, InconsistentTypeException; // odchylenie standarode
    DataFrame var() throws IOException, InconsistentTypeException; // wariancja
    DataFrame apply(Applyable x) throws IOException, InconsistentTypeException;
}
