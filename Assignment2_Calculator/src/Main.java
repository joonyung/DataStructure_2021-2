import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class Main {
	public static void main(String[] args) {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StackCalculator calc = new StackCalculator();
		while(true)
		{
			try
			{
				String infix = br.readLine();
				if(infix.equals("Q") || infix.equals("q"))
				{
					break;
				}
				else
				{
					calc.calculate(infix);
				}
			}
			catch (Exception e)
			{
				System.out.println("Wrong Expression!");
			}
		}
	}
}

class StackCalculator{
	
	private boolean wrong = false;
	
	public long calculate(String infix) {
		long val = 0;
		String postfix = InfixToPostfix(infix);
		val = CalPostfix(postfix);
		if(wrong) {
			System.out.println("Wrong Expression!\n");
			return val;
		}
		else {
			System.out.println(postfix);
			System.out.println(val);
		}
		return val;
	}
	
	public String InfixToPostfix(String infix) {
		
		String postfix = new String();
		OpsStack opstack = new OpsStack();
		boolean FlagNumEnd = false;
		boolean FlagAmpEnd = false;
		boolean FlagBiEnd = true;
		
		for(int i = 0; i < infix.length(); i++) {
			switch(infix.charAt(i)) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				postfix = postfix.concat(Character.toString(infix.charAt(i)));
				FlagNumEnd = true;
				FlagBiEnd = false;
				break;
			case '(':
				if(FlagNumEnd) {
					postfix = postfix.concat(" ");
					FlagNumEnd = false;
				}
				opstack.push(Character.toString(infix.charAt(i)));
				break;
			case ')':
				if(FlagNumEnd) {
					postfix = postfix.concat(" ");
					FlagNumEnd = false;
				}
				while(!opstack.top().equals("(")) {
					postfix = postfix.concat(opstack.pop() + " ");
				}
				String TempPop = opstack.pop();
				break;
			case '+':
			case '/':
			case '%':
				if(FlagNumEnd) {
					postfix = postfix.concat(" ");
					FlagNumEnd = false;
					FlagBiEnd = true;
				}
				while((!opstack.isEmpty()) && OpPrec(Character.toString(infix.charAt(i))) <= OpPrec(opstack.top())) {
					postfix = postfix.concat(opstack.pop() + " ");
				}
					
				opstack.push(Character.toString(infix.charAt(i)));
				break;
			case '-':
				
				if(FlagNumEnd) {
					postfix = postfix.concat(" ");
					FlagNumEnd = false;
				}
				
				if(FlagBiEnd) {
					opstack.push("$");
				}
				else {
					while((!opstack.isEmpty()) && OpPrec(Character.toString(infix.charAt(i))) <= OpPrec(opstack.top())) {
						postfix = postfix.concat(opstack.pop() + " ");
					}
					
					opstack.push("-");
					FlagBiEnd = true;
				}
				break;
			case '*':
				if(FlagNumEnd) {
					postfix = postfix.concat(" ");
					FlagNumEnd = false;
				}
				if(FlagAmpEnd) {
					while((!opstack.isEmpty()) && OpPrec(Character.toString(infix.charAt(i))) <= OpPrec(opstack.top())) {
						postfix = postfix.concat(opstack.pop() + " ");
					}
					
					opstack.push("**");
					FlagAmpEnd = false;
					FlagBiEnd = true;
				}
				else {
					if(infix.charAt(i+1) == '*') FlagAmpEnd = true;
					else {
						while((!opstack.isEmpty()) && OpPrec(Character.toString(infix.charAt(i))) <= OpPrec(opstack.top())) {
							postfix = postfix.concat(opstack.pop() + " ");
						}
						
						opstack.push("*");
						FlagBiEnd = true;
					}
				}
				break;
			case ' ':
				if(FlagNumEnd) {
					postfix = postfix.concat(" ");
				}
				FlagNumEnd = false;
				FlagAmpEnd = false;
				break;
			}
			
		}
		
		if(FlagNumEnd) {
			postfix = postfix.concat(" ");
			FlagNumEnd = false;
		}
		while(!opstack.isEmpty()) {
			postfix = postfix.concat(opstack.pop() + " ");
		}
		
		return postfix;
	}
	
	public long CalPostfix(String postfix) {
		long result = 0;
		String[] postfixArray = postfix.split(" ");
		NumStack numstack = new NumStack();
		
		for(int i = 0; i < postfixArray.length; i++) {
			if(isNumber(postfixArray[i])) {
				long temp = Long.parseLong(postfixArray[i]);
				numstack.push(temp);
			}
			else {
				if(postfixArray[i].equals("$")) {
					long temp = numstack.pop();
					numstack.push(-temp);
				}
				else {
					long temp2 = numstack.pop();
					long temp1 = numstack.pop();
					if(postfixArray[i].equals("+")) numstack.push(temp1 + temp2);
					else if(postfixArray[i].equals("-")) numstack.push(temp1 - temp2);
					else if(postfixArray[i].equals("*")) numstack.push(temp1 * temp2);
					else if(postfixArray[i].equals("/")) numstack.push(temp1 / temp2);
					else if(postfixArray[i].equals("%")) numstack.push(temp1 % temp2);
					else if(postfixArray[i].equals("**")) numstack.push(power(temp1, temp2));
				}
			}
		}	
		
		if(numstack.size() != 1) wrong = true;
		
		result = numstack.pop();
		return result;
	}
	
	public long power(long a, long b) {
		long result = 1;
		for(int i = 0; i < b; i++) {
			result = result * a;
		}
		return result;
	}
	
	public boolean isNumber(String s){ 
		if(s == null || s.equals("")) {
			return false;
		}
		
		try {
			long temp = Long.parseLong(s);
			return true;
		}
		catch(NumberFormatException e) {
			return false;
		}
	}
	
	
	public int OpPrec(String op) {
		int prec = 0;
		
		switch(op) {
		case "+":
		case "-":
			prec = 1;
			break;
		case "*":
		case "/":
		case "%":
			prec = 2;
			break;
		case "**":
			prec = 3;
			break;
		case "$":
			prec = 4;
			break;
		}
		return prec;
	}
	
	
}


class OpsStack
{
	private LinkedList<String> OpsList;
	
	public OpsStack()
	{
		OpsList = new LinkedList<String>();
	}
	
	public boolean isEmpty() {
		return OpsList.isEmpty();
	}
	
	public int size() {
		return OpsList.size();
	}
	
	public void push(String s) {
		OpsList.addLast(s);
	}
	
	public String pop() {
		return OpsList.removeLast();
	}
	
	public String top() {
		return OpsList.getLast();
	}
}


class NumStack
{
	private LinkedList<Long> NumList;
	
	public NumStack()
	{
		NumList = new LinkedList<Long>();
	}
	
	public boolean isEmpty() {
		return NumList.isEmpty();
	}
	
	public int size() {
		return NumList.size();
	}
	
	public void push(Long s) {
		NumList.addLast(s);
	}
	
	public Long pop() {
		return NumList.removeLast();
	}
	
	public Long top() {
		return NumList.getLast();
	}
}
