public class Heap {
    private Node[] array;
    private int currIndex = 1, nrItems = 0;

    Heap(int size) {
        array = new Node[size];
    }

    public void insert(Node object) {
        array[currIndex] = object;

        perculateUp(currIndex++);

        nrItems++;
    }

    public int getSize() {
        return nrItems;
    }

    public Node deleteMin() {
        if (isEmpty()) {
            return null;
        }
        Node min = array[1];
        array[1] = array[nrItems--];
        currIndex--;
        perculateDown(1);

        return min;
    }
    private void perculateUp(int index) {
        Node lastPlace = array[index];

        if (index == 1) return;

        while(index > 1 && array[index/2].getFreq() > lastPlace.getFreq()) {

            int parent = index / 2;

            array[index] = array[parent];

            index = parent;

        }

        array[index] = lastPlace;
    }
    private void perculateDown(int index) {
        int child;
        Node currNode = array[index];

        while ((index * 2) <= nrItems) {
            child = index * 2;

            //Sjekker først hvem av venstre- og høyrebarnet som er minst
            if (child != nrItems &&
                    array[child+1].getFreq() < array[child].getFreq()) {
					/* Hvis venstrebarn er minst, øk child med en (for å sende dette
					 * barnet oppover. Ellers ikke gjør noe
					 */
                child++;
            }
				/*
				 * Hvis barnet er mindre enn forelderen; bytt. Hvis ikke; ferdig
				 */
            if (array[child].getFreq() < currNode.getFreq()) {
                array[index] = array[child];
            } else {
                break;
            }
            index = child;
        }
			/*
			 * Til slutt, sett noden som var først (men ikke minst) ved slette-kallet
			 * til å være der det ble funnet barn som ikke var mindre enn forelder
			 */
        array[index] = currNode;
    }
    public boolean isEmpty() {
        return nrItems == 0;
    }
    public void print() {

        for (int i = 1; i < currIndex; i++) {
        }
    }

}
