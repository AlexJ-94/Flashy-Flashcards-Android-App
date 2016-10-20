package alexj_94.flashyflashcards;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.UUID;

/**
 * Created by Alex on 10/5/2015.
 */
public class FlashCardFragment extends Fragment {
    private static final String ARG_FLASHCARD_ID = "flashcard_id";

    private FlashCard mFlashCard;
    private EditText mQuestionField;
    private EditText mAnswerField;
    private static String mCurrentFolderName;

    public static FlashCardFragment newInstance(UUID flashCardId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_FLASHCARD_ID, flashCardId);

        FlashCardFragment fragment = new FlashCardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID flashCardId = (UUID) getArguments().getSerializable(ARG_FLASHCARD_ID);
        mFlashCard = FlashCardLab.get(getActivity()).getFlashCard(flashCardId);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();

        FlashCardLab.get(getActivity()).updateFlashCard(mFlashCard);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_flashcard, container, false);

        mQuestionField = (EditText) v.findViewById(R.id.question_text);
        mQuestionField.setText(mFlashCard.getQuestion());
        mQuestionField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mFlashCard.setQuestion(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAnswerField = (EditText) v.findViewById(R.id.answer_text);
        mAnswerField.setText(mFlashCard.getAnswer());
        mAnswerField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mFlashCard.setAnswer(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (mFlashCard.getFolderName() == null) {
            mFlashCard.setFolderName(mCurrentFolderName);
        }

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_flashcard, menu);
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    UUID flashCardId = mFlashCard.getId();
                    FlashCardLab.get(getActivity()).deleteFlashCard(flashCardId);
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
            case R.id.delete_flashcard_action:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to delete this flashcard?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void setCurrentFolderName(String currentFolderName) {
        mCurrentFolderName = currentFolderName;
    }


}
