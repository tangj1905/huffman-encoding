/**
 * Class describing a node in a Huffman tree - mostly containing getter and setter methods for the four fields
 * @author jgt31
 */
public class HuffmanNode implements Comparable<HuffmanNode> {
	private Character inChar; // character represented by this node - null represents nothing (non-leaf node)
	private int freq; // number of appearances for this character
	private HuffmanNode left; // left child of this node
	private HuffmanNode right; // right child of this node
	
	public HuffmanNode(Character ch, int f) {
		this.inChar = ch;
		this.freq = f;
	}
	
	public void setLeft(HuffmanNode node) {
		left = node;
	}
	
	public HuffmanNode getLeft() {
		return left;
	}
	
	public void setRight(HuffmanNode node) {
		right = node;
	}
	
	public HuffmanNode getRight() {
		return right;
	}
	
	public Character getChar() {
		return inChar;
	}
	
	public int getFreq() {
		return freq;
	}
	
	/**
	 * Basic string representation of a Huffman node. Certain common ASCII values are specified, rather than being directly printed.
	 */
	@Override
	public String toString() {
		String charID = "";
		if(getChar() == null) charID = "[NULL]";
		else switch((int) getChar().charValue()) {
			case 9:
				charID = "[TAB]";
				break;
			case 10:
				charID = "[LF]"; // line feed
				break;
			case 13:
				charID = "[CR]"; // carriage return
				break;
			case 32:
				charID = "[SPACE]";
				break;
			default:
				charID = getChar().toString();
		}
		
		return charID + ": " + getFreq();
	}

	/**
	 * Compares the frequencies of this node and another node - this is used to establish priority
	 * Lower frequencies are prioritized over higher ones in the construction of the Huffman tree
	 */
	@Override
	public int compareTo(HuffmanNode node) {
		return Integer.signum(getFreq() - node.getFreq());
	}
}