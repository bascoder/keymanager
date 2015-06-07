package nl.bascoder.keymanager.gui;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import nl.bascoder.keymanager.DatabaseManager;
import nl.bascoder.keymanager.entity.Device;
import nl.bascoder.keymanager.entity.Key;
import nl.bascoder.keymanager.entity.Owner;

public class NewKeyDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<Owner> mCbOwner;
    private JComboBox<Device> mCbDevice;
    private JTextField mTxtKey;
    private JCheckBox mChkInUse;
    private JTextField txtNewOwner;
    private JTextField mTxtNewDevice;
    private Component mParent;
    private DatabaseManager mDbManager;
    private ConnectionSource mConnection;
    private Owner mNewOwner;
    private Device mNewDevice;

    public NewKeyDialog(Window parent) {
        super(parent);
        this.mParent = parent;
        mDbManager = DatabaseManager.getInstance();
        init();
    }

    private void init() {
        setModalityType(ModalityType.DOCUMENT_MODAL);
        setTitle("Add a new key");

        setContentPane(contentPane);
        setMinimumSize(new Dimension(500, 500));
        setLocationRelativeTo(mParent);
        getRootPane().setDefaultButton(buttonOK);

        setVisible(true);
        setButtonHandlers();
        initComponents();
    }

    private void setButtonHandlers() {
        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(
                e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void initComponents() {
        initOwnerCheckbox();
        mCbDevice.addActionListener(e -> onSelectDeviceCb());
        addTxtHandlers(txtNewOwner, e -> addNewOwner());
        addTxtHandlers(mTxtNewDevice, e -> addNewDevice());
    }

    private void onOK() {
        saveNewKey();
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void initOwnerCheckbox() {

        initOwnerModel();
        mCbOwner.addActionListener(e -> {
            try {
                onSelectOwner();
            } catch (SQLException e1) {
                Logger.getGlobal().log(Level.SEVERE, "Tried to add devices", e);
            }
        });
    }

    private void onSelectDeviceCb() {
        final Device selectedItem = (Device) mCbDevice.getSelectedItem();
        if (selectedItem.equals(mNewDevice)) {
            mTxtNewDevice.setEditable(true);
            mTxtNewDevice.requestFocus();
        }
    }

    private void addTxtHandlers(JTextField field, ActionListener consumer) {
        field.addActionListener(consumer);
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                consumer.actionPerformed(null);
            }
        });
    }

    private void addNewOwner() {
        try {
            Dao<Owner, Integer> ownerDao = DaoManager.createDao(mConnection, Owner.class);
            mNewOwner.setName(txtNewOwner.getText());

            ownerDao.create(mNewOwner);
        } catch (SQLException e) {
            Logger.getGlobal().log(Level.SEVERE, "Could not add new owner", e);
        }
    }

    private void addNewDevice() {
        try {
            Dao<Device, Integer> deviceDao = DaoManager.createDao(mConnection, Device.class);
            mNewDevice.setName(mTxtNewDevice.getText());
            mNewDevice.setOwner(getSelectedOwner());

            deviceDao.create(mNewDevice);
        } catch (SQLException e) {
            Logger.getGlobal().log(Level.SEVERE, "Could not add new device", e);
        }
    }

    private void saveNewKey() {
        Key key = new Key();
        key.setLicenseKey(mTxtKey.getText());
        key.setDevice(getSelectedDevice());
        key.setInUse(mChkInUse.isSelected());

        try {
            Dao<Key, Integer> dao = DaoManager.createDao(mConnection, Key.class);

            dao.create(key);
        } catch (SQLException e) {
            Logger.getGlobal().log(Level.SEVERE, "Could not add key to db", e);
            Logger.getGlobal().log(Level.SEVERE, e.getCause().toString());
        }
    }

    private void initOwnerModel() {
        try {
            mConnection = initConnection();

            Dao<Owner, Integer> ownerDao =
                    DaoManager.createDao(mConnection, Owner.class);

            final List<Owner> owners = ownerDao.queryForAll();
            final DefaultComboBoxModel<Owner> model = new DefaultComboBoxModel<>(
                    owners.toArray(new Owner[owners.size()]));
            mCbOwner.setModel(model);

            // add option for user to select new owner
            mNewOwner = new Owner();
            mNewOwner.setName("<< new owner >>");
            model.addElement(mNewOwner);
        } catch (SQLException e) {
            Logger.getGlobal().log(Level.SEVERE, "Can't connect to db", e);
        }
    }

    private void onSelectOwner() throws SQLException {
        Owner owner = getSelectedOwner();
        Dao<Device, Integer> deviceDao = DaoManager.createDao(mConnection, Device.class);

        List<Device> devices;
        if (owner.equals(mNewOwner)) {
            if (!txtNewOwner.isEditable()) {
                txtNewOwner.setEditable(true);
                txtNewOwner.requestFocus();
            }
            devices = deviceDao.queryForAll();
        } else {
            devices = deviceDao.queryBuilder().where()
                    .eq("owner_id", owner)
                    .query();

        }
        addDevicesToComboBox(devices);
    }

    private Owner getSelectedOwner() {
        return (Owner) mCbOwner.getSelectedItem();
    }

    private Device getSelectedDevice() {
        return (Device) mCbDevice.getSelectedItem();
    }

    private ConnectionSource initConnection() throws SQLException {
        return mDbManager.getConnectionSource();
    }

    private void addDevicesToComboBox(List<Device> devices) {
        mNewDevice = new Device();
        mNewDevice.setName("<< new device >>");
        devices.add(mNewDevice);

        mCbDevice.setModel(
                new DefaultComboBoxModel<>(
                        devices.toArray(new Device[devices.size()])
                )
        );
    }
}
