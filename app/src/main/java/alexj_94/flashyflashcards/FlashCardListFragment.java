package alexj_94.flashyflashcards;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Alex on 10/13/2015.
 */
public class FlashCardListFragment extends Fragment {
    private RecyclerView mFlashCardRecyclerView;
    private FlashCardAdapter mAdapter;
    private int mLastAdapterClickPosition = -1;
    private static String TAG = "FlashCardListFragment";
    private static String mCurrentFolderName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flashcard_list, container, false);

        mFlashCardRecyclerView = (RecyclerView) view.findViewById(R.id.flashcard_recycler_view);
        mFlashCardRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_flashcard_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add_flashcard:
                FlashCard flashCard = new FlashCard();
                FlashCardLab.get(getActivity()).addFlashCard(flashCard);
                Intent intent = FlashCardPagerActivity.newIntent(getActivity(), flashCard.getId());
                intent.putExtra("flashCardFetch", 1); //Sends the user to a new blank flashcard
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        FlashCardLab flashCardLab = FlashCardLab.get(getActivity());
        List<FlashCard> flashCards = flashCardLab.displayFlashCards();

        if (mAdapter == null) {
            mAdapter = new FlashCardAdapter(flashCards);
            mFlashCardRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setFlashCards(flashCards);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class FlashCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private FlashCard mFlashCard;

        private TextView mQuestionTextView;


        public FlashCardHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mQuestionTextView = (TextView)itemView.findViewById(R.id.list_item_flashcard_question_text_view);
        }

        public void bindFlashCard(FlashCard flashCard) {
            mFlashCard = flashCard;

            mQuestionTextView.setText(mFlashCard.getQuestion());
        }

        @Override
        public void onClick(View v) {
            mLastAdapterClickPosition = getAdapterPosition();
            ViewFlashCard.setClickIndex(mLastAdapterClickPosition);
            Intent intent = FlashCardPagerActivity.newIntent(getActivity(), mFlashCard.getId());
            intent.putExtra("folderName", mCurrentFolderName);
            startActivity(intent);
        }
    }

    private class FlashCardAdapter extends RecyclerView.Adapter<FlashCardHolder> {

        private List<FlashCard> mFlashCards;

        public FlashCardAdapter(List<FlashCard> flashCards) { mFlashCards = flashCards; }

        @Override
        public FlashCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = android.view.LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_flashcard, parent, false);
            return new FlashCardHolder(view);
        }

        @Override
        public void onBindViewHolder(FlashCardHolder holder, int position) {
            FlashCard flashCard = mFlashCards.get(position);
            holder.bindFlashCard(flashCard);
        }

        @Override
        public int getItemCount() { return mFlashCards.size(); }

        public void setFlashCards(List<FlashCard> flashCards) { mFlashCards = flashCards; }
    }

    public static void setCurrentFolderName(String currentFolderName) {
        mCurrentFolderName = currentFolderName;
    }
}
