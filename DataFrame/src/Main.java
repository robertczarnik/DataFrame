import dataframe.DataFrame;
import dataframe.DataFrameMultiThreads;
import exceptions.InconsistentTypeException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InconsistentTypeException {

        String sciezka= "D:\\!!Studia\\Semestr III\\Java\\Pliki\\groupby.csv";

        DataFrame df = DataFrame.builder()
                .file(sciezka)
                .build();

        DataFrameMultiThreads dfMT = DataFrameMultiThreads.builder()
                .file(sciezka)
                .build();


        long startTime = System.nanoTime();
        DataFrame.Grouping gr = df.groupby(new String[] {"id"});
        DataFrame grupa1 = gr.max();
        long endTime = System.nanoTime();
        System.out.println("Jednowatkowy: "+(endTime - startTime)/1000000);

        startTime = System.nanoTime();
        DataFrameMultiThreads.Grouping gr2 = dfMT.groupby(new String[] {"id"});
        DataFrame grupa2 = gr2.max();
        endTime = System.nanoTime();
        System.out.println("Wielowatkowy: "+(endTime - startTime)/1000000);

        /*for (int i=0;i < grupa1.lista.get(0).size();i++){ // dane
            for(int j=0;j<grupa1.lista.size();j++){
                System.out.print(grupa1.lista.get(j).get(i).toString()+" ");
            }
            System.out.println(" ");
        }*/

        /*System.out.println("\n\n");

        for (int i=0;i < grupa2.lista.get(0).size();i++){ // dane
            for(int j=0;j<grupa2.lista.size();j++){
                System.out.print(grupa2.lista.get(j).get(i).toString()+" ");
            }
            System.out.println(" ");
        }*/
    }
}
