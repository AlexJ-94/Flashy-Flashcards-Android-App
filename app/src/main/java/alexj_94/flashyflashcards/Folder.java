package alexj_94.flashyflashcards;

import java.util.UUID;

/**
 * Created by Xander on 11/23/2015.
 */
public class Folder {
    private UUID mId;
    private String mFolder;

    public Folder() {
        this(UUID.randomUUID());
    }

    public Folder(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public String getFolderName() {
        return mFolder;
    }

    public void setFolderName(String folder) {
        mFolder = folder;
    }

}
