package alexj_94.flashyflashcards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import alexj_94.flashyflashcards.database.FlashCardCursorWrapper;
import alexj_94.flashyflashcards.database.FlashCardDbSchema;
import alexj_94.flashyflashcards.database.FlashCardDbSchema.FlashCardTable;
import alexj_94.flashyflashcards.database.FolderDbSchema;

/**
 * Created by Alex on 10/12/2015.
 */
public class FlashCardLab {
    private static FlashCardLab sFlashCardLab;
    private static final String TAG = "FlashCardLab";
    private static String mCurrentFolderName;

    private Context mContext;
    private SQLiteDatabase mDataBase;

    public static FlashCardLab get(Context context) {
        if (sFlashCardLab == null) {
            sFlashCardLab = new FlashCardLab(context);
        }
        return sFlashCardLab;
    }
    
    private FlashCardLab(Context context) {
        mContext = context.getApplicationContext();
        mDataBase = new FlashCardBaseHelper(mContext).getWritableDatabase();
    }

    public void addFlashCard(FlashCard f) {
        ContentValues values = getContentValues(f);

        mDataBase.insert(FlashCardTable.NAME, null, values);
    }

    public void deleteFlashCard(UUID flashCardId) {
        String uuidString = flashCardId.toString();

        mDataBase.delete(FlashCardTable.NAME, FlashCardTable.Cols.UUID + " = ?", new String[]{uuidString});
    }

    public static void setCurrentFolderName(String currentFolderName) {
        mCurrentFolderName = currentFolderName;
    }

    public List<FlashCard> getFlashCards() {
        List<FlashCard> flashCards = new ArrayList<>();

        FlashCardCursorWrapper cursor = queryFlashCards(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                flashCards.add(cursor.getFlashCard());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return flashCards;
    }

    public List<FlashCard> displayFlashCards() {
        List<FlashCard> flashCards = new ArrayList<>();

        FlashCardCursorWrapper cursor = queryFlashCards(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if (cursor.getString(4).equals(mCurrentFolderName)) {
                    flashCards.add(cursor.getFlashCard());
                }
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return flashCards;
    }

    public FlashCard getFlashCard(UUID id) {
        FlashCardCursorWrapper cursor = queryFlashCards(
                FlashCardTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getFlashCard();
        } finally {
            cursor.close();
        }
    }

    public void updateFlashCard(FlashCard flashCard) {
        String uuidString = flashCard.getId().toString();
        ContentValues values = getContentValues(flashCard);

        mDataBase.update(FlashCardTable.NAME, values, FlashCardTable.Cols.UUID + " = ?", new String[]{uuidString});
    }

    public static ContentValues getContentValues(FlashCard flashCard) {
        ContentValues values = new ContentValues();
        values.put(FlashCardTable.Cols.UUID, flashCard.getId().toString());
        values.put(FlashCardTable.Cols.QUESTION, flashCard.getQuestion());
        values.put(FlashCardTable.Cols.ANSWER, flashCard.getAnswer());
        values.put(FlashCardTable.Cols.FOLDERNAME, flashCard.getFolderName());

        return values;
    }

    private FlashCardCursorWrapper queryFlashCards(String whereClause, String[] whereArgs) {
        Cursor cursor = mDataBase.query(
                FlashCardTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new FlashCardCursorWrapper(cursor);
    }

}
