package alexj_94.flashyflashcards;

import java.util.UUID;

/**
 * Created by Alex on 10/12/2015.
 */
public class FlashCard {
    private UUID mId;
    private String mQuestion;
    private String mAnswer;
    private String mFolderName;

    public FlashCard() {
        this(UUID.randomUUID());
    }

    public FlashCard(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String question) {
        mQuestion = question;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String answer) {
        mAnswer = answer;
    }

    public String getFolderName() {
        return mFolderName;
    }

    public void setFolderName(String folderName) { mFolderName = folderName; }

}
