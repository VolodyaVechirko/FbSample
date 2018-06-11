package com.vechirko.fbsample.ui.albums;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vechirko.fbsample.R;
import com.vechirko.fbsample.data.model.AlbumModel;
import com.vechirko.fbsample.ui.user.PageTitle;

import java.util.Collection;

public class AlbumsFragment extends Fragment implements AlbumsView, PageTitle {

    private static final String ARG_USER_ID = "user_id";

    RecyclerView recyclerView;
    TextView emptyView;
    ProgressBar progress;

    AlbumsPresenter presenter;
    AlbumsAdapter adapter;

    public static AlbumsFragment newInstance(String userId) {
        AlbumsFragment fragment = new AlbumsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_albums, container, false);
        findViewsById(rootView);
        initViews();

        presenter = new AlbumsPresenter(this);
        presenter.getAlbums(getUserId());
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }

    private void initViews() {
        recyclerView.setAdapter(adapter = new AlbumsAdapter());
    }

    private void findViewsById(View root) {
        recyclerView = root.findViewById(R.id.recyclerView);
        emptyView = root.findViewById(R.id.emptyView);
        progress = root.findViewById(R.id.progress);
    }

    @Override
    public String getUserId() {
        return getArguments().getString(ARG_USER_ID);
    }

    @Override
    public void showLoading(boolean show) {
        progress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String message) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.error)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    @Override
    public void setData(Collection<AlbumModel> data) {
        showEmptyView(data.isEmpty());
        adapter.setData(data);
    }

    @Override
    public void showEmptyView(boolean show) {
        emptyView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getPageTitle() {
        return R.string.albums;
    }
}
