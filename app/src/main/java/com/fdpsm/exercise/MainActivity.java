package com.fdpsm.exercise;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.fdpsm.exercise.fragments.MainActivityFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;


@EActivity(R.layout.activity_main)
@OptionsMenu({R.menu.menu_main})
public class MainActivity extends AppCompatActivity {

    @AfterViews
    public void init()
    {
        if (findViewById(R.id.container) != null) {

            if (getFragmentManager().findFragmentByTag("list") == null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.container, new MainActivityFragment_(), getString(R.string.fragment_tag_list));
                ft.commit();
            }
        }
    }

   @OptionsItem(R.id.action_settings)
    void settingsMenuItemSelected() {
        //settings menu item ...
    }
}