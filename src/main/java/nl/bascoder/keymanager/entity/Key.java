package nl.bascoder.keymanager.entity;

/**
 * Entity containing license licenseKey.
 *
 * @author Bas van Marwijk
 * @since 9-11-14
 * @version 1.0 - creation
 */
public class Key {
    private long id;
    private String licenseKey;
    private boolean inUse;
    private Device device;

    /**
     * Getter for property 'id'.
     *
     * @return Value for property 'id'.
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for property 'id'.
     *
     * @param id Value to set for property 'id'.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for property 'licenseKey'.
     *
     * @return Value for property 'licenseKey'.
     */
    public String getLicenseKey() {
        return licenseKey;
    }

    /**
     * Setter for property 'licenseKey'.
     *
     * @param licenseKey Value to set for property 'licenseKey'.
     */
    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    /**
     * Getter for property 'inUse'.
     *
     * @return Value for property 'inUse'.
     */
    public boolean isInUse() {
        return inUse;
    }

    /**
     * Setter for property 'inUse'.
     *
     * @param inUse Value to set for property 'inUse'.
     */
    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    /**
     * Getter for property 'device'.
     *
     * @return Value for property 'device'.
     */
    public Device getDevice() {
        return device;
    }

    /**
     * Setter for property 'device'.
     *
     * @param device Value to set for property 'device'.
     */
    public void setDevice(Device device) {
        this.device = device;
    }
}

