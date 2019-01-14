package exceptions;

public class InconsistentTypeException extends Exception {
    private String colName;
    private int row;

    public InconsistentTypeException() {
        super();
    }

    public InconsistentTypeException(String message, String colName, int row) {
        super(message);
        this.colName=colName;
        this.row=row;
    }

    public InconsistentTypeException(String message, String colName, int row, Throwable cause) {
        super(message,cause);
        this.colName=colName;
        this.row=row;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String getMessage() {
        return super.getMessage() + "for colName: " + colName + " and row: " + row;
    }

    public String getColName() {
        return colName;
    }

    public int getRow() {
        return row;
    }
}
