package com.vechirko.fbsample.posts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vechirko.fbsample.R;
import com.vechirko.fbsample.data.Errors;
import com.vechirko.fbsample.data.model.PostModel;
import com.vechirko.fbsample.data.repository.Repository;
import com.vechirko.fbsample.user.PageTitle;

import java.util.Collection;

public class PostsFragment extends Fragment implements PostsView, PageTitle {

    private static final String ARG_USER_ID = "user_id";

    RecyclerView recyclerView;
    TextView emptyView;
    ProgressBar progress;

    Repository repository;
    PostsAdapter adapter;

    public static PostsFragment newInstance(String userId) {
        PostsFragment fragment = new PostsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new Repository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_posts, container, false);
        findViewsById(rootView);
        initViews();

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        repository.onDestroy();
    }

    private void initViews() {
        recyclerView.setAdapter(adapter = new PostsAdapter());

        showLoading(true);
        repository.get(PostModel.class)
                .where(PostModel.USER_ID, getUserId())
                .findAll()
                .subscribe(
                        this::setData,
                        Errors.handle(this::showError),
                        () -> showLoading(false));
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
    public void setData(Collection<PostModel> data) {
        showEmptyView(data.isEmpty());
        adapter.setData(data);
    }

    @Override
    public void showEmptyView(boolean show) {
        emptyView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getPageTitle() {
        return R.string.posts;
    }
}