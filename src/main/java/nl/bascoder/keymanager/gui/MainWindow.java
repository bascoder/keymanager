package nl.bascoder.keymanager.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

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
        this.tblContent.setModel(new DatabaseTableModel());
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainWindow");
        frame.setContentPane(new MainWindow().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

