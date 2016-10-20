package alexj_94.flashyflashcards;

import android.support.v4.app.Fragment;

/**
 * Created by Xander on 11/23/2015.
 */
public class FolderListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new FolderListFragment();
    }
}
