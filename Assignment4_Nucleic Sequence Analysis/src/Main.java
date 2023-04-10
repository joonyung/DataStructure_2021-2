import java.io.*;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Main {
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Hash Table=new Hash();
		LinkedList<String> Text=new LinkedList<>();
		int student_id=0;

		while (true)
		{
			
			try
			{
				StringTokenizer st = new StringTokenizer(br.readLine());
				String option = st.nextToken();
				option = option.toUpperCase();

				if(option.equals("Q"))
				{
					break;
				}
				else if(option.equals("I")) {
					String sequence = st.nextToken("\n").toUpperCase().trim();
					student_id++;
					Table.insertSequence(sequence, student_id);
					Text.add(sequence);
				}
				else if(option.equals("S"))
				{
					String pattern = st.nextToken("\n").toUpperCase().trim();
					Table.retrievalPattern(pattern, Text);
				}
				else if(option.equals("P")) {
					String strHashValue = st.nextToken().trim();
					int HashValue = Integer.parseInt(strHashValue);
					Table.getAVLTree(HashValue).PrintByHashValue(HashValue);
				}
			}
			catch (IOException e)
			{
				System.out.println("Wrong input : " + e.toString());
			}
		}
	}

}

class Hash{
	private int TableSize=0;
	private AVLTree AVLTrees[] = null;

	public Hash(){
		TableSize = 64;
		AVLTrees = new AVLTree[64];

		for(int i = 0; i<64; i++){
			AVLTrees[i] = new AVLTree(i);
		}
	}
	public int CalcHashValue(String HashValueStr){
		HashValueStr = HashValueStr.replace("A", "1");
		HashValueStr = HashValueStr.replace("T", "2");
		HashValueStr = HashValueStr.replace("G", "3");
		HashValueStr = HashValueStr.replace("C", "4");
		int HashValue = Integer.parseInt(HashValueStr);
		return HashValue % 64;
	}
	public AVLTree getAVLTree(int HashValue){
		return AVLTrees[HashValue];
	}
	public void insertSequence(String sequence, int student_id){
		for(int i = 0; i < sequence.length() - 5; i++) {
			TreeNode newTreeNode = new TreeNode();
			ListNode newListNode = new ListNode(student_id, i + 1);
			String subSeq = sequence.substring(i, i + 6);
			int hashValue = CalcHashValue(subSeq);
			
			newTreeNode.setBalanceFactor(1);
			newTreeNode.SetPattern(subSeq);
			newTreeNode.pushList(newListNode);
			
			AVLTrees[hashValue].InsertNode(newTreeNode);
		}
	}
	public void retrievalPattern(String pattern, LinkedList<String> Text){
		int lengthPtn = pattern.length();
		boolean Found = false;
		
		for(int i = 0; i < Text.size(); i++) {
			String currStr = Text.get(i);
			for(int j = 0; j < currStr.length() - lengthPtn + 1; j++) {
				String subSeq = currStr.substring(j, j + lengthPtn);
				if(pattern.equals(subSeq)) {
					if(Found) System.out.print(" ");
					System.out.printf("(%d, %d)", i + 1, j + 1);
					Found = true;
				}
			}
		}
		if(!Found) System.out.println("Not Found");
		else System.out.print("\n");
	}
}

class AVLTree{
	private int HashValue;
	private int NumberOfElement;
	private TreeNode SuperParent = null;

	public AVLTree(int HashValue){
		initAVLTree(HashValue);
	}
	public void initAVLTree(int HashValue){
		this.HashValue=HashValue;
		NumberOfElement=0;
	}
	public int getNumberOfElement(){
		return NumberOfElement;
	}
	public int getHashvalue(){
		return HashValue;
	}

	/*  ##### NOTICE: you should use PrintByHashValue, but you can change other functions to implement AVL tree ###### */
	public void InsertNode(TreeNode NewNode){
		NumberOfElement++;
		
		if(SuperParent == null) {
			SuperParent = NewNode;
		}
		else {
			TreeNode itrNode = SuperParent;
			while(itrNode != null) {
				String newSeq = NewNode.getPattern();
				String itrSeq = itrNode.getPattern();
				int compareNum = newSeq.compareTo(itrSeq);
				
				if(compareNum < 0) {
					if(itrNode.getLeft() == null) {
						NewNode.setParent(itrNode);
						itrNode.setLeft(NewNode);
						break;
					}
					else itrNode = itrNode.getLeft();
				}
				else if(compareNum > 0){
					if(itrNode.getRight() == null) {
						NewNode.setParent(itrNode);
						itrNode.setRight(NewNode);
						break;
					}
					else itrNode = itrNode.getRight();
				}
				else {
					itrNode.pushList(NewNode.getHeadListNode());
					break;
				}
			}
		}
		if(SuperParent != null) CalcBalanceFactor(SuperParent);

		TreeNode detNode = detectUnbalanced(NewNode);

		if(detNode != null) {
			MakeTreeBalanced(detNode);
		}
		if(SuperParent != null) CalcBalanceFactor(SuperParent);
	}
	public TreeNode detectUnbalanced(TreeNode Curr) {
		TreeNode detectNode = Curr;
		
		int balance = getBalFactor(detectNode.getLeft()) - getBalFactor(detectNode.getRight());
		
		if(balance > 1 || balance < -1){
			return detectNode;
		}
		
		if(detectNode.getParent() == null) return null;
		
		detectNode = detectUnbalanced(detectNode.getParent());
		
		return detectNode;
	}
	public void RotateLeft(TreeNode Curr){
		if(Curr.getParent() != null && Curr.getParent().getLeft() == Curr) Curr.getParent().setLeft(Curr.getRight());
		else if(Curr.getParent() != null && Curr.getParent().getRight() == Curr) Curr.getParent().setRight(Curr.getRight());
		if(Curr == SuperParent) {
			SuperParent = Curr.getRight();
		}
		Curr.getRight().setParent(Curr.getParent());
		Curr.setParent(Curr.getRight());
		Curr.setRight(Curr.getParent().getLeft());
		Curr.getParent().setLeft(Curr);
		if(Curr.getRight() != null) Curr.getRight().setParent(Curr);
	}
	public void RotateRight(TreeNode Curr){
		if(Curr.getParent() != null && Curr.getParent().getLeft() == Curr) Curr.getParent().setLeft(Curr.getLeft());
		else if(Curr.getParent() != null && Curr.getParent().getRight() == Curr) {
			Curr.getParent().setRight(Curr.getLeft());
		}
		if(Curr == SuperParent) {
			SuperParent = Curr.getLeft();
		}
		Curr.getLeft().setParent(Curr.getParent());
		Curr.setParent(Curr.getLeft());
		Curr.setLeft(Curr.getParent().getRight());
		Curr.getParent().setRight(Curr);
		if(Curr.getLeft() != null) {
			Curr.getLeft().setParent(Curr);
		}
	}
	public void CalcBalanceFactor(TreeNode Curr) {
		if(Curr == null) return;
		Curr.setBalanceFactor(maxFactor(Curr));
		CalcBalanceFactor(Curr.getLeft());
		CalcBalanceFactor(Curr.getRight());
		
	}
	public int maxFactor(TreeNode Curr) {
		if(Curr == null) {
			return 0;
		}
		if(Curr.getLeft() == null && Curr.getRight() == null) return 1;
		else {
			int lFactor = 0;
			int rFactor = 0;
			if(Curr.getLeft() != null) lFactor = maxFactor(Curr.getLeft());
			if(Curr.getRight() != null) rFactor = maxFactor(Curr.getRight());
			
			if(lFactor > rFactor) return (lFactor + 1);
			else return (rFactor + 1);
		}
	}
	
	public void MakeTreeBalanced(TreeNode Curr){
		int balance = getBalFactor(Curr.getLeft()) - getBalFactor(Curr.getRight());
		TreeNode leftNode = Curr.getLeft();
		TreeNode rightNode = Curr.getRight();
		
		if(balance > 1 && getBalFactor(leftNode.getLeft()) >= getBalFactor(leftNode.getRight())) { 
			RotateRight(Curr);
		}
		else if((balance < -1 && getBalFactor(rightNode.getRight()) >= getBalFactor(rightNode.getLeft()))) {
			RotateLeft(Curr);
		}
		else if((balance > 1 && getBalFactor(leftNode.getLeft()) < getBalFactor(leftNode.getRight()))) {
			RotateLeft(Curr.getLeft());
			RotateRight(Curr);
		}
		else if((balance < -1 && getBalFactor(rightNode.getRight()) < getBalFactor(rightNode.getLeft()))) {
			RotateRight(rightNode);
			RotateLeft(Curr);
		}
	}
	public int getBalFactor(TreeNode Curr) {
		if(Curr == null) return 0;
		return Curr.getBalanceFactor();
	}
	public void PreOrderTraverse(TreeNode Start){
		if(Start == null) return;
		
		if(Start != SuperParent) System.out.print(" ");
		System.out.print(Start.getPattern());
		
		PreOrderTraverse(Start.getLeft());
		PreOrderTraverse(Start.getRight());
	}
	public void PrintByHashValue(int HashValue){
		if(SuperParent == null) {
			System.out.println("Empty!");
			return;
		}
		PreOrderTraverse(SuperParent);
		System.out.print("\n");
	}
}

class TreeNode{
	private int BalanceFactor = 0;

	// TreeNode has subsequence & positions as its element
	// ex) subsequence = 'AATCGC'
	// ex) positions = [(1, 1), (1, 7), (2, 5)]
	private String subsequence;

	// This is an example of implementation of "positions" with linkedlist
	// you can implement {positions|(student_id, start_index)} with any data structures as you want
	private ListNode head=null;
	private ListNode tail=null;
	//////////////////////////////////

	private TreeNode parent=null;
	private TreeNode left=null;
	private TreeNode right=null;

	public TreeNode(){
		initList();
	}
	public TreeNode(String Pattern){
		initList(Pattern);
	}
	public TreeNode(ListNode NewNode){
		initList();
		pushList(NewNode);
	}
	public void initList(){
		this.BalanceFactor = 0;
	}
	public void initList(String Pattern){
		this.subsequence = Pattern;
		this.BalanceFactor = 0;
	}
	public void pushList(ListNode NewNode){
		if(head == null) {
			head = NewNode;
			tail = NewNode;
		}
		else {
			ListNode itr = head;
			while(itr != null) {
				int newId = NewNode.getStudent_id();
				int newPos = NewNode.getStart_pos();
				int itrId = itr.getStudent_id();
				int itrPos = itr.getStart_pos();
				
				if(newId < itrId || ((newId == itrId) && (newPos < itrPos))) {
					itr.getPrev().setNext(NewNode);
					NewNode.setPrev(itr.getPrev());
					NewNode.setNext(itr);
					itr.setPrev(NewNode);
				}
				else itr = itr.getNext();
			}
			NewNode.setNext(null);
			NewNode.setPrev(tail);
			tail.setNext(NewNode);
			tail = NewNode;	
		}
	}

	public void setParent(TreeNode Parent){
		this.parent=Parent;
	}
	public void setLeft(TreeNode Left){
		this.left=Left;
	}
	public void setRight(TreeNode Right){
		this.right=Right;
	}
	public void SetPattern(String Pattern){
		this.subsequence = new String(Pattern);
	}
	public void setBalanceFactor(int newBalanceFactor){
		BalanceFactor = newBalanceFactor;
	}
	public String getPattern(){
		return this.subsequence;
	}
	public ListNode getHeadListNode() {
		return head;
	}
	public TreeNode getParent(){
		return this.parent;
	}
	public TreeNode getLeft(){
		return this.left;
	}
	public TreeNode getRight(){
		return this.right;
	}
	public int getBalanceFactor(){
		return BalanceFactor;
	}

	public void PrintNode(){
		ListNode itr = head;
		while(itr != null) {
			System.out.printf("(%d, %d)", itr.getStudent_id(), itr.getStart_pos());
			itr = itr.getNext();
		}
	}
}

class ListNode{
	private int student_id;
	private int start_pos;

	private ListNode next=null;
	private ListNode prev=null;

	public ListNode(){
	}
	public ListNode(int student_id, int start_pos){
		this.student_id =student_id;
		this.start_pos =start_pos;
	}
	public void setNext(ListNode NextNode){this.next=NextNode;}
	public void setPrev(ListNode PrevNode){this.prev=PrevNode;}
	public int getStudent_id(){return student_id;}
	public int getStart_pos(){return start_pos;}
	public ListNode getNext(){return next;}
	public ListNode getPrev(){return prev;}
}