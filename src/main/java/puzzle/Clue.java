package puzzle;


import java.util.List;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The clue class. This class is used to store the processed clue.
 */
public class Clue {

	/** The subject/s. */
	private List<String> subject;
	
	/** The step. */
	private float step;
	
	/** The equation. */
	private String equation;
	
	/** The object/s. */
	private  List<String> object;
	

	/**
	 * Gets the subject list.
	 *
	 * @return the subject list
	 */
	public List<String> getSubjectList() {
		return subject;
	}

	/**
	 * Sets the subject.
	 *
	 * @param subject the new subject
	 */
	public void setSubject(List<String> subject) {
		this.subject = subject;
	}

	/**
	 * Gets the step.
	 *
	 * @return the step
	 */
	public float getStep() {
		return step;
	}

	/**
	 * Sets the step.
	 *
	 * @param step the new step
	 */
	public void setStep(float step) {
		this.step = step;
	}

	/**
	 * Gets the equation.
	 *
	 * @return the equation
	 */
	public String getEquation() {
		return equation;
	}

	/**
	 * Sets the equation.
	 *
	 * @param equation the new equation
	 */
	public void setEquation(String equation) {
		this.equation = equation;
	}

	/**
	 * Gets the object list.
	 *
	 * @return the object list
	 */
	public List<String> getObjectList() {
		return object;
	}

	/**
	 * Sets the object.
	 *
	 * @param object the new object
	 */
	public void setObject (List<String>object) {
		this.object = object;
	}
	
	/**
	 * Shortcut to pull a certain subject based on placement. This code is used often and makes more complex equations too bulky. 
	 *
	 * @param i the item location
	 * @return the string
	 */
	public String subject(int i) {
			return getSubjectList().get(i);
	}
	
	/**
	 * Shortcut to pull a certain object based on placement. This code is used often and makes more complex equations too bulky. 
	 *
	 * @param i the item location
	 * @return the string
	 */
	public String object(int i) {
		return getObjectList().get(i);
}
	
	/**
	 * Instantiates a new clue.
	 *
	 * @param subject the subject
	 * @param step the step
	 * @param equation the equation
	 * @param object the object
	 */
	public Clue(List<String> subject, float step, String equation, List<String> object) {
		this.subject = subject;
		this.step = step;
		this.equation = equation;
		this.object = object;				
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for(String item:subject) {
			s.append("[" + item + "]");
		}
		if(step != 0) {
			s.append(" " + step);
		}
		if(!equation.isEmpty()) {
			s.append(equation);
		}
		for(String item:object) {
			s.append("[" + item + "]");
		}
		return s.toString();
	}
	
	



}
