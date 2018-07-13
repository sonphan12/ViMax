package com.sonphan12.vimax.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.sonphan12.vimax.DummyFragment;
import com.sonphan12.vimax.R;
import com.sonphan12.vimax.ViMaxApplication;
import com.sonphan12.vimax.ui.videolist.VideoFragment;
import com.sonphan12.vimax.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.mainTab)
    TabLayout mainTab;
    @BindView(R.id.mainPager)
    ViewPager mainPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    MainPagerAdapter adapter;
    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        setUpPager(mainPager);
        mainTab.setupWithViewPager(mainPager);

        compositeDisposable = ((ViMaxApplication)getApplication()).getApplicationComponent().getCompositeDisposable();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                Intent i = new Intent(AppConstants.ACION_SEARCH);
                i.putExtra(AppConstants.EXTRA_SEARCH_QUERY, newText);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
                return true;
            }
        });
    }

    private void setUpPager(ViewPager mainPager) {
        adapter = new MainPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DummyFragment(), "Album");
        adapter.addFragment(new VideoFragment(), "Video");
        mainPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        // Disable default focusing
        searchView.setFocusable(false);
        searchView.setIconified(false);
        searchView.clearFocus();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MainPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

}
