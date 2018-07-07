package com.hack.innovvapp.Activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.hack.innovvapp.Fragments.FeedbackFrament;
import com.hack.innovvapp.Fragments.InformationFragment;
import com.hack.innovvapp.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String FEEDBACK_TYPE ="bundle";

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_emitra) {
            toolbar.setTitle(getResources().getString(R.string.about_emitra));

            InformationFragment informationFragment = new InformationFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(FEEDBACK_TYPE,0);
            informationFragment.setArguments(bundle);
            changeMainFragment(this,informationFragment);


        } else if (id == R.id.nav_bhamashah) {
            toolbar.setTitle(getResources().getString(R.string.about_bhamashah));

            InformationFragment informationFragment = new InformationFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(FEEDBACK_TYPE,1);
            informationFragment.setArguments(bundle);
            changeMainFragment(this,informationFragment);






        } else if (id == R.id.nav_emitra_feedback) {
            toolbar.setTitle(getResources().getString(R.string.emitra_feedback));

            FeedbackFrament feedbackFrament = new FeedbackFrament();
            Bundle bundle = new Bundle();
            bundle.putInt(FEEDBACK_TYPE,1);
            feedbackFrament.setArguments(bundle);
            changeMainFragment(MainActivity.this,feedbackFrament);




        } else if (id == R.id.nav_bhamashah_feedback) {
            toolbar.setTitle(getResources().getString(R.string.bhamashah_feedback));
            FeedbackFrament feedbackFrament = new FeedbackFrament();
            Bundle bundle = new Bundle();
            bundle.putInt(FEEDBACK_TYPE,2);
            feedbackFrament.setArguments(bundle);
            changeMainFragment(MainActivity.this,feedbackFrament);

        } else if (id == R.id.nav_new_services_feedback) {

            toolbar.setTitle(getResources().getString(R.string.new_services_feedback));
            FeedbackFrament feedbackFrament = new FeedbackFrament();
            Bundle bundle = new Bundle();
            bundle.putInt(FEEDBACK_TYPE,3);
            feedbackFrament.setArguments(bundle);
            changeMainFragment(MainActivity.this,feedbackFrament);

        } else if (id == R.id.nav_new_services) {
            toolbar.setTitle(getResources().getString(R.string.new_serices_title));

            InformationFragment informationFragment = new InformationFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(FEEDBACK_TYPE,2);
            informationFragment.setArguments(bundle);
            changeMainFragment(this,informationFragment);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public static void changeMainFragment(FragmentActivity fragmentActivity, Fragment fragment){
        fragmentActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_main, fragment)
                .commit();
    }
    public static void changeMainFragmentWithBack(FragmentActivity fragmentActivity, Fragment fragment){

        fragmentActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_main, fragment)
                .addToBackStack(null)
                .commit();
    }

}
