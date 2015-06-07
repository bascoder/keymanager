package nl.bascoder.keymanager.gui;

import java.awt.Dimension;
import java.awt.Window;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import nl.bascoder.keymanager.DatabaseTableModel;

/**
 * MainWindow shows keys and ability to add keys.
 *
 * @author Bas van Marwijk
 * @since 9-11-14
 * @version 1.0 - creation
 */
public class MainWindow {

    private final Window mWindow;
    private JTable tblContent;
    private JPanel panel1;
    private JButton mBtnAdd;

    public MainWindow() {
        mWindow = SwingUtilities.windowForComponent(panel1);
        initTable();
        initBtn();
    }

    private void initTable() {
        try {
            this.tblContent.setModel(new DatabaseTableModel());
        } catch(Exception e) {
            Logger.getGlobal().log(Level.SEVERE, "Could not create model", e);
            JOptionPane.showConfirmDialog(panel1,
                    "Failed to access database",
                    "DB Error",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    private void initBtn() {
        mBtnAdd.addActionListener(e -> new NewKeyDialog(mWindow));
    }

    public static void main(String[] args) {
        initJFrame();
    }

    private static void initJFrame() {
        JFrame frame = new JFrame("Key Manager");
        frame.setContentPane(new MainWindow().panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(600, 200));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

