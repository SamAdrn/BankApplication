package bank;

/**
 * This interface classifies classes which requires a unique identification key.<br><br>
 * This interface only specifies one method, <code>getKey()</code>, in which implementing classes
 * must define using their own respective unique identification <code>Integer</code> key.
 *
 * @author Samuel Kosasih
 */
public interface Serialized {

    /**
     * Retrieves non-negative <code>Integer</code> key.
     *
     * @return the key as an <code>Integer</code>
     */
    int getKey();

}
