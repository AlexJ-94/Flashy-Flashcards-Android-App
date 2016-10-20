package alexj_94.flashyflashcards;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import alexj_94.flashyflashcards.database.FlashCardDbSchema;
import alexj_94.flashyflashcards.database.FolderDbSchema;

/**
 * Created by Xander on 11/23/2015.
 */
public class FolderBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "folderBase.db";

    public FolderBaseHelper(Context context) { super(context, DATABASE_NAME, null, VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + FolderDbSchema.FolderTable.NAME + "(" + " _id integer primary key autoincrement, " +
                FolderDbSchema.FolderTable.Cols.UUID + ", " +
                FolderDbSchema.FolderTable.Cols.FOLDER + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
