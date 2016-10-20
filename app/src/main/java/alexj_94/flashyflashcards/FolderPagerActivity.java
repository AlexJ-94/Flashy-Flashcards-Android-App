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

import alexj_94.flashyflashcards.database.FlashCardDbSchema;

/**
 * Created by Xander on 11/23/2015.
 */
public class FolderPagerActivity extends AppCompatActivity {
    private static final String EXTRA_FOLDER_ID = "alexj_94.flashyflashcards.folder_id";
    private ViewPager mViewPager;
    private List<Folder> mFolders;
    private static final String TAG = "FolderPagerActivity";

    public static Intent newIntent(Context packageContext, UUID folderId) {
        Intent intent = new Intent(packageContext, FolderPagerActivity.class);
        intent.putExtra(EXTRA_FOLDER_ID, folderId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_pager);

        final UUID folderId = (UUID) getIntent().getSerializableExtra(EXTRA_FOLDER_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_folder_pager_view_pager);

        mFolders = FolderLab.get(this).getFolders();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Folder folder = mFolders.get(position);
                Bundle bundle = getIntent().getExtras();
                int folderFetch = bundle.getInt("folderFetch");
                String folderName = bundle.getString("folderName");
                setTitle(folderName);
                FlashCardFragment.setCurrentFolderName(folderName);
                FlashCardLab.setCurrentFolderName(folderName);
                FlashCardListFragment.setCurrentFolderName(folderName);
                switch (folderFetch) {
                    case 1:
                        return FolderFragment.newInstance(folder.getId());
                    default:
                        return new FlashCardListFragment();
                }

            }

            @Override
            public int getCount() {
                return mFolders.size();
            }
        });

        for (int i = 0; i < mFolders.size(); i++) {
            if (mFolders.get(i).getId().equals(folderId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
