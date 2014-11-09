package nl.bascoder.keymanager;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;

/**
 * TableModel bound to database.
 *
 *
 *
 * @author Bas van Marwijk
 * @since 9-11-14
 * @version 1.0 - creation
 */
public class DatabaseTableModel extends AbstractTableModel {

    private DatabaseManager dbManager;
    private ResultSetMetaData resultSetMetaData;
    private final List<Row> rows;

    public DatabaseTableModel() {
        this.dbManager = DatabaseManager.getInstance();
        rows = new ArrayList<>();

        initResultSet();

        new SwingWorker<Void, Row>() {
            public Void doInBackground() {
                // TODO: Process ResultSet and create Rows.  Call publish() for every N rows created.
                return null;
            }

            protected void process(Row... chunks) {
                // TODO: Add to ResultSetTableModel List and fire TableEvent.
            }
        }.execute();
    }

    private void initResultSet() {
        dbManager.getConnection();
    }

    /**
     * Returns the number of rows in the model. A
     * <code>JTable</code> uses this method to determine how many rows it
     * should display.  This method should be quick, as it
     * is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see #getColumnCount
     */
    @Override
    public int getRowCount() {
        return rows.size();
    }

    /**
     * Returns the number of columns in the model. A
     * <code>JTable</code> uses this method to determine how many columns it
     * should create and display by default.
     *
     * @return the number of columns in the model
     * @see #getRowCount
     */
    @Override
    public int getColumnCount() {
        try {
            return resultSetMetaData.getColumnCount();
        } catch (SQLException e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage() + " " + e.getErrorCode(), e);
            return 0;
        }
    }

    /**
     * Returns the name of the column at <code>columnIndex</code>.  This is used
     * to initialize the table's column header name.  Note: this name does
     * not need to be unique; two columns in a table can have the same name.
     *
     * @param   columnIndex     the index of the column
     * @return the name of the column
     */
    @Override
    public String getColumnName(int columnIndex) {
        try {
            return resultSetMetaData.getColumnName(columnIndex - 1);
        } catch (SQLException e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage() + " " + e.getErrorCode(), e);
            return null;
        }
    }

    /**
     * Returns the most specific superclass for all the cell values
     * in the column.  This is used by the <code>JTable</code> to set up a
     * default renderer and editor for the column.
     *
     * @param columnIndex  the index of the column
     * @return the common ancestor class of the object values in the model.
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        // TODO return correct class
        return Object.class;
    }

    /**
     * Returns true if the cell at <code>rowIndex</code> and
     * <code>columnIndex</code>
     * is editable.  Otherwise, <code>setValueAt</code> on the cell will not
     * change the value of that cell.
     *
     * @param   rowIndex        the row whose value to be queried
     * @param   columnIndex     the column whose value to be queried
     * @return true if the cell is editable
     * @see #setValueAt
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // only id is not editable
        return columnIndex != 0;
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param   rowIndex        the row whose value is to be queried
     * @param   columnIndex     the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rows.get(rowIndex)
                .getValue(columnIndex);
    }

    /**
     * Internal used row
     */
    private class Row {
        private final Object[] values;

        public Row(Object[] values) {
            this.values = values;
        }

        public int getSize() {
            return values.length;
        }

        public Object getValue(int i) {
            return values[i];
        }
    }
}

