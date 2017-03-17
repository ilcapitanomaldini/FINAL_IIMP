package com.liveproject.ycce.iimp.messaging;

/*COPYRIGHT NOTICE

Copyright By "YCCE TEAM" on 15-03-2017.

Members of "YCCE TEAM" are stated in the postscript.

We, the creators of this software (i.e. developers) referenced as "YCCE TEAM" or "we" from here on,
allow the person who gets this software and/or code for the software to present this software/code
in a presentation dated 16-03-2017 only. Any further usage would be deemed to be breach of contract.
 Accepting this software/code is legally binding and would mean that the terms stated here have been
  accepted. The person does not have the right to copy/modify/distribute or in any form make the
  software or code available to anyone without the explicit permission of all the members of
   "YCCE TEAM". It is the responsibility of the aforementioned person that this software/code
   does not get illegally distributed till the time the person is in possession of the software/code.

P.S. :
Members of "YCCE TEAM" :
1. Aakash Wankhede
2. Akash Kahalkar
3. Mayur Dongare
4. Ved Mehta*/

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.liveproject.ycce.iimp.DatabaseService;
import com.liveproject.ycce.iimp.R;
import com.liveproject.ycce.iimp.events.EventHandlerService;
import com.liveproject.ycce.iimp.networkservice.updateservice.DowndateService;
import com.liveproject.ycce.iimp.constants.Constants;
import com.liveproject.ycce.iimp.news.NewsActivity;
import com.liveproject.ycce.iimp.pendingrequests.PRActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laptop on 07-01-2017.
 */
public class Activity_Home_Messaging extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String s_id, s_status;
    Toast toast;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_messaging);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //TODO : Put the following function in a proper context/place.
        //Testing
        DatabaseService.updateLoginDateTimeByYear(2000);
        startService(new Intent(this, EventHandlerService.class));

        if (isOnline(this)) {
            //TODO : Add UpdateService as well.
            Intent intent1 = new Intent(this, DowndateService.class);
            this.startService(intent1);
        }

        s_id = DatabaseService.fetchID();
        s_status = DatabaseService.fetchUserStatus(s_id);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        DatabaseService.updateLoginDateTime();
        s_id = DatabaseService.fetchID();
        s_status = DatabaseService.fetchUserStatus(s_id);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        if (Constants.USERSTATUS[2].equalsIgnoreCase(s_status)) {
            toast.makeText(getBaseContext(), "Your ID has been blocked. Please contact database server.", Toast.LENGTH_LONG).show();
            finish();
        } else if (Constants.USERSTATUS[0].equalsIgnoreCase(s_status)) {
            toast.makeText(getBaseContext(), "Your request has not been approved let. Try again later.", Toast.LENGTH_LONG).show();
            finish();
        } else {

            //toolbar = (Toolbar) findViewById(R.id.toolbar);
            //setSupportActionBar(toolbar);

            // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(viewPager);

            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment_Groups(), "GROUPS");
        adapter.addFragment(new Fragment_Messaging(), "MESSAGES");
        //adapter.addFragment(new Fragment_Three(), "THREE");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent i;

        if (id == R.id.nav_messaging) {
            i = new Intent("com.liveproject.persi.ycce.iimp.HOME_MESSAGING");
            startActivity(i);
        } else if (id == R.id.nav_news) {
            // DatabaseService.createnewstable();
            i = new Intent(this, NewsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_profile) {
            i = new Intent("com.liveproject.persi.ycce.iimp.USER_PROFILE");
            startActivity(i);
        } else if (id == R.id.nav_pr) {
            i = new Intent(this, PRActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }
}
