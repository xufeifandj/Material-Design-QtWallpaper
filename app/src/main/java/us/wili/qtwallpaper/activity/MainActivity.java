package us.wili.qtwallpaper.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.MenuItem;

import us.wili.qtwallpaper.R;
import us.wili.qtwallpaper.fragment.HotFragment;

public class MainActivity extends AppCompatActivity {
    public static final int PAGE_HOT = 0;
    public static final int PAGE_CATEGORY = 1;
    private SparseArray<Fragment> mFragments;

    private DrawerLayout mDrawerLayout;

    private int mContainer;

    private int currentTab = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();

        initData();
    }

    private void initViews() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.hot);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getGroupId() == R.id.group_tab) {
                    item.setChecked(true);
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        mContainer = R.id.container;

        mFragments = new SparseArray<>();
        mFragments.put(PAGE_HOT, new HotFragment());
    }

    private void initData() {
        //选中hot
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(mContainer, mFragments.get(PAGE_HOT));
        ft.commit();
        currentTab = PAGE_HOT;
    }

    /**
     * 改变选项卡
     * @param tab PAGE_HOT / PAGE_CATEGORY
     */
    private void changeTab(int tab) {
        if (currentTab == tab) {
            return;
        }
        Fragment nextFragment = mFragments.get(tab);
        if (nextFragment == null) {
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (!nextFragment.isAdded()) {
            ft.add(mContainer, nextFragment);
        }
        ft.hide(mFragments.get(currentTab));
        ft.show(nextFragment);
        if (!isFinishing()) {
            ft.commitAllowingStateLoss();
        }
        currentTab = tab;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.hot:
                changeTab(PAGE_HOT);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}