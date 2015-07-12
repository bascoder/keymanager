package nl.bascoder.keymanager.test.orm;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import nl.bascoder.keymanager.DatabaseManager;
import nl.bascoder.keymanager.entity.Owner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Bas on 7-6-2015.
 *
 * @author Bas
 * @version 1.0 - creation
 * @since 7-6-2015
 */
public class OwnerTestCase {
    private Owner mOwner;
    private Dao<Owner, Integer> mOwnerDao;

    @Before
    public void setUp() throws Exception {
        mOwner = new Owner();
        mOwner.setName("Test Guy");
        final ConnectionSource connectionSource
                = DatabaseManager.getInstance().getConnectionSource();
        mOwnerDao = DaoManager.createDao(connectionSource, Owner.class);

        mOwnerDao.create(mOwner);
    }

    @After
    public void tearDown() throws Exception {
        try {
            deleteOwner(mOwner);
        } catch (Exception e) {
            Logger.getGlobal().log(Level.WARNING, "Could not clean up owner", e);
        }
    }

    private void deleteOwner(Owner owner) throws SQLException {
        mOwnerDao.delete(owner);
    }

    @Test
    public void testSetupComplete() throws Exception {
        Assert.assertNotNull(mOwner);
        Assert.assertNotNull(mOwnerDao);
    }

    @Test
    public void testRead() throws Exception {
        int id = mOwner.getId();

        Owner o = mOwnerDao.queryForId(id);

        Assert.assertEquals(mOwner, o);
    }

    @Test
    public void testUpdate() throws Exception {
        int id = mOwner.getId();
        final String NEW_NAME = "Fred";

        mOwner.setName(NEW_NAME);
        mOwnerDao.update(mOwner);

        Owner o = mOwnerDao.queryForId(id);
        Assert.assertEquals(NEW_NAME, o.getName());
    }

    @Test
    public void testDelete() throws Exception {
        Owner o = new Owner();
        o.setName("Harry" + UUID.randomUUID().toString());

        mOwnerDao.create(o);
        int id = o.getId();

        Owner ownerToDelete = mOwnerDao.queryForId(id);

        Assert.assertEquals(o, ownerToDelete);
        deleteOwner(ownerToDelete);

        Owner selected = mOwnerDao.queryForId(id);
        Assert.assertNull(selected);
    }
}
