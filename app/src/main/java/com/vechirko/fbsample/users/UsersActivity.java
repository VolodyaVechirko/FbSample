package com.vechirko.fbsample.users;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vechirko.fbsample.R;
import com.vechirko.fbsample.data.Errors;
import com.vechirko.fbsample.data.model.UserModel;
import com.vechirko.fbsample.data.repository.Repository;

import java.util.Collection;

public class UsersActivity extends AppCompatActivity implements UsersView {

    Toolbar toolbar;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    TextView emptyView;
    ProgressBar progress;

    Repository repository;
    UsersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        repository = new Repository();
        findViewsById();
        initViews();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        repository.onDestroy();
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        fab.setOnClickListener(v -> {/**/});

        recyclerView.setAdapter(adapter = new UsersAdapter());

        showLoading(true);
        repository.getAll(UserModel.class)
                .subscribe(
                        this::setData,
                        Errors.handle(this::showError),
                        () -> showLoading(false)
                );
    }

    private void findViewsById() {
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);
        emptyView = findViewById(R.id.emptyView);
        progress = findViewById(R.id.progress);
    }

    @Override
    public void showLoading(boolean show) {
        progress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String message) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.error)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    @Override
    public void setData(Collection<UserModel> data) {
        showEmptyView(data.isEmpty());
        adapter.setData(data);
    }

    @Override
    public void showEmptyView(boolean show) {
        emptyView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
