import java.util.List;
import java.util.ArrayList;

/**
 * A basic min heap containing Huffman nodes as elements
 * @author jgt31
 */
public class HuffmanHeap {
	 public List<HuffmanNode> heap = new ArrayList<HuffmanNode>();
	
	/**
	 * Utility method for swapping the elements at two specified indexes
	 */
	private void swap(int indexOne, int indexTwo) {
		HuffmanNode first = heap.get(indexOne);
		heap.set(indexOne, heap.get(indexTwo));
		heap.set(indexTwo, first);
	}
	
	/**
	 * Inserts another node into the heap
	 */
	public void insert(HuffmanNode node) {
		heap.add(heap.size(), node);
		siftUp();
	}
	
	/**
	 * Utility method for "sifting up", bringing the last element to its proper place
	 */
	private void siftUp() {
		int cIndex = heap.size() - 1; // stores the child index
		while(cIndex >= 1) {
			int pIndex = (cIndex - 1) / 2; // parent index
			if(heap.get(cIndex).compareTo(heap.get(pIndex)) > 0)
				break;
			swap(cIndex, pIndex);
			cIndex = pIndex;
		}
	}
	
	/**
	 * Removes the node with the lowest frequency
	 */
	public HuffmanNode remove() {
		HuffmanNode node = heap.get(0);
		swap(0, heap.size() - 1);
		heap.remove(heap.size() - 1);
		siftDown();
		return node;
	}
	
	/**
	 * Utility method for "sifting down", bringing the first element to its proper place
	 */
	private void siftDown() {
		int pIndex = 0; // stores the parent index
		int cIndex = 1; // stores the left child's index
		while(cIndex < heap.size()) {
			if(cIndex < heap.size() - 1 && heap.get(cIndex).compareTo(heap.get(cIndex + 1)) > 0) 
				cIndex++;
			
			if(heap.get(pIndex).compareTo(heap.get(cIndex)) < 0)
				break;
			
			swap(pIndex, cIndex);
			pIndex = cIndex;
			cIndex = 2 * pIndex + 1;
		}
	}
	
	/**
	 * Returns the size of the heap
	 */
	public int size() {
		return heap.size();
	}
}
