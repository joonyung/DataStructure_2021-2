import java.io.*;
import java.util.StringTokenizer;

public class Main {
	public static void main(String[] args) {
		
		DepartmentLinkedList attendance_list = new DepartmentLinkedList();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		while(true)
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
				else if(option.equals("P"))
				{
					attendance_list.PrintAll();
				}
				else if(option.equals("I"))
				{
					String department = st.nextToken();
					String student_name = st.nextToken();
					String student_id = st.nextToken();
					attendance_list.insertOrdered(department, student_name, student_id);
				}
				else if(option.equals("D"))
				{
					String department = st.nextToken();
					String student_id = st.nextToken();
					attendance_list.delete(department, student_id);
				}
			}
			catch (Exception e)
			{
				System.out.println("잘못된 입력입니다. I,D,P,Q 네가지 옵션 중 하나를 선택하고, 올바른 인자를 입력하세요. 오류 : " + e.toString());
			}
		}
	}
}


class DepartmentLinkedList{
	
	private DepartmentNode DepartmentHead;
	
	public void insertOrdered(String department, String student_name, String student_id){
		//StudentNode newSdtNode = new StudentNode();
		if(DepartmentHead == null) {
			DepartmentHead = new DepartmentNode();
			DepartmentHead.setDepartmentName(department);
			DepartmentHead.getStudentList().insertOrdered(student_name, student_id);
			DepartmentHead.setNext(null);
		}
		else {
			DepartmentNode currentNode = new DepartmentNode();
			currentNode = DepartmentHead;
			
			while(currentNode != null) {
				if(DepartmentHead.getDepartmentName().compareTo(department) > 0) {
					DepartmentNode newHead = new DepartmentNode();
					newHead.setDepartmentName(department);
					newHead.getStudentList().insertOrdered(student_name, student_id);
					newHead.setNext(DepartmentHead);
					DepartmentHead = newHead;
					break;
				}
				else if (currentNode.getDepartmentName().compareTo(department) == 0) {
					currentNode.getStudentList().insertOrdered(student_name, student_id);
					break;
					
				}
				else if (currentNode.getDepartmentName().compareTo(department) < 0 && currentNode.getNext() != null && currentNode.getNext().getDepartmentName().compareTo(department) > 0) {
					DepartmentNode InsertNode = new DepartmentNode();
					InsertNode.setDepartmentName(department);
					InsertNode.getStudentList().insertOrdered(student_name, student_id);
					InsertNode.setNext(currentNode.getNext());
					currentNode.setNext(InsertNode);
					break;
				}
				else if (currentNode.getNext() == null) {
					DepartmentNode newDptNode = new DepartmentNode();
					newDptNode.setDepartmentName(department);
					newDptNode.getStudentList().insertOrdered(student_name, student_id);
					newDptNode.setNext(null);
					currentNode.setNext(newDptNode);
					break;
				}
				else currentNode = currentNode.getNext();
			}
		}
		
	}
	
	public void delete(String department, String student_id){
		DepartmentNode currentNode = new DepartmentNode();
		DepartmentNode forDelNode = new DepartmentNode();
		currentNode = DepartmentHead;
		forDelNode = DepartmentHead;
		
		while(currentNode != null) {
			if(currentNode.getDepartmentName().compareTo(department) == 0) {
				currentNode.getStudentList().delete(student_id);
				break;
			}
			else {
				currentNode = currentNode.getNext();
			}
		}
		
		
		if (DepartmentHead.getStudentList().getStudentHead() == null) {
			DepartmentHead = DepartmentHead.getNext();
		}
		while(forDelNode != null && forDelNode.getNext() != null) {
			if(forDelNode.getNext().getStudentList().getStudentHead() == null) {
				forDelNode.setNext(forDelNode.getNext().getNext());
			}
			else forDelNode = forDelNode.getNext().getNext();
		}
		
		
		
	}
	
	public void PrintAll(){	
		DepartmentNode currentNode = new DepartmentNode();
		currentNode = DepartmentHead;

		if(DepartmentHead == null) {
			System.out.println("Empty!\n");

		}
		else {
			while(currentNode != null) {
				currentNode.getStudentList().PrintAll(currentNode.getDepartmentName());
				currentNode = currentNode.getNext();
			}
			System.out.println("End!\n");
		}
		
	}
}


class StudentLinkedList{
	
	private StudentNode StudentHead;

	public void insertOrdered(String student_name, String student_id) {
		if(StudentHead == null) {
			StudentHead = new StudentNode();
			StudentHead.setStudentName(student_name);
			StudentHead.setStudentId(student_id);
			StudentHead.setNext(null);
		}
		else {
			
			StudentNode currentNode = new StudentNode();
			currentNode = StudentHead;
			
			
			while(currentNode != null) {
				if(StudentHead.getStudentId().compareTo(student_id) > 0) {
					StudentNode newHead = new StudentNode();
					newHead.setStudentName(student_name);
					newHead.setStudentId(student_id);
					newHead.setNext(StudentHead);
					StudentHead = newHead;
					break;
				}
				else if (currentNode.getStudentId().compareTo(student_id) < 0 && currentNode.getNext() != null && currentNode.getNext().getStudentId().compareTo(student_id) > 0) {
					StudentNode InsertNode = new StudentNode();
					InsertNode.setStudentName(student_name);
					InsertNode.setStudentId(student_id);
					InsertNode.setNext(currentNode.getNext());
					currentNode.setNext(InsertNode);
					break;
				}
				else if (currentNode.getStudentName().compareTo(student_name) == 0 && currentNode.getStudentId().compareTo(student_id) == 0) break;
				else if (currentNode.getNext() == null) {
					StudentNode newStdNode = new StudentNode();
					newStdNode.setStudentName(student_name);
					newStdNode.setStudentId(student_id);
					newStdNode.setNext(null);
					currentNode.setNext(newStdNode);
					break;
				}
				else currentNode = currentNode.getNext();
			}
		}
	}

	public void delete(String student_id){
		StudentNode currentNode = new StudentNode();
		currentNode = StudentHead;
		
		if(StudentHead.getStudentId().compareTo(student_id) == 0) {
			StudentHead = StudentHead.getNext();
		}
		else {
			while(currentNode != null && currentNode.getNext() != null) {
				if(currentNode.getNext().getStudentId().compareTo(student_id) == 0) {
					currentNode.setNext(currentNode.getNext().getNext());	
					break;
				}
				else currentNode = currentNode.getNext();
			}
		}
		
	}
	
	public void PrintAll(String department) {
		StudentNode currentNode = new StudentNode();
		currentNode = StudentHead;
		while(currentNode != null) {
			System.out.println(String.format("(%s, %s, %s)", department, currentNode.getStudentName(), currentNode.getStudentId()));
			currentNode = currentNode.getNext();
		}
	}
	
	public StudentNode getStudentHead() {
		return StudentHead;
	}
}

class DepartmentNode{
	private String DepartmentName;
	private StudentLinkedList StudentList;
	private DepartmentNode next;
	
	public DepartmentNode() {
		this(null, null, null);
	}
	
	public DepartmentNode(String dptname, StudentLinkedList studentlist, DepartmentNode n) {
		DepartmentName = dptname;
		StudentList = studentlist;
		next = n;		
	}
	
	public String getDepartmentName() {
		return DepartmentName;
	}
	
	public StudentLinkedList getStudentList() {
		if(StudentList == null) {
			StudentList = new StudentLinkedList();
		}
		return StudentList;
	}
	
	public DepartmentNode getNext() {
		return next;
	}
	
	public void setDepartmentName(String newName) {
		DepartmentName = newName;
	}

	public void setNext(DepartmentNode newNext) {
		next = newNext;
	}
}

class StudentNode{
	private String studentName;
	private String studentId;
	private StudentNode next;
	
	public StudentNode() {
		this(null, null, null);
	}
	
	public StudentNode(String name, String id, StudentNode n) {
		studentName = name;
		studentId = id;
		next = n;
	}
	
	public String getStudentName() {
		return studentName;
	}
	
	public String getStudentId() {
		return studentId;
	}
	
	public StudentNode getNext() {
		return next;
	}
	
	public void setStudentName(String name) {
		studentName = name;
	}
	
	public void setStudentId(String id) {
		studentId = id;
	}
	public void setNext(StudentNode newNode) {
		next = newNode;
	}
	
}

