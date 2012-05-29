package knowledgeBase;

public class KBMachine {
	
	private String name;
	public enum type {SWITCH, USER };
	public type machineType;
	
	public KBMachine(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return "M: " +getName();
	}
	
	public boolean equals(Object kb) {
	       if (!(kb instanceof KBMachine))
	            return false;
	       KBMachine other = (KBMachine) kb;
		return other.name.equals(name);
	}
}
