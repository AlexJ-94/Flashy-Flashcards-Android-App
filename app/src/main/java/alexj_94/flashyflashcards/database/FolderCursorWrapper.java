package alexj_94.flashyflashcards.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import alexj_94.flashyflashcards.Folder;


/**
 * Created by Xander on 11/23/2015.
 */
public class FolderCursorWrapper extends CursorWrapper {
    public FolderCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Folder getFolder(){
        String uuidString = getString(getColumnIndex(FolderDbSchema.FolderTable.Cols.UUID));
        String folderName = getString(getColumnIndex(FolderDbSchema.FolderTable.Cols.FOLDER));

        Folder folder = new Folder(UUID.fromString(uuidString));
        folder.setFolderName(folderName);


        return folder;
    }
}
