package common;

/**
 * many of the static fields used
 * are stored here for development
 * and testing.
 */
public enum FMFields {
    SHAPES_FULL_NAME {
        public String get() {
            return path + shapesFileName;
        }
    },
    DECK_FULL_NAME {
        public String get() {
            return path + deckName;
        }
    },
    DECK_NAME_ONLY {
        public String get() { return deckName;}
    },
    USER_NAME {
        public String get() {
            return userOrigEmail;
        }
    };

    // *** fields ***
    private static String path = "../";
    private static String deckName = "testDeckName.dat";
    private static String shapesFileName = "testShapesName.dat";
    private static String userOrigEmail = "idk@idk.com";

    public abstract String get();
}
