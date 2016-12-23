package net.latin.client.validation.utils;



/**
 * A class for validating 10 digit ISBN codes.
 * Based on this
 * <a href="http://www.isbn.org/standards/home/isbn/international/html/usm4.htm">
 * algorithm</a>
 *
 * @version $Revision$ $Date$
 * @since Validator 1.2.0
 *
 * tuneada por FedeT
 */
public class ISBNValidator {

    private static final String SEP = "(\\-|\\s)";
    private static final String GROUP = "(\\d{1,5})";
    private static final String PUBLISHER = "(\\d{1,7})";
    private static final String TITLE = "(\\d{1,6})";
    private static final String CHECK = "([0-9X])";

    /**
     * ISBN consists of 4 groups of numbers separated by either dashes (-)
     * or spaces.  The first group is 1-5 characters, second 1-7, third 1-6,
     * and fourth is 1 digit or an X.
     */
    private static final String ISBN_PATTERN =
        "^" + GROUP + SEP + PUBLISHER + SEP + TITLE + SEP + CHECK + "$";

    /**
     * Default Constructor.
     */
    public ISBNValidator() {
        super();
    }

    /**
     * If the ISBN is formatted with space or dash separators its format is
     * validated.  Then the digits in the number are weighted, summed, and
     * divided according to the ISBN algorithm. This method accepts formatted or raw ISBN codes.
     *
     * @param isbn Candidate ISBN number to be validated. <code>null</code> is
     * considered invalid.
     * @return true if the string is a valid ISBN code.
     */
    public static boolean isValid(String isbn) {
        if (isbn == null || isbn.length() < 10 || isbn.length() > 17) {
            return false;
        }

        isbn = isbn.replace('x', 'X');

        if (isFormatted(isbn) && !isValidPattern(isbn)) {
            return false;
        }

        isbn = clean(isbn);
        if (isbn.length() != 10 && isbn.length()!=13) {
            return false;
        }

        //es de 10 digitos
        if(isbn.length() == 10){
        	return (sum_10digits(isbn)%11 == toInt(isbn.charAt(isbn.length()-1)));
        }

        //es de 13 digitos
        return (10-sum_13digits(isbn)%10 == toInt(isbn.charAt(isbn.length()-1)));

    }

    /**
     * Returns the sum of the weighted ISBN characters for 10 digits ISBN.
     */
    private static int sum_10digits(String isbn) {
    	int total = 0;

    	for(int i = 0; i< 9; i++){
    		int weight = i + 1;
    		total += (weight * toInt(isbn.charAt(i)));
    	}

    	return total;
    }

    /**
     * Returns the sum of the weighted ISBN characters for 13 digits ISBN.
     */
    private static int sum_13digits(String isbn) {
    	int total = 0;
    	boolean flag = true; //para no tener que fijarse por par/impar

    	for(int i = 0; i<12; i++){
    		int weight;

    		if(flag){
    			weight = 1;
    			flag = false;
    		} else {
    			weight = 3;
    			flag = true;
    		}

    		total += (weight * toInt(isbn.charAt(i)));
    	}

    	return total;
    }


    /**
     * Removes all non-digit characters except for 'X' which is a valid ISBN
     * character.
     */
    private static String clean(String isbn) {
        StringBuffer buf = new StringBuffer(isbn.length());

        for (int i = 0; i < isbn.length(); i++) {
            char digit = isbn.charAt(i);
            if (Character.isDigit(digit) || (digit == 'X')) {
                buf.append(digit);
            }
        }

        return buf.toString();
    }

    /**
     * Returns the numeric value represented by the character.  If the
     * character is not a digit but an 'X', 10 is returned.
     */
    private static int toInt(char ch) {
    	return (ch == 'X') ? 10 : Integer.valueOf(String.valueOf(ch));
//        return (ch == 'X') ? 10 : Integer.valueOf(ch);
    }

    /**
     * Returns true if the ISBN contains one of the separator characters space
     * or dash.
     */
    private static boolean isFormatted(String isbn) {
        return ((isbn.indexOf('-') != -1) || (isbn.indexOf(' ') != -1));
    }

    /**
     * Returns true if the ISBN is formatted properly.
     */
    private static boolean isValidPattern(String isbn) {
    	if(isbn.length() >= 13 && "978".equals(isbn.substring(0, 3))){
    		//es isbn de 13 digitos

    		if(isbn.charAt(3) == '-'){
    			return isbn.substring(4).matches(ISBN_PATTERN);
    		}

    		return isbn.substring(3).matches(ISBN_PATTERN);
    	}

    	//es isbn de 10 digitos
    	return isbn.matches(ISBN_PATTERN);
    }

}
