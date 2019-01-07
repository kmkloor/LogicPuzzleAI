package puzzle;


import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

// TODO: Auto-generated Javadoc
/**
 * The puzzle's categories. Each category is its own object with a list of items. 
 */
@XmlRootElement(name = "category")
@XmlAccessorType (XmlAccessType.FIELD)
public class Category {
	
	/** The category's name. */
	@XmlElement(name = "name")
	private String name;
	
	/** The list of category items. */
	@XmlElement(name = "item")
	private  List<String> items;
	
	/** A step is used for numerical categories. If the items have a measurable increase or decrease that value is the step.
	 * For example, a category of prices would need to recognize the ordered value of items to 
	 * respond to clues like "more than" or "at least $2.00"
	 */
	private float step;

	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the items.
	 *
	 * @return the items
	 */
	public List<String> getItems() {
		return items;
	}
	
	/**
	 * Sets the items.
	 *
	 * @param items the new items
	 */
	public void setItems(List<String> items) {
		this.items = items;
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
	 * Item.
	 *
	 * @param i the i
	 * @return the string
	 */
	public String item(int i){
		return getItems().get(i);
	}
	

	

	

}
