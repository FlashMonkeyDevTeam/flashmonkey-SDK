package type.testtypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Assists with providing a varied distribution of HashTables or HashMaps from Test to Test. Provides
 * a hashCode function that takes an integer to choose the hashing constant. To use this class
 * provide the constructor with a string to hash, and an integer 0 - 4; and use the classes
 * getHashCode to return the hashCode
 *
 * @author Lowell Stadelman
 */
public final class FMHashCode {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FMHashCode.class);
    public FMHashCode() { /* do nothing */ }

    /**
     * Returns a hash code for this string. The hash code for a
     * String object is computed as
     *
     * s[0]* C^(n-1) + s[1] * C^(n-2) + ... + s[n-1]
     *
     * using int arithmetic:
     * Where 'C' is a constant of
     * 31, 33, 37, 39, or 41 and 's[i]' is the
     * i'th character of the string, 'n' is the length of
     * the string, and '^' indicates exponentiation.
     * (The hash value of the empty string is zero.)
     *
     * @param obj Object that gets a new hashCode
     * @param zeroThruFour is a number from zero through four inclusive.
     * @return  returns a hash code value for this object.
     */
    public static int getHashCode(Object obj, int zeroThruFour) {

        int[] primeAry = {9,3,7,5,0};
        int hashCode;
        int key = obj.hashCode();   // hashed memory address
        //hashCode = key ^ (key >> primeAry[zeroThruFour]);
        hashCode = key >> primeAry[zeroThruFour];
        //LOGGER.debug(" primeAry[zeroThruFour] " + primeAry[zeroThruFour]);
        return hashCode;
    }
}
