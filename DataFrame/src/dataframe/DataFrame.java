package dataframe;

import exceptions.InconsistentTypeException;
import value.*;
import java.io.*;
import java.util.*;

/**
 * Klasa implementujaca ramki danych
 * @author Robert
 */
public class DataFrame {

    public ArrayList<ArrayList<Value>> lista; // Lista list z obiektami
    public ArrayList<String> names;
    protected ArrayList<Class<? extends Value> > types;

    private DataFrame(DataFrameBuilder builder){
        this.lista=builder.lista;
        this.names=builder.names;
        this.types=builder.types;
    } // konstrukotr dla buildera

    protected DataFrame() {

    } // dla klasy sdf

    /**
     * wewnetrzna klasa implemwntująca wzorzec Builder
     */
    public static class DataFrameBuilder{
        private ArrayList<ArrayList<Value>> lista;
        private ArrayList<String> names;
        private ArrayList<Class<? extends Value> > types;
        private String file;
        private boolean header;
        private IntegerValue intV;
        private DoubleValue doubleV;
        private FloatValue floatV;
        private StringValue stringV;
        private DataTimeValue datatimeV;

        private DataFrameBuilder(){
            lista  = new ArrayList<>();
            types = new ArrayList<>();
            names = new ArrayList<>();
            header = true;

            intV = new IntegerValue(0);
            doubleV = new DoubleValue(0.0);
            floatV = new FloatValue(0.0f);
            stringV = new StringValue("");
            datatimeV = new DataTimeValue("2018-11-07");
        }

        public DataFrameBuilder names(ArrayList<String> names){
            this.names.addAll(names);
            return this;
        }

        public DataFrameBuilder types(ArrayList<Class<? extends Value> > types){
            this.types.addAll(types);
            return this;
        }

        public DataFrameBuilder file(String file){
            this.file=file;
            return this;
        }

        public DataFrameBuilder header(boolean header){
            this.header=header;
            return this;
        }

        private boolean isInt(String str) {
            int oneMinus = 0;

            for (char c : str.toCharArray())
            {
                if (!Character.isDigit(c) && c!='-')
                    return false;

                if(c=='-')
                    oneMinus++;
            }
            return oneMinus < 2;
        }

        private boolean isDouble(String str) {
            int oneMinus = 0;
            int oneDot = 0;
            for (char c : str.toCharArray())
            {
                if (!Character.isDigit(c) && c!='-' && c!='.')
                    return false;

                if(c=='-')
                    oneMinus++;

                if(c=='.')
                    oneDot++;
            }
            return oneMinus < 2 && oneDot < 2;
        }

        private boolean isDateTime(String str) {
            return str.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}") || str.matches("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}");
        }

        public DataFrame build() throws IOException, InconsistentTypeException {
            for (Class<? extends Value> ignore : types) {
                lista.add(new ArrayList<>()); // tworzy tyle kolumn ile jest podanych typów (typy zawsze musza byc podane)
            }

            if(file!=null) {
                BufferedReader br = null;
                int row = 1;
                int k = 0;
                try {
                    FileInputStream fstream = new FileInputStream(file);
                    br = new BufferedReader(new InputStreamReader(fstream));

                    String strLine;
                    if (header) { // pierwszy wiersz to nazwy kolumn domyślnie header=true
                        strLine = br.readLine();
                        names = new ArrayList<>(Arrays.asList(strLine.split(",")));
                        row++;
                    }


                    String[] parts;


                    if (types.size() < 1) { // sprobuj rozpoznac typy bo nie zostaly podane
                        if ((strLine = br.readLine()) != null) {
                            parts = strLine.split(",");

                            for (int i = 0; i < parts.length; i++) {
                                lista.add(new ArrayList<>()); // tworzy tyle kolumn ile jest kolumn
                            }


                            for (int i = 0; i < parts.length; i++) {
                                if (isInt(parts[i])) {
                                    types.add(IntegerValue.class);
                                    lista.get(i).add((intV.create(parts[i])));
                                } else if (isDouble(parts[i])) {
                                    types.add(DoubleValue.class);
                                    lista.get(i).add((doubleV.create(parts[i])));
                                } else if (isDateTime(parts[i])) {
                                    types.add(DataTimeValue.class);
                                    lista.get(i).add((datatimeV.create(parts[i])));
                                } else {
                                    types.add(StringValue.class);
                                    lista.get(i).add((stringV.create(parts[i])));
                                }
                            }
                            row++;
                        }
                    }



                    while ((strLine = br.readLine()) != null) {
                        parts = strLine.split(",");
                        for (k = 0; k < parts.length; k++) { // tworze obiekty przy użyciu jednej pustej instancji każdej klasy widocznej w tej klasie wewnetrznej
                            if (types.get(k) == IntegerValue.class)
                                lista.get(k).add((intV.create(parts[k])));
                            else if (types.get(k) == DoubleValue.class)
                                lista.get(k).add((doubleV.create(parts[k])));
                            else if (types.get(k) == FloatValue.class)
                                lista.get(k).add((floatV.create(parts[k])));
                            else if (types.get(k) == StringValue.class)
                                lista.get(k).add((stringV.create(parts[k])));
                            else if (types.get(k) == DataTimeValue.class)
                                lista.get(k).add((datatimeV.create(parts[k])));

                        }
                        row++;
                    }
                }catch(Exception e) {
                    throw new InconsistentTypeException("Inconsistent Type",names.get(k),row);
                }


                finally {
                    if(br != null){
                        br.close();

                    }
                }

            }else if(names==null)
                throw new IllegalArgumentException("DataFrame must have columns names");


            return  new DataFrame(this);
        }
    }

    public static DataFrameBuilder builder() {
        return new DataFrame.DataFrameBuilder();
    }

    /**
     * dodaje wiersz do DataFrame
     * @param e tablica z obiektami które chcemy dodać
     */
    public void addRow(Value[] e) {
        for(int i=0;i<e.length;i++){
            lista.get(i).add(e[i]);
        }
    }

    /**
     * @return liczba wierszy w ramce danych
     */
    public int size(){
        return lista.get(0).size();
    }

    /**
     * wyswietla dataframe
     */
    public void show()
    {
        if(this.lista.get(0) != null) {
            for (int i = 0; i < this.lista.get(0).size(); i++) { // dane
                for (int j = 0; j < this.lista.size(); j++) {
                    System.out.print(this.lista.get(j).get(i).toString() + " ");
                }
                System.out.println(" ");
            }
        }
        else
            System.out.println("brak danych do wysietlenia");
    }

    /**
     * pobranie jednej kolumny z ramki danych
     * @param colname nazwa kolumny ktora chcemy pobrac
     * @return lista obiektow reprezentujacych zadana kolumne
     */
    public ArrayList<Value> get(String colname){
        int searchCol = 0;
        while(!names.get(searchCol).equals(colname)){
            searchCol++;
        }
        return lista.get(searchCol);
    } //shallow copy

    /**
     * pobranie kilku kolumn z ramki danych
     * @param cols nazwy kolumn
     * @return ramka danych z zadanymi kolumnami
     */
    public DataFrame get(ArrayList<String> cols) throws IOException, InconsistentTypeException{ // deep copy
        DataFrame result = DataFrame.builder()
                .names(cols)
                .types(types)
                .build();

        for (String col : cols) {
            for (int i = 0; i < names.size(); i++) {
                if (col.equals(names.get(i))) {
                    result.lista.set(i,new ArrayList<>());
                    for(int k=0;k<lista.get(i).size();k++){
                        if(types.get(i) == DoubleValue.class)
                            result.lista.get(i).add(new DoubleValue(((DoubleValue)lista.get(i).get(k)).getWartosc()));
                        else if(types.get(i) == IntegerValue.class)
                            result.lista.get(i).add(new IntegerValue(((IntegerValue)lista.get(i).get(k)).getWartosc()));
                        else if(types.get(i) == StringValue.class)
                            result.lista.get(i).add(new StringValue(((StringValue)lista.get(i).get(k)).getWartosc()));
                        else if(types.get(i) == DataTimeValue.class)
                            result.lista.get(i).add(new DataTimeValue(((DataTimeValue)lista.get(i).get(k)).getDate()));
                        else if(types.get(i) == FloatValue.class)
                            result.lista.get(i).add(new FloatValue(((FloatValue)lista.get(i).get(k)).getWartosc()));
                    }
                }
            }
        }

        for (int i = 0; i < types.size(); i++) { // usunięcie zbędnych pustych kolumn
            if(result.lista.get(i).size()==0) {
                result.lista.remove(i);
            }
        }

        return result;
    }

    /**
     * @param i index wiersza
     * @return DataFrame z jednym wierszem
     * @throws IOException w build()
     */
    public DataFrame iloc(int i) throws IOException, InconsistentTypeException {
        DataFrame result = DataFrame.builder()
                .types(types)
                .names(names)
                .build();

        Value [] wiersz = new Value[lista.size()];

        for(int j=0;j<lista.size();j++)
            wiersz[j]=lista.get(j).get(i);

        result.addRow(wiersz);
        return result;
    }

    public Value[] ilocValue(int i) {
        Value [] wiersz = new Value[lista.size()];

        for(int j=0;j<lista.size();j++)
            wiersz[j]=lista.get(j).get(i);

        return wiersz;
    }




    /**
     * @param from index od
     * @param to index do
     * @return DataFrame z wierszami od from do to
     * @throws IOException w build()
     */
    public DataFrame iloc(int from, int to) throws IOException, InconsistentTypeException {
        DataFrame result = DataFrame.builder()
                .types(types)
                .names(names)
                .build();


        for(int i=from;i<=to;i++){
            Value [] wiersz = new Value[lista.size()];

            for(int j=0;j<lista.size();j++)
                wiersz[j]=lista.get(j).get(i);

            result.addRow(wiersz);
        }

        return result;
    }

    /**
     * Metoda ktora grupuje DataFrame według podanych kolumn
     * @param colnames kolumny według ktorych grupuje
     * @return obiekt(z LinkedList) klasy ktora implementuje Groupby
     */
    public Grouping groupby(String[] colnames) throws IOException, InconsistentTypeException{
        LinkedList<DataFrame> search = new LinkedList<>();
        DataFrame copyBaseDf = this.get(names); // kopia DataFrame - this
        search.add(copyBaseDf);
        //copyBaseDf.lista.get(0).set(0,new StringValue("Z"));

        LinkedList<DataFrame> result = new LinkedList<>();
        int punkt; // punkt od ktorego szukam czy juz takie cos wystapiło


        boolean flag; // czy wiersz zostal dodany

        for(String kolumna : colnames){ // przejscie po wszystkich podanych kolumnach
            for (int j=0;j<names.size();j++) { // wyszukiwanie podanej kolumny
                if (names.get(j).equals(kolumna)) { // znalezlismy kolumne wedlug której grupujemy
                    result = new LinkedList<>();


                    for(int q=0;q<search.size();q++){ // przy wiekszej ilosci kolumn bedą robione grupy z grup
                        punkt = result.size(); // zeby zachowac kolejnosc nadrzednej grupy

                        for(int i=0;i<search.get(q).lista.get(0).size();i++){ // przejscie po wszystkich elementach tej kolumny
                            flag = false;

                            /*if(search.get(q).lista.get(j).get(i).getClass() != this.types.get(j)) { // sprawdzenieczy czy ktorys z elementow kolumny wedlug ktorej grupujemy nie jest niespojny
                                throw new InconsistentTypeException("Incosistaent type ",kolumna,i);
                            }*/

                            for(int k=punkt;k<result.size();k++){ // przejscie po DataFrameach(grupach) w liście
                                if(result.get(k).lista.get(j).get(0).equals(search.get(q).lista.get(j).get(i))){ // jesli pierwszy element jakiejs DataFrame(grupy) jest równy mojemu elementowi to moge ten wiersz dodac do tej DataFrame
                                    for(int z=0;z<result.get(k).lista.size();z++) // dodanie calego wiersza
                                        result.get(k).lista.get(z).add(search.get(q).lista.get(z).get(i));

                                    flag = true;
                                    break;

                                }
                            }

                            if(!flag){
                                DataFrame obiekt = DataFrame.builder() // tworze DataFrame(grupe) z jednym wierszem
                                        .names(names)
                                        .types(types)
                                        .build();

                                for(int z=0;z<obiekt.lista.size();z++) // dodanie calego wiersza
                                    obiekt.lista.get(z).add(search.get(q).lista.get(z).get(i));

                                result.add(obiekt);
                            }

                        }
                    }

                    search = null;
                    search = new LinkedList<>(result);

                    break;
                }
            }
        }
        return new Grouping(result,colnames);
    }

    /**
     * Wewnetrzna klasa ktora implementuje mozliwe sposoby grupowania danych
     */
    public class Grouping implements Groupby{
        private LinkedList<DataFrame> groups;
        private String[] cols; // kolumny wzgledem ktorych grupowalismy
        private HashSet<Integer> colRemovement = new HashSet<>();

        public Grouping(){

        }

        private Grouping(LinkedList<DataFrame> o1, String[] names){
            groups = new LinkedList<>(o1);
            cols = names.clone();
        }

        private void checkInconsistency (int idGroup,int idCol ,int idRow) throws InconsistentTypeException { // sprawdzenieczy czy wiersz(idRow) jest spojny z typem kolumny(idCol)
            if(groups.get(idGroup).lista.get(idCol).get(idRow).getClass() != DataFrame.this.types.get(idCol))
                throw new InconsistentTypeException("Inconsistent type ",groups.get(idGroup).names.get(idCol),idRow);
        }

        private boolean skipCol (int idGroup,int idCol){ //sprawdzenie czy dana kolumna jest kolumna wedlug ktorej grupowalismy
            boolean flag=false;
            for(String col : cols){
                if(col.equals(groups.get(idGroup).names.get(idCol))){
                    flag=true;
                    break;
                }
            }

            return flag;
        }

        private void removeCols(DataFrame result){ // usuniecie kolumny i powiazanych z nia wlasnosci
            for (Integer id : colRemovement) {
                result.lista.remove((int) id);
                result.names.remove((int) id);
                result.types.remove((int) id);
            }
        }


        public DataFrame max() throws IOException, InconsistentTypeException {
            DataFrame result = DataFrame.builder()
                    .types(types)
                    .names(names)
                    .build();

            Value maxValue;

            for(int i=0;i<groups.size();i++){ // dla kazdej grupy
                for(int j=0;j<groups.get(i).lista.size();j++){ //dla każej kolumny za wyjatkiem tych względem których grupowaliśmy

                    if(skipCol(i,j)) { // pominiecie kolumny wedlug ktorej grupowalismy
                        result.lista.get(j).add(groups.get(i).lista.get(j).get(0));
                        continue;
                    }

                    checkInconsistency(i,j,0);
                    maxValue=groups.get(i).lista.get(j).get(0);

                    for(int k=1;k<groups.get(i).lista.get(j).size();k++){ // przejscie przez wszyskie elementy
                        checkInconsistency(i,j,k);
                        if(!maxValue.gte(groups.get(i).lista.get(j).get(k))) // jesli maxValue nie jest wieksze badz rowne niz ten element to maxValue staje sie tym elementem
                            maxValue = groups.get(i).lista.get(j).get(k);

                    }
                    result.lista.get(j).add(maxValue);
                }
            }
            return result;
        }

        public DataFrame min() throws IOException, InconsistentTypeException {
            DataFrame result = DataFrame.builder()
                    .types(types)
                    .names(names)
                    .build();

            Value minValue;

            for(int i=0;i<groups.size();i++){ // dla kazdej grupy
                for(int j=0;j<groups.get(i).lista.size();j++){ //dla każej kolumny za wyjatkiem tych względem których grupowaliśmy

                    if(skipCol(i,j)) { // pominiecie kolumny wedlug ktorej grupowalismy
                        result.lista.get(j).add(groups.get(i).lista.get(j).get(0));
                        continue;
                    }

                    checkInconsistency(i,j,0);
                    minValue=groups.get(i).lista.get(j).get(0);

                    for(int k=1;k<groups.get(i).lista.get(j).size();k++){ // przejscie przez wszyskie elementy
                        checkInconsistency(i,j,k);
                        if(!minValue.lte(groups.get(i).lista.get(j).get(k))) // jesli minValue nie jest mniejsze badz rowne niz ten element to minValue staje sie tym elementem
                            minValue = groups.get(i).lista.get(j).get(k);
                    }
                    result.lista.get(j).add(minValue);
                }
            }
            return result;
        }

        public DataFrame sum() throws IOException, InconsistentTypeException {
            DataFrame result = DataFrame.builder()
                    .types(types)
                    .names(names)
                    .build();

            Value sumValue;

            for(int i=0;i<groups.size();i++){ // dla kazdej grupy
                for(int j=0;j<groups.get(i).lista.size();j++){ //dla każej kolumny za wyjatkiem tych względem których grupowaliśmy

                    if(skipCol(i,j)) { // pominiecie kolumny wedlug ktorej grupowalismy
                        result.lista.get(j).add(groups.get(i).lista.get(j).get(0));
                        continue;
                    }

                    if((!result.types.get(j).equals(IntegerValue.class)) && (!result.types.get(j).equals(DoubleValue.class)) && (!result.types.get(j).equals(FloatValue.class))){ // czy kolumna spelnia wymogi
                        colRemovement.add(j);
                        continue;
                    }

                    sumValue=groups.get(i).lista.get(j).get(0);
                    checkInconsistency(i,j,0);
                    for(int k=1;k<groups.get(i).lista.get(j).size();k++){ // przejscie przez wszyskie elementy
                        checkInconsistency(i,j,k);
                        sumValue.add(groups.get(i).lista.get(j).get(k)); // zsumowanie wszystkich elemwntów
                    }
                    result.lista.get(j).add(sumValue);
                }
            }

            removeCols(result);

            System.out.println(result.lista.get(0).get(0));
            return result;
        }

        public DataFrame mean() throws IOException, InconsistentTypeException {
            DataFrame result = DataFrame.builder()
                    .types(types)
                    .names(names)
                    .build();

            Value meanValue;

            for(int i=0;i<groups.size();i++){ // dla kazdej grupy
                for(int j=0;j<groups.get(i).lista.size();j++){ //dla każej kolumny za wyjatkiem tych względem których grupowaliśmy

                    if(skipCol(i,j)) { // pominiecie kolumny wedlug ktorej grupowalismy
                        result.lista.get(j).add(groups.get(i).lista.get(j).get(0));
                        continue;
                    }

                    if((!result.types.get(j).equals(IntegerValue.class)) && (!result.types.get(j).equals(DoubleValue.class)) && (!result.types.get(j).equals(FloatValue.class))){
                        colRemovement.add(j);
                        continue;
                    }

                    checkInconsistency(i,j,0);
                    meanValue=groups.get(i).lista.get(j).get(0);

                    for(int k=1;k<groups.get(i).lista.get(j).size();k++){ // przejscie przez wszyskie elementy
                        checkInconsistency(i,j,k);
                        meanValue.add(groups.get(i).lista.get(j).get(k)); // zsumowanie wszystkich elemwntów
                    }
                    meanValue.div(new IntegerValue(groups.get(i).lista.get(j).size())); // podzielenie przez liczbe elemnwtów
                    result.lista.get(j).add(meanValue);
                }
            }

            removeCols(result);
            return result;
        }

        public DataFrame std() throws IOException, InconsistentTypeException {
            DataFrame result = DataFrame.builder()
                    .types(types)
                    .names(names)
                    .build();

            Value meanValue;
            Value stdValue;

            for(int i=0;i<groups.size();i++){ // dla kazdej grupy
                for(int j=0;j<groups.get(i).lista.size();j++){ //dla każej kolumny za wyjatkiem tych względem których grupowaliśmy

                    if(skipCol(i,j)) { // pominiecie kolumny wedlug ktorej grupowalismy
                        result.lista.get(j).add(groups.get(i).lista.get(j).get(0));
                        continue;
                    }

                    if((!result.types.get(j).equals(IntegerValue.class)) && (!result.types.get(j).equals(DoubleValue.class)) && (!result.types.get(j).equals(FloatValue.class))){
                        colRemovement.add(j);
                        continue;
                    }

                    checkInconsistency(i,j,0);
                    stdValue = new DoubleValue(0.0);
                    meanValue = groups.get(i).lista.get(j).get(0);

                    for(int k=1;k<groups.get(i).lista.get(j).size();k++){ // przejscie przez wszyskie elementy
                        checkInconsistency(i,j,k);
                        meanValue.add(groups.get(i).lista.get(j).get(k)); // zsumowanie wszystkich elemwntów
                    }

                    meanValue=meanValue.div(new IntegerValue(groups.get(i).lista.get(j).size()));

                    for(int k=0;k<groups.get(i).lista.get(j).size();k++){ // przejscie przez wszyskie elementy
                        stdValue = stdValue.add((groups.get(i).lista.get(j).get(k).sub(meanValue)).mul(groups.get(i).lista.get(j).get(k).sub(meanValue)));
                    }

                    stdValue.div(new IntegerValue(groups.get(i).lista.get(j).size())); // podzielenie przez liczbe elementów
                    stdValue = new DoubleValue(Math.sqrt( ((DoubleValue)stdValue).getWartosc() ));
                    result.lista.get(j).add(stdValue);
                }
            }

            removeCols(result);
            return result;
        }

        public DataFrame var() throws IOException, InconsistentTypeException {
            DataFrame result = DataFrame.builder()
                    .types(types)
                    .names(names)
                    .build();

            Value meanValue;
            Value varValue;

            for(int i=0;i<groups.size();i++){ // dla kazdej grupy
                for(int j=0;j<groups.get(i).lista.size();j++){ //dla każej kolumny za wyjatkiem tych względem których grupowaliśmy

                    if(skipCol(i,j)) { // pominiecie kolumny wedlug ktorej grupowalismy
                        result.lista.get(j).add(groups.get(i).lista.get(j).get(0));
                        continue;
                    }

                    if((!result.types.get(j).equals(IntegerValue.class)) && (!result.types.get(j).equals(DoubleValue.class)) && (!result.types.get(j).equals(FloatValue.class))){
                        colRemovement.add(j);
                        continue;
                    }

                    checkInconsistency(i,j,0);
                    varValue = new DoubleValue(0.0);
                    meanValue=groups.get(i).lista.get(j).get(0);

                    for(int k=1;k<groups.get(i).lista.get(j).size();k++){ // przejscie przez wszyskie elementy
                        checkInconsistency(i,j,k);
                        meanValue.add(groups.get(i).lista.get(j).get(k)); // zsumowanie wszystkich elemwntów
                    }
                    meanValue=meanValue.div(new IntegerValue(groups.get(i).lista.get(j).size()));

                    for(int k=0;k<groups.get(i).lista.get(j).size();k++){ // przejscie przez wszyskie elementy
                        varValue = varValue.add((groups.get(i).lista.get(j).get(k).sub(meanValue)).mul(groups.get(i).lista.get(j).get(k).sub(meanValue)));
                    }

                    varValue.div(new IntegerValue(groups.get(i).lista.get(j).size())); // podzielenie przez liczbe elementów
                    result.lista.get(j).add(varValue);
                }
            }

            removeCols(result);
            return result;
        }

        public DataFrame apply(Applyable x) throws IOException, InconsistentTypeException {
            DataFrame result = new DataFrame();

            for(int i=0;i<groups.size();i++) { // dla kazdej grupy
                result = x.apply(groups.get(i));
            }

            return result;
        }
    }

    /**
     * Metoda ktora pobiera odpowiednia kolumne i zwraca obiekt klasy OperationOnColumn
     * @param col nazwa kolumny ktora chce zmodyfikowac
     * @return obiekt klasy OperationOnColumn na ktorym mozna wywolac jakies modyfikacje, zawiera kolumne w postaci ArrayList i jej nazwe
     */
    public OperationOnColumn modify(String col){
        ArrayList<Value> kolumnaDoModyfikacji;
        kolumnaDoModyfikacji=this.get(col); // dalej operuje na tych samych referencjach (shallow copy)
        return new OperationOnColumn(kolumnaDoModyfikacji,col);
    }

    /**
     * Wewnetrzna klasa ktora implementuje 3 mozliwosci modyfikacji kolumny
     * poprzez modyfikowanie jedna wartoscia, kolumna z tej samej DataFrame lub innej DataFrame
     * Możliwe modyfikacje wskazuje interface Modify (add,sub,.. itp)
     */
    public class OperationOnColumn implements Modify { // x to zmienna lub kolumna ktora modyfikujemy zadana kolumne
        private ArrayList<Value> kolumnaDoModyfikacji;
        private String kolumnaDoModyfikacjaNazwa;

        private OperationOnColumn(ArrayList<Value> col,String name){
            kolumnaDoModyfikacji = col;
            kolumnaDoModyfikacjaNazwa = name;
        }

        public void add(Value x){
            for(int i =0 ;i<kolumnaDoModyfikacji.size();i++)
                kolumnaDoModyfikacji.get(i).add(x);
        }

        private boolean addCore(ArrayList<Value> x){ // glowne dodawanie jednej kolumny do drugiej
            if(kolumnaDoModyfikacji.size() == x.size()){
                for(int i=0;i<kolumnaDoModyfikacji.size();i++){
                    kolumnaDoModyfikacji.get(i).add(x.get(i));
                }
                return false;
            }else
                return true;
        }

        public void add(String col){ // dodawanie kolumny z tej samej DataFrame
            ArrayList<Value> x = DataFrame.this.get(col);
            if(addCore(x)) // tylko tutaj mam dostep do pola col
                throw new IllegalArgumentException("Different sizes of columns: " + kolumnaDoModyfikacjaNazwa  + " " + col);
        }

        public void add(ArrayList<Value> x, String colName){ // dodawanie kolumny z innej DataFrame
            if(addCore(x)) // tylko tutaj mam dostep do pola colName
                throw new IllegalArgumentException("Different sizes of columns: " + kolumnaDoModyfikacjaNazwa  + " " + colName);
        }

        public void sub(Value x){
            for(int i =0 ;i<kolumnaDoModyfikacji.size();i++)
                kolumnaDoModyfikacji.get(i).add(x);
        }

        private boolean subCore(ArrayList<Value> x){
            if(kolumnaDoModyfikacji.size() == x.size()){
                for(int i=0;i<kolumnaDoModyfikacji.size();i++){
                    kolumnaDoModyfikacji.get(i).sub(x.get(i));
                }
                return false;
            }else
                return true;
        }

        public void sub(String col){
            ArrayList<Value> x = DataFrame.this.get(col);
            if(subCore(x))
                throw new IllegalArgumentException("Different sizes of columns: " + kolumnaDoModyfikacjaNazwa  + " " + col);
        }

        public void sub(ArrayList<Value> x, String colName){
            if(subCore(x))
                throw new IllegalArgumentException("Different sizes of columns: " + kolumnaDoModyfikacjaNazwa  + " " + colName);
        }

        public void mul(Value x){
            for(int i =0 ;i<kolumnaDoModyfikacji.size();i++)
                kolumnaDoModyfikacji.get(i).add(x);
        }

        private boolean mulCore(ArrayList<Value> x){
            if(kolumnaDoModyfikacji.size() == x.size()){
                for(int i=0;i<kolumnaDoModyfikacji.size();i++){
                    kolumnaDoModyfikacji.get(i).mul(x.get(i));
                }
                return false;
            }else
                return true;
        }

        public void mul(String col){
            ArrayList<Value> x = DataFrame.this.get(col);
            if(mulCore(x))
                throw new IllegalArgumentException("Different sizes of columns: " + kolumnaDoModyfikacjaNazwa  + " " + col);
        }

        public void mul(ArrayList<Value> x, String colName){
            if(mulCore(x))
                throw new IllegalArgumentException("Different sizes of columns: " + kolumnaDoModyfikacjaNazwa  + " " + colName);
        }

        public void div(Value x){
            for(int i =0 ;i<kolumnaDoModyfikacji.size();i++)
                kolumnaDoModyfikacji.get(i).add(x);
        }

        private boolean divCore(ArrayList<Value> x){
            if(kolumnaDoModyfikacji.size() == x.size()){
                for(int i=0;i<kolumnaDoModyfikacji.size();i++){
                    kolumnaDoModyfikacji.get(i).div(x.get(i));
                }
                return false;
            }else
                return true;
        }

        public void div(String col){
            ArrayList<Value> x = DataFrame.this.get(col);
            if(divCore(x))
                throw new IllegalArgumentException("Different sizes of columns: " + kolumnaDoModyfikacjaNazwa  + " " + col);
        }

        public void div(ArrayList<Value> x, String colName){
            if(divCore(x))
                throw new IllegalArgumentException("Different sizes of columns: " + kolumnaDoModyfikacjaNazwa  + " " + colName);
        }

        public void pow(Value x){
            for(int i =0 ;i<kolumnaDoModyfikacji.size();i++)
                kolumnaDoModyfikacji.get(i).add(x);
        }

        private boolean powCore(ArrayList<Value> x){
            if(kolumnaDoModyfikacji.size() == x.size()){
                for(int i=0;i<kolumnaDoModyfikacji.size();i++){
                    kolumnaDoModyfikacji.get(i).pow(x.get(i));
                }
                return false;
            }else
                return true;
        }

        public void pow(String col){
            ArrayList<Value> x = DataFrame.this.get(col);
            if(powCore(x))
                throw new IllegalArgumentException("Different sizes of columns: " + kolumnaDoModyfikacjaNazwa  + " " + col);
        }

        public void pow(ArrayList<Value> x, String colName){
            if(powCore(x))
                throw new IllegalArgumentException("Different sizes of columns: " + kolumnaDoModyfikacjaNazwa  + " " + colName);
        }
    }
}
