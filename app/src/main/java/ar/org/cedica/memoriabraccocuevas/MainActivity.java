package ar.org.cedica.memoriabraccocuevas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView img =(ImageView)findViewById(R.id.imageView);
        ImageView img2 =(ImageView)findViewById(R.id.imageView2);
        ImageView img3 =(ImageView)findViewById(R.id.imageView3);
        ImageView img4 =(ImageView)findViewById(R.id.imageView4);
        img.setBackgroundResource(R.drawable.casco);
        img2.setBackgroundResource(R.drawable.bajomontura);
        img3.setBackgroundResource(R.drawable.bozal);
        img4.setBackgroundResource(R.drawable.cola);

//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        String word=prefs.getString("pref_level_word",null);
        Button playButton = (Button) this.findViewById(R.id.button_play_sound);
        final MediaPlayer mp = MediaPlayer.create(this, MainActivity.this.getResources().getIdentifier("caballo","raw",MainActivity.this.getPackageName()));
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
       /* List<String> listita = new ArrayList<String>();
        listita.add("holaa");
        listita.add("El Hombre rata");
        listita.add("holaa");
        listita.add("El Hombre rata");
        ListView l = (ListView) findViewById(R.id.listaImg);
        ArrayAdapter<String> ada = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,listita);
        l.setAdapter(ada);
        */
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
            Intent intent= new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
