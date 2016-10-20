package alexj_94.flashyflashcards;





import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;
import java.util.UUID;

/**
 * Created by Alex on 10/13/2015.
 */
public class FlashCardPagerActivity extends AppCompatActivity {
    private static final String EXTRA_FLASHCARD_ID = "alexj_94.flashyflashcards.flashcard_id";
    private ViewPager mViewPager;
    private List<FlashCard> mFlashCards;
    private static final String TAG = "FlashCardPagerActivity";

    public static Intent newIntent(Context packageContext, UUID flashCardId) {
        Intent intent = new Intent(packageContext, FlashCardPagerActivity.class);
        intent.putExtra(EXTRA_FLASHCARD_ID, flashCardId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_pager);

        final UUID flashCardId = (UUID) getIntent().getSerializableExtra(EXTRA_FLASHCARD_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_flashcard_pager_view_pager);

        mFlashCards = FlashCardLab.get(this).getFlashCards();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                FlashCard flashCard = mFlashCards.get(position);
                Bundle bundle = getIntent().getExtras();
                int flashCardFetch = bundle.getInt("flashCardFetch");
                String folderName = bundle.getString("folderName");
                setTitle(folderName);
                switch (flashCardFetch) { //checks to see if the user wants to edit or just view the flashcard
                    case 1:
                        return FlashCardFragment.newInstance(flashCard.getId());
                    default:
                        return ViewFlashCard.newInstance(flashCard.getId());
                }
            }


            @Override
            public int getCount() {
                return mFlashCards.size();
            }
        });

        for (int i = 0; i < mFlashCards.size(); i++) {
            if (mFlashCards.get(i).getId().equals(flashCardId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}

