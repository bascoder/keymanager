package nl.bascoder.keymanager.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.WindowConstants;

import nl.bascoder.keymanager.DatabaseTableModel;

/**
 * @author Bas van Marwijk
 * @since 9-11-14
 * @version 1.0 - creation
 */
public class MainWindow {

    private JTable tblContent;
    private JPanel panel1;

    public MainWindow() {
        initTable();
    }

    private void initTable() {
        try {
            this.tblContent.setModel(new DatabaseTableModel());
        } catch(Exception e) {
            JOptionPane.showConfirmDialog(panel1, "Failed to access database");
        }

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Key Manager");
        frame.setContentPane(new MainWindow().panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

