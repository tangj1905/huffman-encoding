import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main class for the Huffman encoder
 * @author jgt31
 */
public class HuffmanCompressor {
	/**
	 * Scans the input file and returns a list of Huffman nodes, containing the characters and their frequencies
	 * This method returns a priority queue, backed by a min heap, which is fairly optimal for finding and removing the items with lowest priority
	 * The heap in question is implemented with an ArrayList, simply because there's no need to worry about resizing operations
	 */
	public static HuffmanHeap scanInput(String file) throws IOException{
		// initialize readers for reading the input
		FileReader r = new FileReader(file);
		BufferedReader br = new BufferedReader(r);
		
		// initialize hashmap - the first integer is just the character in integer format
		HashMap<Character, Integer> map = new HashMap<Character, Integer>();
		
		// reading input file, reading character-frequency pairs into a hashmap
		int ch = br.read();
		while(ch != -1) {
			char c = (char) ch; // characters are read by BufferedReader as an integer, requires casting to character
			if(map.containsKey(c)) {
				map.replace(c, map.get(c) + 1);
			} else 
				map.put(c, 1);
			
			ch = br.read();
		}
		br.close();
		
		// initialize heap - we'll put the Huffman nodes in here
		HuffmanHeap heap = new HuffmanHeap();
		
		// iterate through the keys in the map, dropping their corresponding Huffman nodes into the heap
		for (Map.Entry<Character, Integer> mapEntry : map.entrySet()) {
			heap.insert(new HuffmanNode(mapEntry.getKey(), mapEntry.getValue()));
		}
		
		return heap;
	}
	
	/**
	 * Merges two Huffman nodes and returns their new parent
	 */
	public static HuffmanNode mergeNodes(HuffmanNode n1, HuffmanNode n2) {
		HuffmanNode parent = new HuffmanNode(null, n1.getFreq() + n2.getFreq()); // internal nodes don't store a character, so they will hold a null value
		parent.setLeft(n1);
		parent.setRight(n2);
		return parent;
	}
	
	/**
	 * Builds the Huffman tree from a min heap of Huffman nodes and returns the root of the tree
	 */
	public static HuffmanNode buildTree(HuffmanHeap heap) {
		while(heap.size() > 1) {
			HuffmanNode mergedNode = mergeNodes(heap.remove(), heap.remove());
			heap.insert(mergedNode);
		}
		
		return heap.remove();
	}
	
	/**
	 * Creates and returns a map which associates a leaf node with its corresponding Huffman code, while printing the char-freq-encoding triples
	 * Using these triples, this method will also calculate the space savings of this Huffman encoding
	 * A recursive helper method will be called to traverse the Huffman tree
	 */
	public static HashMap<Character, String> getCharEncoding(HuffmanNode root) {
		HashMap<HuffmanNode, String> map = new HashMap<HuffmanNode, String>();
		HashMap<Character, String> charEncoding = new HashMap<Character, String>();
		getCharEncoding(root, map, "");
		
		int originalSize = 0;
		int compressedSize = 0;
		
		// we'll sort the set of entries in this map so they'll be printed in order, based on frequency (it'll make the compression look more clear)
		List<Map.Entry<HuffmanNode, String>> entryList = new ArrayList<Map.Entry<HuffmanNode, String>>(map.entrySet());
		entryList.sort(Map.Entry.comparingByKey());
		
		// prints out the triples and calculates space savings
		for(Map.Entry<HuffmanNode, String> mapEntry : entryList) {
			System.out.println(mapEntry.getKey() + ": " + mapEntry.getValue());
			charEncoding.put(mapEntry.getKey().getChar(), mapEntry.getValue());
			
			originalSize += 8 * mapEntry.getKey().getFreq(); // each character assumed to occupy 8 bits, under UTF-8
			compressedSize += mapEntry.getValue().length() * mapEntry.getKey().getFreq(); // each character now occupies a variable number of bits
		}
		float ratio = (float)compressedSize / originalSize;
		
		System.out.println("Original size: " + originalSize + " bits");
		System.out.println("Compressed size: " + compressedSize + " bits (" + (Math.round(ratio * 1000) / 10.0) + "% of original)");
		
		return charEncoding;
	}
	
	/**
	 * Recursive helper method to correspond a Huffman leaf node with its encoding, which is just a string of zeros and ones
	 */
	private static void getCharEncoding(HuffmanNode node, HashMap<HuffmanNode, String> map, String code) {
		if(node.getChar() != null)  // if the node is a leaf node
			map.put(node, code);
		else {
			getCharEncoding(node.getLeft(), map, code + "0");
			getCharEncoding(node.getRight(), map, code + "1");
		}
	}
	
	/**
	 * Scans the file again, outputting the encoding file with the specified filename
	 * The output file will automatically be overwritten before writing to it
	 */
	public static void writeFile(String inputFile, String outputFile, HashMap<Character, String> charEncoding) throws IOException {
		FileReader r = new FileReader(inputFile);
		BufferedReader br = new BufferedReader(r);
		
		FileWriter w = new FileWriter(outputFile, false);
		
		// reading the input file, with each character's encoding written to the output file
		int ch = br.read();
		while(ch != -1) {
			char c = (char) ch;
			w.write(charEncoding.get(c));
			
			ch = br.read();
		}
		r.close();
		w.close();
	}
	
	/**
	 * Consolidates all of the above methods into a single method - this will do the full Huffman encoding algorithm
	 */
	public static String huffmanCoder(String inputFileName, String outputFileName) {
		try {
			long startTime = System.nanoTime();
			
			// scans the file, creates a min heap of the char-freq pairs
			HuffmanHeap heap = scanInput(inputFileName);
			// constructs the tree from the heap, gets the string of zeros and ones that each character encodes to
			HashMap<Character, String> charEncoding = getCharEncoding(buildTree(heap));
			// re-scans the first file and writes the encoding to the output file
			writeFile(inputFileName, outputFileName, charEncoding);
			
			long timeElapsed = (System.nanoTime() - startTime) / 1000000;
			return "Successfully encoded file (time elapsed: " + timeElapsed + "ms)";
		} catch(IOException e) {
			return "Input file error";
		}
	}
	
	/**
	 * Main method of this HuffmanCompressor - the program will take in the first two String arguments from the console
	 */
	public static void main(String[] args) {
		System.out.println(huffmanCoder(args[0], args[1]));
	}
}