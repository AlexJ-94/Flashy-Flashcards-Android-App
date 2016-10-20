package alexj_94.flashyflashcards.database;

/**
 * Created by Alex on 10/12/2015.
 */
public class FlashCardDbSchema {

    public static final class FlashCardTable {
        public static final String NAME = "flashcards";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String QUESTION = "question";
            public static final String ANSWER = "answer";
            public static final String FOLDERNAME = "folder_name";
        }
    }
}
