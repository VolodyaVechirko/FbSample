package com.vechirko.fbsample.ui.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vechirko.fbsample.R;
import com.vechirko.fbsample.data.test.AlbumContract;

import java.util.Collection;

public class AlbumsActivity extends AppCompatActivity implements AlbumsView {

    Toolbar toolbar;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    TextView emptyView;
    ProgressBar progress;

    AlbumsPresenter presenter;
    AlbumsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);
        findViewsById();
        initViews();

        presenter = new AlbumsPresenter(this);
        presenter.getAll();
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        fab.setOnClickListener(v -> {/**/});

        recyclerView.setAdapter(adapter = new AlbumsAdapter());
    }

    private void findViewsById() {
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);
        emptyView = findViewById(R.id.emptyView);
        progress = findViewById(R.id.progress);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_albums, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_all:
                presenter.getAll();
                return true;
            case R.id.action_favourite:
                presenter.getFavourite();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
    public void setData(Collection<AlbumContract> data) {
        showEmptyView(data.isEmpty());
        adapter.setData(data);
    }

    @Override
    public void showEmptyView(boolean show) {
        emptyView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
