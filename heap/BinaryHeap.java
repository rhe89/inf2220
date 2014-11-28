
public class BinaryHeap<T extends PriInterface> {
		private T[] array;
		private int currIndex = 1, nrItems = 0;
		
		@SuppressWarnings("unchecked")
		BinaryHeap(int size) {
			array = (T[]) new PriInterface[size];
		}
		
		public void insert(T object) {
			array[currIndex] = object;
			
			perculateUp(currIndex++);
			
			nrItems++;
		}

		public T deleteMin() {
			if (isEmpty()) {
				return null;
			}
			T min = array[1];
			array[1] = array[nrItems--];
			perculateDown(1);
			
			return min;
		}
		private void perculateUp(int index) {
			T lastPlace = array[index];
			
			if (index == 1) return;
			
			while(index > 1 && array[index/2].getPri() > lastPlace.getPri()) {
				
				int parent = index / 2;
				
				array[index] = array[parent];
				
				index = parent;
				
			}
			
			array[index] = lastPlace
		}
		private void perculateDown(int index) {
			int child;
			T currNode = array[index];
			
			while ((index * 2) <= nrItems) {
				child = index * 2;
				
				//Sjekker først hvem av venstre- og høyrebarnet som er minst
				if (child != nrItems &&
						array[child+1].getPri() < array[child].getPri()) {
					/* Hvis venstrebarn er minst, øk child med en (for å sende dette
					 * barnet oppover. Ellers ikke gjør noe
					 */
					child++;
				}
				/*
				 * Hvis barnet er mindre enn forelderen; bytt. Hvis ikke; ferdig
				 */
				if (array[child].getPri() < currNode.getPri()) {
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
			for (int i = 0; i < array.length; i++) {
				if (array[i] != null) {
					return false;
				}
			}
			return true;
		}
		public void print() {
			for (int i = 1; i < currIndex; i++) {
				System.out.println(array[i].getPri());
			}
		}

}
