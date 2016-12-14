package ar.org.cedica.memoriabraccocuevas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

        //map nombre y posicion de imagen
        Map<String,Integer> componenteAMemorizar = this.setMapGame();

        //este claves seguro va a desaparecer, no contaba con el preferences
        ArrayList<String> claves =new  ArrayList<String>(componenteAMemorizar.keySet());
        List<Integer> elementosEnPantalla = new ArrayList<Integer>();
        String claveActual=claves.get(1);

        final TextView text = (TextView) findViewById(R.id.textView);

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
        final Integer idImgActual = componenteAMemorizar.get(word);

        text.setText(word);

        ImageView[] imgs = new ImageView[4];

         imgs[0] =(ImageView)findViewById(R.id.imageView);


       final AlertDialog.Builder  builder1 = new AlertDialog.Builder(this);

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
        assetsImg.add(R.drawable.cepillo);
        int Max=(6)+1;// rango para las imagenes
        int Min=0;
        Random random = new Random();
        Integer randPosition =(random.nextInt(4));


        imgs[(randPosition)].setBackgroundResource(componenteAMemorizar.get(word));
        imgs[randPosition].setTag(componenteAMemorizar.get(word));
        int rand =((int)(Math.random()*(Max-Min))+Min);
        elementosEnPantalla.add(componenteAMemorizar.get(word));

        for(int i=0; i<4; i++) {
            boolean flag = true;
            while (flag) {
                if (i != randPosition) {
                    if (!elementosEnPantalla.contains(assetsImg.get(rand))) {
                        elementosEnPantalla.add(assetsImg.get(rand));
                        imgs[i].setBackgroundResource(assetsImg.get(rand));
                        imgs[i].setTag(assetsImg.get(rand));
                        flag = false;
                    }
                    rand = ((int) (Math.random() * (Max - Min)) + Min);
                }else{
                    flag = false;
                }
            }

        }
        imgs[0].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(v.getTag().toString().equals(idImgActual.toString())){
                    text.setText((String)v.getTag().toString());
                }

                return false;
            }
        });
        imgs[2].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(v.getTag().toString().equals(idImgActual.toString())){
                    text.setText((String)v.getTag().toString());

                }

                return false;
            }
        });

        imgs[3].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(v.getTag().toString().equals(idImgActual.toString())){
                    text.setText((String)v.getTag().toString());
                }

                return false;
            }
        });

        imgs[1].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(v.getTag().toString().equals(idImgActual.toString())){
                    text.setText((String)v.getTag().toString());
                }

                return false;
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
        elementos.put("bajomontura",R.drawable.bajomontura);
        elementos.put("cola",R.drawable.cola);
        elementos.put("bozal",R.drawable.bozal);
        elementos.put("casco",R.drawable.casco);
        elementos.put("caballo",R.drawable.caballo);
        elementos.put("zanahoria",R.drawable.zanahoria);
        elementos.put("cuerda",R.drawable.cuerda);
        elementos.put("arriador",R.drawable.arriador);
        elementos.put("cepillo",R.drawable.cepillo);
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
