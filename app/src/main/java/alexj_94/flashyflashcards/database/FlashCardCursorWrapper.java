package alexj_94.flashyflashcards.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import alexj_94.flashyflashcards.FlashCard;
import alexj_94.flashyflashcards.database.FlashCardDbSchema.FlashCardTable;

/**
 * Created by Alex on 10/12/2015.
 */
public class FlashCardCursorWrapper extends CursorWrapper {
    public FlashCardCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public FlashCard getFlashCard(){
        String uuidString = getString(getColumnIndex(FlashCardTable.Cols.UUID));
        String question = getString(getColumnIndex(FlashCardTable.Cols.QUESTION));
        String answer = getString(getColumnIndex(FlashCardTable.Cols.ANSWER));
        String folderName = getString(getColumnIndex(FlashCardTable.Cols.FOLDERNAME));

        FlashCard flashCard = new FlashCard(UUID.fromString(uuidString));
        flashCard.setQuestion(question);
        flashCard.setAnswer(answer);
        flashCard.setFolderName(folderName);

        return flashCard;
    }
}
