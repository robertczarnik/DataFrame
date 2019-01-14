package dataframe;

import exceptions.InconsistentTypeException;

import java.io.IOException;

public class Operacja implements Applyable {

    public Operacja(){

    }

    public DataFrame apply(DataFrame x) throws IOException, InconsistentTypeException {
        DataFrame result = DataFrame.builder()
                .names(x.names)
                .types(x.types)
                .build();

        /* implementacja jakies operacji */

        return result;
    }
}
