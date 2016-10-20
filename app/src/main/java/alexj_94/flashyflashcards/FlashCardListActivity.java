package alexj_94.flashyflashcards;


import android.support.v4.app.Fragment;


/**
 * Created by Alex on 10/13/2015.
 */
public class FlashCardListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new FlashCardListFragment();
    }
}
