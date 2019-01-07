package puzzle;

// TODO: Auto-generated Javadoc
/**
 * The Enum SplitWords.
 */
public enum SplitWords {

    	MORETHAN("more",">"),
    	LESSTHAN("less","<"),
    	HIGHERTHAN("higher",">"),
    	LOWERTHAN("lower","<"),
    	AFTER("after",">"),
    	BEFORE("before","<"),
    	LONGERTHAN("longer",">"),
    	SHORTERTHAN("shorter","<"),
    	ISNOT("is not", "!="),
    	ISNT("isn't", "!="),
    	IS(" is ", "="),
    	ARE(" are ", "="),
	    WAS("was", "="),
	    WASNOT("was not", "!="),
	    WERE("were", "="),
		WERENOT ("were not", "!="),
		GOES("goes", "="),
		WONTGO("won't go", "!=");

    	private final String word;
    	private final String eq;

	    /**
    	 * Instantiates a new split words.
    	 *
    	 * @param word the word
    	 * @param eq the eq
    	 */
    	private SplitWords(String word, String eq) {
	        this.word = word;
	        this.eq = eq;
	    }

	    /**
    	 * Gets the word.
    	 *
    	 * @return the word
    	 */
    	public String getWord() {
	        return word;
	    }

	    /**
    	 * Gets the equation.
    	 *
    	 * @return the equation
    	 */
    	public String getEquation() {
	        return eq;
	    }
	    

}
