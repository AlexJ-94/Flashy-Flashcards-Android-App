package alexj_94.flashyflashcards;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import alexj_94.flashyflashcards.database.FlashCardDbSchema;
import alexj_94.flashyflashcards.database.FlashCardDbSchema.FlashCardTable;

/**
 * Created by Alex on 10/12/2015.
 */
public class FlashCardBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "flashcardBase.db";

    public FlashCardBaseHelper(Context context) { super(context, DATABASE_NAME, null, VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + FlashCardTable.NAME + "(" + " _id integer primary key autoincrement, " +
                FlashCardTable.Cols.UUID + ", " +
                FlashCardTable.Cols.QUESTION + ", " +
                FlashCardTable.Cols.ANSWER + ", " +
                FlashCardTable.Cols.FOLDERNAME + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
