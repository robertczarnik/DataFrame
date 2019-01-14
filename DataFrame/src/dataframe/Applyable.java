package dataframe;
import exceptions.InconsistentTypeException;

import java.io.IOException;

public interface Applyable  {
    DataFrame apply(DataFrame x) throws IOException, InconsistentTypeException;
}
