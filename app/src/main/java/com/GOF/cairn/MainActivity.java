package com.GOF.cairn;

import android.os.Bundle;
import android.view.MenuItem;

import com.GOF.cairn.ui.favourites.FavFragment;
import com.GOF.cairn.ui.map.MapFragment;
import com.GOF.cairn.ui.preferences.PreferenceFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);                                     //logical assigning of nav bar
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont, new MapFragment()).commit(); //selects Map on startup
        bottomNav.setSelectedItemId( R.id.navigation_Map);                                                  //sets the middle item as selected on startup (map)

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =   //for selecting a nav bar item
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        case R.id.navigation_Pref:
                            selectedFragment  = new PreferenceFragment();
                            break;
                        case R.id.navigation_Map:
                            selectedFragment  = new MapFragment();
                            break;
                        case R.id.navigation_Fav:
                            selectedFragment  = new FavFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont,selectedFragment).commit(); //launches fragment
                    return true;
                }
            };
}