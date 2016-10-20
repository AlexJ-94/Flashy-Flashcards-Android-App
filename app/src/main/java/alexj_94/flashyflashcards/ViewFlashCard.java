package alexj_94.flashyflashcards;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

/**
 * Created by Alex on 10/14/2015.
 */
public class ViewFlashCard extends Fragment {
    private static final String ARG_FLASHCARD_ID = "flashcard_id";
    private FlashCard mFlashCard;
    private TextView mFlashCardText;
    private TextView mFlashCardNumber;
    private Button mPreviousButton;
    private Button mSwitchTextButton;
    private Button mNextButton;
    private Boolean mIsQuestion = true;
    private UUID mFlashCardId;
    private static Integer mIndex;
    private static Integer endOfList;
    private static final String TAG = "ViewFlashCard";


    public static ViewFlashCard newInstance(UUID flashCardId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_FLASHCARD_ID, flashCardId);

        ViewFlashCard fragment = new ViewFlashCard();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFlashCardId = (UUID) getArguments().getSerializable(ARG_FLASHCARD_ID);
        mFlashCard = FlashCardLab.get(getActivity()).getFlashCard(mFlashCardId);
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.flashcard_view, container, false);

        final FlashCardLab flashCardLab = FlashCardLab.get(getActivity());
        final List<FlashCard> flashCards = flashCardLab.displayFlashCards();

        mFlashCardText = (TextView) v.findViewById(R.id.question_text_view);
        mFlashCardText.setText(mFlashCard.getQuestion());

        mFlashCardNumber = (TextView) v.findViewById(R.id.flashcard_number);
        mFlashCardNumber.setText(mIndex + 1 + " of " + flashCards.size());

        mNextButton = (Button) v.findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endOfList = flashCards.size() - 1; //Checks the size of the list and subtracts 1 to account for the index
                if (mIndex < endOfList) {
                    mIndex++;
                } else if (mIndex == endOfList) {
                    mIndex = 0;
                }
                mFlashCard = flashCards.get(mIndex);
                mFlashCardText.setText(mFlashCard.getQuestion());
                mFlashCardNumber.setText(mIndex + 1 + " of " + flashCards.size());
                mSwitchTextButton.setText(R.string.show_answer);
                mIsQuestion = true;
            }
        });

        mPreviousButton = (Button) v.findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endOfList = flashCards.size() - 1; //Checks the size of the list and subtracts 1 to account for the index
                if (mIndex == 0) { //Checks to see if the user is at the beginning of the list and if so moves them to the end
                    mIndex = endOfList;
                } else {
                    mIndex--;
                }
                mFlashCard = flashCards.get(mIndex);
                mFlashCardText.setText(mFlashCard.getQuestion());
                mFlashCardNumber.setText(mIndex + 1 + " of " + flashCards.size());
                mSwitchTextButton.setText(R.string.show_answer);
                mIsQuestion = true;
            }
        });

        mSwitchTextButton = (Button) v.findViewById(R.id.switch_text_button);
        mSwitchTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsQuestion) {
                    mFlashCardText.setText(mFlashCard.getAnswer());
                    mSwitchTextButton.setText(R.string.show_question);
                    mIsQuestion = false;
                } else if (mIsQuestion == false) {
                    mFlashCardText.setText(mFlashCard.getQuestion());
                    mSwitchTextButton.setText(R.string.show_answer);
                    mIsQuestion = true;
                }
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.flashcard_view, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_edit_flaschard:
                Intent intent = FlashCardPagerActivity.newIntent(getActivity(), mFlashCard.getId());
                intent.putExtra("flashCardFetch", 1); //Sends the user to the editing screen for the selected flashcard
                startActivity(intent);
                return true;
            case R.id.delete_flashcard_action:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to delete this flashcard?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            default:
                return super.onOptionsItemSelected(item);
        }
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

    public static void setClickIndex(Integer clickIndex) {
        mIndex = clickIndex;
    }


}
