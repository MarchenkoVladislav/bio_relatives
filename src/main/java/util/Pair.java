package util;

/**
 * Structure for storage a data of two Clases in one object
 * @param <K> key
 * @param <V> value
 * @author Vladiislav Marchenko
 */
public class Pair<K,V>  {
    /**
     * Key of this Pair
     */
    private K key;
    /**
     * Value of this Pair
     */
    private V value;

    /**
     * Creates a new pair
     * @param key The key for this pair
     * @param value The value to use for this pair
     */
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Gets the key for this pair.
     * @return key for this pair
     */
    public K getKey() { return key; }

    /**
     * Gets the value for this pair.
     * @return value for this pair
     */
    public V getValue() { return value; }

}