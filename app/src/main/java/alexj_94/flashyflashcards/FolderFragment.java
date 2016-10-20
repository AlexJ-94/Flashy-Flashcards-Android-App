package alexj_94.flashyflashcards;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;
import java.util.UUID;

/**
 * Created by Xander on 11/23/2015.
 */
public class FolderFragment extends Fragment {
    private static final String ARG_FOLDER_ID = "folder_id";

    private Folder mFolder;
    private EditText mFolderNameField;

    public static FolderFragment newInstance(UUID folderId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_FOLDER_ID, folderId);

        FolderFragment fragment = new FolderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID folderId = (UUID) getArguments().getSerializable(ARG_FOLDER_ID);
        mFolder = FolderLab.get(getActivity()).getFolder(folderId);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();

        FolderLab.get(getActivity()).updateFolder(mFolder);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_folder, container, false);

        mFolderNameField = (EditText) v.findViewById(R.id.folder_name_text);
        mFolderNameField.setText(mFolder.getFolderName());
        mFolderNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FlashCardLab.setCurrentFolderName(mFolder.getFolderName());
                FlashCardLab flashCardLab = FlashCardLab.get(getActivity());
                List<FlashCard> flashCards = flashCardLab.displayFlashCards();
                FlashCard mFlashCard;
                mFolder.setFolderName(s.toString());
                for (Integer i = 0; i < flashCards.size(); i++) {
                    mFlashCard = flashCards.get(i);
                    mFlashCard.setFolderName(mFolder.getFolderName());
                    FlashCardLab.get(getActivity()).updateFlashCard(mFlashCard);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_folder, menu);
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    FlashCardLab.setCurrentFolderName(mFolder.getFolderName());
                    FlashCardLab flashCardLab = FlashCardLab.get(getActivity());
                    List<FlashCard> flashCards = flashCardLab.displayFlashCards();
                    FlashCard mFlashCard;
                    UUID flashcardId;
                    for (Integer i = 0; i < flashCards.size(); i++) {
                        mFlashCard = flashCards.get(i);
                        flashcardId = mFlashCard.getId();
                        FlashCardLab.get(getActivity()).deleteFlashCard(flashcardId);
                    }
                    UUID folderId = mFolder.getId();
                    FolderLab.get(getActivity()).deleteFolder(folderId);
                    getActivity().finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.delete_folder_action:
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Are you sure you want to delete this folder?" +
                    " Doing so will delete all flashcards contained in the folder.").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }

    }
    
}
