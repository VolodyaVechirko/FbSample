package com.vechirko.fbsample.ui.post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vechirko.fbsample.R;
import com.vechirko.fbsample.data.model.PostModel;

public class PostActivity extends AppCompatActivity implements PostView {

    Toolbar toolbar;
    TextView bodyText;
    TextView emptyView;
    ProgressBar progress;

    PostPresenter presenter;

    public static Intent newIntent(Context context, String postId) {
        return new Intent(context, PostActivity.class)
                .putExtra(PostModel.ID, postId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        findViewsById();
        initViews();

        presenter = new PostPresenter(this);
        presenter.getPost();
    }

    private void initViews() {
        setSupportActionBar(toolbar);
    }

    private void findViewsById() {
        toolbar = findViewById(R.id.toolbar);
        bodyText = findViewById(R.id.bodyText);
        emptyView = findViewById(R.id.emptyView);
        progress = findViewById(R.id.progress);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_remove:
                presenter.removePost();
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
    public String getPostId() {
        return getIntent().getStringExtra(PostModel.ID);
    }

    @Override
    public void setData(PostModel data) {
        toolbar.setTitle(data.getTitle());
        toolbar.setSubtitle("Author: ".concat(data.getUserId()));
        bodyText.setText(data.getBody());
    }

    @Override
    public void showEmptyView(boolean show) {
        emptyView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
