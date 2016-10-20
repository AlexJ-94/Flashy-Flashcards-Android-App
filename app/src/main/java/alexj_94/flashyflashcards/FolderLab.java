package alexj_94.flashyflashcards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import alexj_94.flashyflashcards.database.FolderCursorWrapper;
import alexj_94.flashyflashcards.database.FolderDbSchema;
import alexj_94.flashyflashcards.database.FolderDbSchema.FolderTable;

/**
 * Created by Xander on 11/23/2015.
 */
public class FolderLab {
    private static FolderLab sFolderLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static FolderLab get(Context context) {
        if (sFolderLab == null) {
            sFolderLab = new FolderLab(context);
        }
        return sFolderLab;
    }

    private FolderLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new FolderBaseHelper(mContext).getWritableDatabase();
    }

    public void addFolder(Folder f) {
        ContentValues values = getContentValues(f);

        mDatabase.insert(FolderTable.NAME, null, values);
    }

    public void deleteFolder(UUID folderId) {
        String uuidString = folderId.toString();

        mDatabase.delete(FolderTable.NAME, FolderTable.Cols.UUID + " = ?", new String[]{uuidString});
    }

    public List<Folder> getFolders() {
        List<Folder> folders = new ArrayList<>();

        FolderCursorWrapper cursor = queryFolder(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                folders.add(cursor.getFolder());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return folders;
    }

    public Folder getFolder(UUID id) {
        FolderCursorWrapper cursor = queryFolder(
                FolderTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );

        try{
            if (cursor.getCount() == 0) {
            return null;
            }

            cursor.moveToNext();
            return cursor.getFolder();
        } finally {
            cursor.close();
        }
    }

    public void updateFolder(Folder folder) {
        String uuidString = folder.getId().toString();
        ContentValues values = getContentValues(folder);

        mDatabase.update(FolderTable.NAME, values, FolderTable.Cols.UUID + " = ?" , new String[]{uuidString});
    }

    private static ContentValues getContentValues(Folder folder) {
        ContentValues values = new ContentValues();
        values.put(FolderTable.Cols.UUID, folder.getId().toString());
        values.put(FolderTable.Cols.FOLDER, folder.getFolderName());

        return values;
    }

    private FolderCursorWrapper queryFolder(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                FolderTable.NAME,
                null, //Columns - null selects all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null //orderBy
        );
        return new FolderCursorWrapper(cursor);
    }
}
