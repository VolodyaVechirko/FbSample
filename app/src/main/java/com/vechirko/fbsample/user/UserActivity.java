package com.vechirko.fbsample.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vechirko.fbsample.R;
import com.vechirko.fbsample.albums.AlbumsFragment;
import com.vechirko.fbsample.data.Errors;
import com.vechirko.fbsample.data.model.UserModel;
import com.vechirko.fbsample.data.repository.Repository;
import com.vechirko.fbsample.posts.PostsFragment;

import java.util.Arrays;
import java.util.List;

public class UserActivity extends AppCompatActivity implements UserView, AppBarLayout.OnOffsetChangedListener {

    private static final String EXTRA_USER_ID = "user_id";

    Toolbar toolbar;
    AppBarLayout appbar;
    ImageView avatar;
    TextView username;
    TextView address;
    ViewPager viewPager;
    TabLayout tabLayout;
    ProgressBar progress;
    FloatingActionButton fab;

    int mMaxScrollSize;
    static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    boolean mIsAvatarShown = true;

    Repository repository;

    public static Intent newIntent(Context context, String userId) {
        return new Intent(context, UserActivity.class)
                .putExtra(EXTRA_USER_ID, userId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
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

        appbar.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbar.getTotalScrollRange();

        List<Fragment> fragments = Arrays.asList(
                AlbumsFragment.newInstance(getUserId()),
                PostsFragment.newInstance(getUserId())
        );

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return getText(((PageTitle) fragments.get(position)).getPageTitle());
            }
        });

        fab.setOnClickListener(view -> {/**/});

        showLoading(true);
        repository.get(UserModel.class)
                .where(UserModel.ID, getUserId())
                .findAll()
                .map(coll -> coll.iterator().next())
                .subscribe(
                        this::setData,
                        Errors.handle(this::showError),
                        () -> showLoading(false)
                );
    }

    private void findViewsById() {
        toolbar = findViewById(R.id.toolbar);
        appbar = findViewById(R.id.appbar);
        avatar = findViewById(R.id.avatar);
        username = findViewById(R.id.username);
        address = findViewById(R.id.address);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        progress = findViewById(R.id.progress);
        fab = findViewById(R.id.fab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public String getUserId() {
        return getIntent().getStringExtra(EXTRA_USER_ID);
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
    public void setData(UserModel user) {
        username.setText(user.getUsername());
        address.setText(user.getAddress().toString());
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(verticalOffset)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            avatar.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            avatar.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }
}
