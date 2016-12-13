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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Map<String,Integer> componenteAMemorizar = this.setMapGame();
        TextView text = (TextView) findViewById(R.id.textView);
        ArrayList<String> claves =new  ArrayList<String>(componenteAMemorizar.keySet());
        List<Integer> elementosEnPantalla = new ArrayList<Integer>();
        String claveActual=claves.get(1);

        text.setText(claveActual);

        ImageView[] imgs = new ImageView[4];

         imgs[0] =(ImageView)findViewById(R.id.imageView);
         imgs[2] =(ImageView)findViewById(R.id.imageView2);
         imgs[3] =(ImageView)findViewById(R.id.imageView3);
         imgs[1] =(ImageView)findViewById(R.id.imageView4);

        List<Integer> assetsImg = new ArrayList<Integer>();
        assetsImg.add(R.drawable.bajomontura);
        assetsImg.add(R.drawable.cola);
        assetsImg.add(R.drawable.bozal);
        assetsImg.add(R.drawable.casco);
        assetsImg.add(R.drawable.caballo);
        assetsImg.add(R.drawable.zanahoria);
        int Max=(5)+1;// rango para las imagenes
        int Min=0;
        int randPosition =((int)(Math.random()*3));
        imgs[randPosition].setBackgroundResource(componenteAMemorizar.get(claveActual));
        int rand =((int)(Math.random()*(Max-Min))+Min);
        elementosEnPantalla.add(componenteAMemorizar.get(claveActual));

        for(int i=0; i<4; i++) {
            boolean flag = true;
            while (flag) {
                if (i != randPosition) {
                    if (!elementosEnPantalla.contains(assetsImg.get(rand))) {
                        elementosEnPantalla.add(assetsImg.get(rand));
                        imgs[i].setBackgroundResource(assetsImg.get(rand));
                        flag = false;
                    }
                    rand = ((int) (Math.random() * (Max - Min)) + Min);
                }else{
                    flag = false;
                }
            }

        }
        ImageView img =(ImageView)findViewById(R.id.imageView);
        ImageView img2 =(ImageView)findViewById(R.id.imageView2);
        ImageView img3 =(ImageView)findViewById(R.id.imageView3);
        ImageView img4 =(ImageView)findViewById(R.id.imageView4);
        img.setBackgroundResource(R.drawable.casco);
        img2.setBackgroundResource(R.drawable.bajomontura);
        img3.setBackgroundResource(R.drawable.bozal);
        img4.setBackgroundResource(R.drawable.cola);

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String word=SP.getString("pref_level_word","caballo");
        Button playButton = (Button) this.findViewById(R.id.button_play_sound);
        final MediaPlayer mp = MediaPlayer.create(this, MainActivity.this.getResources().getIdentifier(word.toLowerCase(),"raw",MainActivity.this.getPackageName()));
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
    private Map<String,Integer> setMapGame(){
        Map<String,Integer> elementos = new HashMap<String,Integer>();
        elementos.put("Bajomontura",R.drawable.bajomontura);
        elementos.put("Cola",R.drawable.cola);
        elementos.put("Bozal",R.drawable.bozal);
        elementos.put("Casco",R.drawable.casco);
        elementos.put("Caballo",R.drawable.caballo);
        elementos.put("Zanahoria",R.drawable.zanahoria);
        elementos.put("Cuerda",R.drawable.cuerda);
        elementos.put("Arriador",R.drawable.arriador);
        return elementos;


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
