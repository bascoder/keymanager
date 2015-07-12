package nl.bascoder.keymanager.test.orm;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import nl.bascoder.keymanager.DatabaseManager;
import nl.bascoder.keymanager.entity.Device;
import nl.bascoder.keymanager.entity.Owner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Bas for project licenseKey-manager.
 *
 * @author bascoder
 * @since 12-7-2015
 */
public class DeviceTestCase {
    Device mDevice;
    Dao<Device, Integer> mDao;
    Dao<Owner, Integer> mOwnerDao;

    @Before
    public void setUp() throws Exception {
        mDevice = new Device();
        mDevice.setName("Test device1");

        final ConnectionSource connectionSource
                = DatabaseManager.getInstance().getConnectionSource();
        mDao = DaoManager.createDao(connectionSource, Device.class);
        mOwnerDao = DaoManager.createDao(connectionSource, Owner.class);

        Owner owner = new Owner();
        owner.setName("Test owner1");
        mDevice.setOwner(owner);

        mOwnerDao.create(owner);
        mDao.create(mDevice);
    }

    @After
    public void tearDown() throws Exception {
        try {
            mOwnerDao.delete(mDevice.getOwner());
            mDao.delete(mDevice);
        } catch (Exception e) {
            Logger.getGlobal().log(Level.WARNING, "Could not clean up device", e);
        }
    }

    @Test
    public void testCreate() throws Exception {
        Assert.assertNotNull(mDevice);
        Assert.assertNotNull(mDao);
    }

    @Test
    public void testRead() throws Exception {
        int id = mDevice.getId();

        Device queried = mDao.queryForId(id);

        Assert.assertEquals(mDevice.getId(), queried.getId());
    }

    @Test
    public void testUpdate() throws Exception {
        final String NEW_NAME = "test device 2";
        mDevice.setName(NEW_NAME);

        mDao.update(mDevice);

        Device queried = mDao.queryForSameId(mDevice);

        Assert.assertEquals(NEW_NAME, queried.getName());
    }

    @Test
    public void testDelete() throws Exception {
        Device toDelete = new Device();
        toDelete.setName("test device to delete1");
        toDelete.setOwner(mDevice.getOwner());

        mDao.create(toDelete);
        int id = toDelete.getId();

        Assert.assertNotNull(mDao.queryForId(id));
        mDao.delete(toDelete);

        Assert.assertNull(mDao.queryForId(id));
    }
}
