import dataframe.DataFrame;
import dataframe.DataFrameMultiThreads;
import exceptions.InconsistentTypeException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InconsistentTypeException {
        DataFrame df = DataFrame.builder()
                .file(a[0])
                .build();

        DataFrame grupa1 = df.groupby(new String[] {"id"}).max();

        for (int i=0;i < grupa1.lista.get(0).size();i++){ // dane
            for(int j=0;j<grupa1.lista.size();j++){
                System.out.print(grupa1.lista.get(j).get(i).toString()+" ");
            }
            System.out.println(" ");
        }
    }
}
