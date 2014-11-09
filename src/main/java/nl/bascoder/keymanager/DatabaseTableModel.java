package nl.bascoder.keymanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

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
public class DatabaseTableModel extends AbstractTableModel implements AutoCloseable {

    private DatabaseManager dbManager;
    private ResultSetMetaData resultSetMetaData;
    private final List<Row> rows;
    private Connection c;

    public DatabaseTableModel() {
        this.dbManager = DatabaseManager.getInstance();
        rows = new ArrayList<>();

        initResultSet();
    }

    private void initResultSet() {
        //try (Connection c = dbManager.getConnection()) {

        try {
            c = dbManager.getConnection();
            final String sql = String.format(Locale.US,
                    "SELECT k.id, k.license_key, k.in_use, d.id," +
                            " d.name, o.id AS own_id, o.name " +
                            "FROM key k " +
                            "INNER JOIN device d ON k.device_id = d.id " +
                            "INNER JOIN owner o ON d.owner_id = o.id");
            PreparedStatement stat = c.prepareStatement(sql);
            ResultSet s = stat.executeQuery();
            this.resultSetMetaData = s.getMetaData();

            while(s.next()) {
                Object[] values = new Object[7];
                values[0] = s.getLong(1);
                values[1] = s.getString(2);
                values[2] = s.getBoolean(3);
                values[3] = s.getLong(4);
                values[4] = s.getString(5);
                values[5] = s.getLong(6);
                values[6] = s.getString(7);

                rows.add(new Row(values));
            }
        } catch (SQLException e) {
            Logger.getGlobal().severe(e.getMessage());
        }
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
            return resultSetMetaData.getColumnName(columnIndex + 1);
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


    @Override
    public void close() throws Exception {
        c.close();
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

