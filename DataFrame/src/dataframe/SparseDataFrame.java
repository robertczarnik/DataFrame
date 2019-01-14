package dataframe;
import exceptions.InconsistentTypeException;
import value.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SparseDataFrame extends DataFrame {

    public ArrayList<ArrayList<COOValue>> lista = new ArrayList<>(); // Lista list z obiektami COOvalue
    private int actualSize;
    private String hide;

    public SparseDataFrame() {
        actualSize=0;
        hide="";
    }

    public SparseDataFrame(ArrayList<String> kol, ArrayList<Class<? extends Value> > type, String hide){
        super();
        names.addAll(kol);
        types=type;
        this.hide=hide;
        this.actualSize=0;

        for (Class<? extends Value> ignored : types) {
            lista.add(new ArrayList<>()); // tworzy tyle kolumn ile jest podanych typów
        }
    }

    public SparseDataFrame(String file,ArrayList<Class<? extends Value> > type,String hide) throws IOException {
        super();
        this.hide=hide;
        this.actualSize=0;

        BufferedReader br=null;

        try {
            FileInputStream fstream = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;
            types = type;

            for (Class<? extends Value> ignored : types) {
                lista.add(new ArrayList<>()); // tworzy tyle kolumn ile jest podanych typów
            }

            strLine = br.readLine();
            names = new ArrayList<>(Arrays.asList(strLine.split(",")));


            String[] parts;
            while ((strLine = br.readLine()) != null) {
                parts = strLine.split(",");
                Object[] e = new Object[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    if (types.get(i) == IntegerValue.class)
                        e[i] = Integer.parseInt(parts[i]);
                    else if (types.get(i) == DoubleValue.class)
                        e[i] = Double.parseDouble(parts[i]);
                    else if (types.get(i) == FloatValue.class)
                        e[i] = Float.parseFloat(parts[i]);
                    else if (types.get(i) == StringValue.class)
                        e[i] = parts[i];
                    else if (types.get(i) == DataTimeValue.class)
                        e[i] = parts[i];
                }
                addRow(e);
            }



        } catch (FileNotFoundException e) {
            System.err.println("File does not exist");
        } catch (IOException e) {
            System.err.println("File exists, but there was IOException");
        } finally {
            if(br != null){
                try{
                    br.close();
                }
                catch (IOException e){
                    System.err.println("An IOException was caught");
                }
            }
        }
    } // domyslnie true

    public SparseDataFrame(DataFrame o1,String hide) {
        this.hide=hide;
        actualSize=0;
        names=o1.names;
        types=o1.types;
        int licznik=0;

        if(o1.lista.size()>0){
            for(int i=0;i<o1.lista.size();i++){
                lista.add(new ArrayList<>());
                for(int j=0;j<o1.lista.get(i).size();j++){
                    if(!o1.lista.get(i).get(j).toString().equals(hide)){
                        lista.get(i).add(new COOValue(licznik,o1.lista.get(i).get(j)));

                    }licznik++;
                }
                if(licznik>actualSize)
                    actualSize=licznik;
                licznik=0;

            }
        }
    }

    public void addRow(Object[] e) {
        for(int i=0;i<e.length;i++){
            if(!e[i].toString().equals(hide)){
                lista.get(i).add(new COOValue(actualSize,e[i]));
            }
        }
        actualSize++;
    }

    public int size(){
        return actualSize;
    } // mozna zwracać tablice intow z wymiarami kolumn

    public DataFrame toDense() throws IOException, InconsistentTypeException {
        DataFrame result = DataFrame.builder()
                .names(names)
                .types(types)
                .build();

        IntegerValue intV = new IntegerValue(0);
        DoubleValue doubleV =new DoubleValue(0.0);
        FloatValue floatV = new FloatValue(0.0f);
        StringValue stringV = new StringValue("");
        DataTimeValue datatimeV = new DataTimeValue("2018-11-07");

        if(types.size()>0){
                if (types.get(0) == IntegerValue.class){
                    for(int i=0;i<names.size();i++) {
                        for (int j = 0; j < actualSize; j++)
                            result.lista.get(i).add(intV.create(hide));
                    }
                    for(int i=0;i<this.names.size();i++) { // dodanie wartosci we wlasciwe miejsca
                        for (int j = 0; j < this.lista.get(i).size(); j++) {
                            result.lista.get(i).set(this.lista.get(i).get(j).getIndex(),intV.create(this.lista.get(i).get(j).getValue().toString()));
                        }
                    }
                }
                else if (types.get(0) == DoubleValue.class){
                    for(int i=0;i<names.size();i++) {
                        for (int j = 0; j < actualSize; j++)
                            result.lista.get(i).add(doubleV.create(hide));
                    }
                    for(int i=0;i<this.names.size();i++) { // dodanie wartosci we wlasciwe miejsca
                        for (int j = 0; j < this.lista.get(i).size(); j++) {
                            result.lista.get(i).set(this.lista.get(i).get(j).getIndex(),doubleV.create(this.lista.get(i).get(j).getValue().toString()));
                        }
                    }
                }
                else if (types.get(0) == FloatValue.class){
                    for(int i=0;i<names.size();i++) {
                        for (int j = 0; j < actualSize; j++)
                            result.lista.get(i).add(floatV.create(hide));
                    }
                    for(int i=0;i<this.names.size();i++) { // dodanie wartosci we wlasciwe miejsca
                        for (int j = 0; j < this.lista.get(i).size(); j++) {
                            result.lista.get(i).set(this.lista.get(i).get(j).getIndex(),floatV.create(this.lista.get(i).get(j).getValue().toString()));
                        }
                    }
                }
                else if (types.get(0) == StringValue.class){
                    for(int i=0;i<names.size();i++) {
                        for (int j = 0; j < actualSize; j++)
                            result.lista.get(i).add(stringV.create(hide));
                    }
                    for(int i=0;i<this.names.size();i++) { // dodanie wartosci we wlasciwe miejsca
                        for (int j = 0; j < this.lista.get(i).size(); j++) {
                            result.lista.get(i).set(this.lista.get(i).get(j).getIndex(),stringV.create(this.lista.get(i).get(j).getValue().toString()));
                        }
                    }
                }
                else if (types.get(0) == DataTimeValue.class){
                    for(int i=0;i<names.size();i++) {
                        for (int j = 0; j < actualSize; j++)
                            result.lista.get(i).add(datatimeV.create(hide));
                    }
                    for(int i=0;i<this.names.size();i++) { // dodanie wartosci we wlasciwe miejsca
                        for (int j = 0; j < this.lista.get(i).size(); j++) {
                            result.lista.get(i).set(this.lista.get(i).get(j).getIndex(),datatimeV.create(this.lista.get(i).get(j).getValue().toString()));
                        }
                    }
                }
        }

        return result;
    }

    public ArrayList<Value> get(String colname){
        for(int i=0;i<names.size();i++){
            if(names.get(i).equals(colname)){
                return (new ArrayList<Value>(lista.get(i)));
            }
        }
        return new ArrayList<>();
    }

    public SparseDataFrame get(String[] cols) { // deep copy
        SparseDataFrame result = new SparseDataFrame();
        int nrCol = 0;
        int ile = cols.length;
        result.names = new ArrayList<>();
        result.types = new ArrayList<>();

        for (String col : cols) {
            for (int i = 0; i < names.size(); i++) {
                if (col.equals(names.get(i))) {
                    result.lista.add(new ArrayList<>()); // dodaje kolumnę

                    for(int j=0;j<lista.get(i).size();j++) { // kopiuje po kolei każdy obiekt ('new' bardzo istotne)
                        COOValue kopia= new COOValue(lista.get(i).get(j).getIndex(),lista.get(i).get(j).getValue());
                        result.lista.get(nrCol).add(kopia);
                    }

                    result.names.set(nrCol,names.get(i));
                    result.types.add(types.get(i));

                    nrCol++;
                }
            }
        }

        return result;
    }

    public SparseDataFrame iloc(int i){
        SparseDataFrame result = new SparseDataFrame(names,types,hide);
        Object [] wiersz = new Object[lista.size()];

        for(int j=0;j<lista.size();j++)
            wiersz[j]=lista.get(j).get(i).getValue();

        result.addRow(wiersz);
        return result;
    }

    public SparseDataFrame iloc(int from, int to){
        SparseDataFrame result = new SparseDataFrame(names,types,hide);

        for(int i=from;i<=to;i++){
            Object [] wiersz = new Object[lista.size()];

            for(int j=0;j<lista.size();j++)
                wiersz[j]=lista.get(j).get(i).getValue();

            result.addRow(wiersz);
        }

        return result;
    }
}
