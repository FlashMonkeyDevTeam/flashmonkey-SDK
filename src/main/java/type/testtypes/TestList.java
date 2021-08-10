/*
 * Copyright (c) 2019 - 2021. FlashMonkey Inc. (https://www.flashmonkey.xyz) All rights reserved.
 *
 * License: This is for internal use only by those who are current employees of FlashMonkey Inc, or have an official
 *  authorized relationship with FlashMonkey Inc..
 *
 * DISCLAIMER OF WARRANTY.
 *
 * COVERED CODE IS PROVIDED UNDER THIS LICENSE ON AN "AS IS" BASIS, WITHOUT WARRANTY OF ANY
 *  KIND, EITHER EXPRESS OR IMPLIED, INCLUDING, WITHOUT LIMITATION, WARRANTIES THAT THE COVERED
 *  CODE IS FREE OF DEFECTS, MERCHANTABLE, FIT FOR A PARTICULAR PURPOSE OR NON-INFRINGING. THE
 *  ENTIRE RISK AS TO THE QUALITY AND PERFORMANCE OF THE COVERED CODE IS WITH YOU. SHOULD ANY
 *  COVERED CODE PROVE DEFECTIVE IN ANY RESPECT, YOU (NOT THE INITIAL DEVELOPER OR ANY OTHER
 *  CONTRIBUTOR) ASSUME THE COST OF ANY NECESSARY SERVICING, REPAIR OR CORRECTION. THIS
 *  DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE.  NO USE OF ANY COVERED
 *  CODE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS DISCLAIMER.
 *
 */

package type.testtypes;

import org.slf4j.LoggerFactory;

import java.util.BitSet;
import java.util.stream.IntStream;

/**
 * An ordered array of TestCards. The array index is used to get the
 * card from the array. A testType's testBSet contains the index for
 * its type. That index is used during the read session
 * and matches this array. See notes for index relationships.
 * To add a Test class
 * add it's index into the getBitSet bitset array.
 * @TODO convert testTypes to singletons. and Lazily instantiate them.
 */
public final class TestList {

    private static final ch.qos.logback.classic.Logger LOGGER = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(TestList.class);


    // The order is important. Cards should be added to
    // the bottom of the list unless testtypes are modified.
    // The index set in the bitSet of each test correlates
    // with this array.
    public static final GenericTestType[] TEST_TYPES = {
            new AIMode(),
            MultiChoice.getInstance(),
            //new MultiAnswer(),
            QandA.QandATest.getInstance(),
            //TrueOrFalse.getInstance(),
            new FillnTheBlank(),
            //new TurninVideo(),
            //new TurninAudio(),
            //new WriteIn(),
            //new TurnInDraw(),
            //new DrawOnImage(),
            new MathCard(),
            new GraphCard(),
            NoteTaker.getInstance(),// end
    };

    /**
     * Returns the
     * @param n = testType
     * @return
     */
    public static GenericTestType selectTest(Integer n)
    {
        if(n == 31) {
            return TEST_TYPES[0];
        }

        LOGGER.debug("selectTest and n: {}", n);

        // least significant bit
        int idx = (int)((Math.log10(n & -n)) / Math.log10(2));

        LOGGER.debug("Least significant bit: <{}>", idx);

        // Each test type is accessed from the TESTS array using the cards BitSet to
        // provide the index
        return TEST_TYPES[idx];
    }
}
