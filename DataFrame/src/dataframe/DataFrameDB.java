package dataframe;

import exceptions.InconsistentTypeException;
import value.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


public class DataFrameDB extends DataFrame {

    private static Connection conn;
    private final String connection_string;
    private String table;

    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<Class<? extends Value> > types = new ArrayList<>();

    /**
     *
     * @param db nazwa bazy
     * @param user nazwa uzytkownika
     * @param pass haslo uzytkownika
     */
    public DataFrameDB(String db,String user, String pass){
        super();
        connection_string="jdbc:mysql://localhost:3306/"+db+"?user="+user+"&password="+pass+"&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    }

    private void catchNamesAndTypes(ResultSet resultSet) throws SQLException {
        names.clear();
        types.clear();

        ResultSetMetaData metadata = resultSet.getMetaData();
        int columnCount = metadata.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            String columnName = metadata.getColumnName(i);
            names.add(columnName);

            if(metadata.getColumnTypeName(i).equals("INT") || metadata.getColumnTypeName(i).equals("SMALLINT") || metadata.getColumnTypeName(i).equals("BIGINT") || metadata.getColumnTypeName(i).equals("MEDIUMINT") || metadata.getColumnTypeName(i).equals("TINYINT")){
                types.add(IntegerValue.class);
            }
            else if(metadata.getColumnTypeName(i).equals("DOUBLE")){
                types.add(DoubleValue.class);
            }
            else if(metadata.getColumnTypeName(i).equals("FLOAT")){
                types.add(FloatValue.class);
            }
            else if(metadata.getColumnTypeName(i).equals("CHAR") || metadata.getColumnTypeName(i).equals("VARCHAR")){
                types.add(StringValue.class);
            }
            else if(metadata.getColumnTypeName(i).equals("DATE")){
                types.add(DataTimeValue.class);
            }
        }
    }

    /**
     * Laczenie z baza danych i inicjalizacja danych z nia zwiazanych
     * @param table nazwa tablei ktora ma byc uzyta do stworzenia dataframe
     * @return true jesli udalo sie polaczyc z baza danych
     */
    public boolean open(String table) {
        try {
            this.table=table;
            // polaczenie z baza
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(connection_string);

            //uzyskanie naglowkow i typow
            String str="SELECT * FROM "+table;

            PreparedStatement statement = conn.prepareStatement(str);
            ResultSet resultSet = statement.executeQuery();

            catchNamesAndTypes(resultSet);

            return true;
        } catch(SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * zamkniecie polaczenia z baza danych
     */
    public void close() {
        try {
            if(conn != null) {
                conn.close();
            }
        } catch(SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    /**
     * metoda zwraca zwykla dataframe z wyniku otrzymanego z podanego zapytania
     */
    public static DataFrame query(String sql_query) throws InconsistentTypeException {
        ArrayList<String> namesV = new ArrayList<>();
        ArrayList<Class<? extends Value> > typesV = new ArrayList<>();
        DataFrame df_result = null;
        try {
            Statement stm = conn.createStatement();
            ResultSet resultSet = stm.executeQuery(sql_query);

            ResultSetMetaData metadata = resultSet.getMetaData();
            int columnCount = metadata.getColumnCount(); // liczba kolumn

            // otrzymanie nazw kolumn i typow
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metadata.getColumnName(i);
                namesV.add(columnName);

                switch (metadata.getColumnTypeName(i)) {
                    case "INT":
                    case "SMALLINT":
                    case "BIGINT":
                    case "MEDIUMINT":
                    case "TINYINT":
                        typesV.add(IntegerValue.class);
                        break;
                    case "DOUBLE":
                        typesV.add(DoubleValue.class);
                        break;
                    case "FLOAT":
                        typesV.add(FloatValue.class);
                        break;
                    case "CHAR":
                    case "VARCHAR":
                        typesV.add(StringValue.class);
                        break;
                    case "DATE":
                        typesV.add(DataTimeValue.class);
                        break;
                }
            }

            df_result = DataFrame.builder() // stworzenie df
                    .names(namesV)
                    .types(typesV)
                    .header(false)
                    .build();

            //wypelnienie df
            while (resultSet.next()) {
                Value[] row = new Value[typesV.size()];

                for(int i=0;i<typesV.size();i++)
                {
                    if(typesV.get(i) == IntegerValue.class){
                        row[i] = new IntegerValue(resultSet.getInt(i+1));
                    }
                    else if(typesV.get(i) == DoubleValue.class){
                        row[i] = new DoubleValue(resultSet.getDouble(i+1));
                    }
                    else if(typesV.get(i) == FloatValue.class){
                        row[i] = new FloatValue(resultSet.getFloat(i+1));
                    }
                    else if(typesV.get(i) == StringValue.class){
                        row[i] = new StringValue(resultSet.getString(i+1));
                    }
                    else if(typesV.get(i) == DataTimeValue.class){
                        row[i] = new DataTimeValue(resultSet.getDate(i+1).toString());
                    }
                }

                df_result.addRow(row); // dodanie wiersza
            }

            return df_result;

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return  df_result; // zwraca null jesli cos poszlo nie tak z zapytaniem
        }
    }

    public void saveDataFrameToDB(DataFrame df, String table){
        Statement stm;
        try {
            stm = conn.createStatement();
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE ");
            builder.append(table);
            builder.append(" (");

            for(int i=0;i<df.names.size();i++){
                builder.append(df.names.get(i));
                builder.append(" ");
                if(df.types.get(i) == IntegerValue.class){
                    builder.append("INT");
                }
                else if(df.types.get(i) == DoubleValue.class){
                    builder.append("DOUBLE");
                }
                else if(df.types.get(i) == FloatValue.class){
                    builder.append("FLOAT");
                }
                else if(df.types.get(i) == StringValue.class){
                    builder.append("VARCHAR(64)");
                }
                else if(df.types.get(i) == DataTimeValue.class){
                    builder.append("DATE");
                }
                builder.append(" ,");

            }

            builder.deleteCharAt(builder.length()-1);
            builder.append(");");
            stm.executeUpdate(builder.toString());

            StringBuilder insert;

            for(int j=0;j<df.size();j++){
                insert = new StringBuilder();
                insert.append("INSERT INTO ");
                insert.append(table);
                insert.append(" VALUES (");
                for(int i=0;i<df.names.size();i++) {
                    if(df.types.get(i) == DataTimeValue.class || df.types.get(i) == StringValue.class) {
                        insert.append("'");
                        insert.append(df.lista.get(i).get(j));
                        insert.append("'");
                        insert.append(",");
                    }else {
                        insert.append(df.lista.get(i).get(j));
                        insert.append(",");
                    }

                }
                insert.deleteCharAt(insert.length()-1);
                insert.append(");");

                stm.executeUpdate(insert.toString());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Grouping groupby(String[] colnames) throws IOException, InconsistentTypeException {
        return new Grouping(colnames);
    }

    public class Grouping extends DataFrame.Grouping implements Groupby{
        private String[] cols;

        private Grouping(String[] names) throws IOException, InconsistentTypeException {
            super();
            cols = names.clone();
        }

        public DataFrame max() throws InconsistentTypeException {
            StringBuilder builder = new StringBuilder();
            boolean flag;
            builder.append("SELECT ");

            for(int j=0;j<cols.length;j++) {
                builder.append(cols[j]);
                builder.append(",");
            }

            for( int i = 0 ; i < names.size(); i++ ) {
                flag = false;
                for(int j=0;j<cols.length;j++){
                    if(names.get(i).equals(cols[j])){
                        flag=true;
                    }
                }

                if (!flag) {
                    builder.append("max(");
                    builder.append(names.get(i));
                    builder.append(") as ");
                    builder.append(names.get(i));
                    builder.append(",");
                }

            }
            builder.deleteCharAt(builder.length()-1);
            builder.append(" FROM ");
            builder.append(table);
            builder.append(" GROUP BY ");
            for(int j=0;j<cols.length;j++) {
                builder.append(cols[j]);
                builder.append(",");
            }
            builder.deleteCharAt(builder.length()-1);
            builder.append(";");

            return DataFrameDB.query(builder.toString());

        }
        public DataFrame min() throws InconsistentTypeException {
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT ");
            boolean flag;

            for(int j=0;j<cols.length;j++) {
                builder.append(cols[j]);
                builder.append(",");
            }

            for( int i = 0 ; i < names.size(); i++ ) {
                flag = false;
                for(int j=0;j<cols.length;j++){
                    if(names.get(i).equals(cols[j])){
                        flag=true;
                    }
                }

                if (!flag) {
                    builder.append("min(");
                    builder.append(names.get(i));
                    builder.append(") as ");
                    builder.append(names.get(i));
                    builder.append(",");
                }

            }
            builder.deleteCharAt(builder.length()-1);
            builder.append(" FROM ");
            builder.append(table);
            builder.append(" GROUP BY ");
            for(int j=0;j<cols.length;j++) {
                builder.append(cols[j]);
                builder.append(",");
            }
            builder.deleteCharAt(builder.length()-1);
            builder.append(";");

            return DataFrameDB.query(builder.toString());
        }
        public DataFrame sum() {return new DataFrame();}
        public DataFrame mean() {return new DataFrame();}
        public DataFrame std() {return new DataFrame();}
        public DataFrame var() {return new DataFrame();}
        public DataFrame apply(Applyable x) {return new DataFrame();}
    }
}
