package ar.org.cedica.memoriabraccocuevas;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ImageSelector extends AppCompatActivity {

    private ArrayList<String> selected=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selector);
        setupActionBar();

        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Set<String> set=sp.getStringSet("images",new HashSet<String>());
        selected=new ArrayList<String>(set);

        for (int i=1; i <= 26; i++){
            String imageViewId="imageSelector"+i;
            int resID= getResources().getIdentifier(imageViewId, "id", getPackageName());

            final ImageView imageView=(ImageView) findViewById(resID);

            if(!selected.contains(imageView.getTag().toString())){
                imageView.setColorFilter(Color.argb(150,0,0,0));
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(imageView.getColorFilter() != null){
                        imageView.clearColorFilter();
                        selected.add(imageView.getTag().toString());
                    }
                    else{
                        imageView.setColorFilter(Color.argb(150,0,0,0));
                        selected.remove(imageView.getTag().toString());
                    }
                }
            });
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Set<String> set=new HashSet<String>();
        set.addAll(selected);

        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor=sp.edit();

        editor.putStringSet("images",set);
        editor.commit();
    }
}
