public class Hash {

    int[] hashArray, tableSize, currNr;

    Hash(int size) {
        tableSize = size;
        hashArray = new int[tableSize];
        for (int i = 0; i < tableSize; i++) {
            hashArray[i] = -1;
        }
    }

    public void insert(int key) {
        hashArray[hash(key)] = key;
        currNr++;
        if (rehashNeeded()) rehash();
    }

    public int get(int key) {
        int index = key % tableSize;
        int count = 0;

        while (hashArray[index] != key) {
            index = linearprobing(count++);

            index = quadraticProbing(count++);

            index = hash2(key, count++);
        }

        return hashArray[index];
    }

    public int remove(int key) {
        int index = key % tableSize;
        int count = 0;

        while (hashArray[index] != key) {
            index = linearprobing(count++);

            index = quadraticProbing(count++);

            index = hash2(key, count++);
        }

        int removed = hashArray[index];
        hashArray[index] = -1;
        return removed;
    }

    int hash(int key) {
        int index = key % tableSize;

        int count = 0;

        while (isOccupied(index)) {
            index = linearprobing(count++);

            index = quadraticProbing(count++);

            index = hash2(key, count++);
        }

        return index;

    }

    private boolean isOccupied(int key) {
        return hashArray[index] != -1;
    }

    /*
    Dobbel hashing
     */
    private int hash2(int key, int count) {
        int r = getLargestPrime();

        return count * (r - (key % r));
    }

    /*
    For dobbel hashing. Finne høyeste primtal mindre enn tableSize
     */

    private int getLargestPrime() {
        int prime = 0;

        for (int i = 0; i < tableSize; i++) {
            if (isPrime(i)) {
                prime = i;
            }
        }

        return prime;
    }
    boolean isPrime(int n) {
        for (int i = 2; 2 * i < n; i++) {
            if (n % i==0)
                return false;
        }
        return true;
    }

    //Separate chaining

    private boolean rehashNeeded() {
        return ( (double) currNr / tableSize > 0.75);
    }

    private void rehash() {
        int[] newHashArray = new int[hashArray.size*2];
        int[] oldHashArray = hashArray;
        hashArray = newHashArray;
        tableSize = tableSize*2;

        for (int i = 0; i < oldHashArray.length; i++) {
            insert(oldHashArray[i]);
        }
    }
}