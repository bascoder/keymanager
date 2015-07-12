package nl.bascoder.keymanager.test.orm;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import nl.bascoder.keymanager.DatabaseManager;
import nl.bascoder.keymanager.entity.Key;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Bas for project licenseKey-manager.
 *
 * @author bascoder
 * @since 12-7-2015
 */
public class KeyTestCase {

    private Key mKey;
    private Dao<Key, Integer> mKeyDao;

    @Before
    public void setUp() throws Exception {
        mKey = new Key();
        mKey.setLicenseKey("test key");

        final ConnectionSource connectionSource
                = DatabaseManager.getInstance().getConnectionSource();

        mKeyDao = DaoManager.createDao(connectionSource, Key.class);
        mKeyDao.create(mKey);
    }

    @After
    public void tearDown() throws Exception {
        try {
            mKeyDao.delete(mKey);
        } catch (Exception e) {
            Logger.getGlobal().log(Level.WARNING, "Could not clean up owner", e);
        }
    }

    @Test
    public void testCreate() throws Exception {
        Assert.assertNotNull(mKey);
        Assert.assertNotNull(mKeyDao);
    }

    @Test
    public void testUpdate() throws Exception {
        final String LICENSE_KEY_NEW = "Test change";
        mKey.setLicenseKey(LICENSE_KEY_NEW);
        mKeyDao.update(mKey);

        Key queried = mKeyDao.queryForId(mKey.getId());

        Assert.assertEquals(LICENSE_KEY_NEW, queried.getLicenseKey());
    }

    @Test
    public void testDelete() throws Exception {
        Key k = new Key();
        k.setLicenseKey(UUID.randomUUID().toString());

        mKeyDao.create(k);
        int id = k.getId();

        Key toDelete = mKeyDao.queryForId(id);
        mKeyDao.delete(toDelete);

        Key queried = mKeyDao.queryForId(id);
        Assert.assertNull(queried);
    }

    @Test
    public void testRead() throws Exception {
        int id = mKey.getId();

        Key queried = mKeyDao.queryForId(id);

        Assert.assertEquals(mKey, queried);
    }
}
