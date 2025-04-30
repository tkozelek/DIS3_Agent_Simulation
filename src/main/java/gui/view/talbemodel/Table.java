package gui.view.talbemodel;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Table<T> extends AbstractTableModel {
    protected final String[] columnNames;
    protected final List<Function<T, Object>> columnMappers;
    protected List<T> data;

    public Table(String[] columnNames, List<Function<T, Object>> columnMappers) {
        this.columnNames = columnNames;
        this.data = new ArrayList<>();
        this.columnMappers = columnMappers;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= data.size()) {
            return null;
        }
        if (columnIndex < 0 || columnIndex >= columnMappers.size()) {
            throw new IllegalArgumentException("Invalid column index: " + columnIndex);
        }
        return columnMappers.get(columnIndex).apply(data.get(rowIndex));
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        data.set(rowIndex, (T) aValue);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void addRows(List<T> rows) {
        if (rows == null || rows.isEmpty()) return;
        data = rows;
        fireTableDataChanged();
    }
}