package alexj_94.flashyflashcards;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

/**
 * Created by Xander on 11/20/2015.
 */
public class FolderListFragment extends Fragment {
    private RecyclerView mFolderRecyclerView;
    private FolderAdapter mAdapter;
    private static final String TAG = "FolderListFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folder_list, container, false);

        mFolderRecyclerView = (RecyclerView) view.findViewById(R.id.folder_recycler_view);
        mFolderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
        inflater.inflate(R.menu.fragment_folder_flashcard_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add_folder:
                Folder folder = new Folder();
                FolderLab.get(getActivity()).addFolder(folder);
                Intent intent = FolderPagerActivity.newIntent(getActivity(), folder.getId());
                intent.putExtra("folderFetch", 1); //Sends the user to a blank folder
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateUI() {
        FolderLab folderLab = FolderLab.get(getActivity());
        List<Folder> folders = folderLab.getFolders();

        if (mAdapter == null) {
            mAdapter = new FolderAdapter(folders);
            mFolderRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setFolders(folders);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class FolderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Folder mFolder;
        private TextView mFolderNameTextView;
        private TextView mEditFolder;
        private ImageView mDeleteFolder;


        public FolderHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mFolderNameTextView = (TextView) itemView.findViewById(R.id.list_item_folder_name_text_view);
            mEditFolder = (TextView) itemView.findViewById(R.id.edit_folder_icon);
            mDeleteFolder = (ImageView) itemView.findViewById(R.id.delete_folder_icon);

            mEditFolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = FolderPagerActivity.newIntent(getActivity(), mFolder.getId());
                    intent.putExtra("folderFetch", 1); //Sends the user to the editing screen for the selected flashcard
                    startActivity(intent);
                }
            });

            mDeleteFolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Are you sure you want to delete this folder? This will also delete all of the flashcards in the folder.")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            });

        }

        public void bindFolder(Folder folder) {
            mFolder = folder;
            mFolderNameTextView.setText(mFolder.getFolderName());
        }

        @Override
        public void onClick(View v) {
            Intent intent = FolderPagerActivity.newIntent(getActivity(), mFolder.getId());
            intent.putExtra("folderName", mFolder.getFolderName());
            startActivity(intent);
        }

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                       // Yes button clicked
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
                        updateUI();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
    }



    private class FolderAdapter extends RecyclerView.Adapter<FolderHolder> {
        private List<Folder> mFolders;

        public FolderAdapter(List<Folder> folders) { mFolders = folders; }

        @Override
        public FolderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = android.view.LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_folder, parent, false);
            return new FolderHolder(view);
        }

        @Override
        public void onBindViewHolder(FolderHolder holder, int position) {
            Folder folder = mFolders.get(position);
            holder.bindFolder(folder);
        }

        @Override
        public int getItemCount() { return mFolders.size(); }

        public void setFolders(List<Folder> folders) { mFolders = folders; }


    }



}
