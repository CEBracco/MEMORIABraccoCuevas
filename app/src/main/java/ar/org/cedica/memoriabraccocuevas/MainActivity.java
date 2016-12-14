package ar.org.cedica.memoriabraccocuevas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
        String word=SP.getString("pref_level_word","Caballo");

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
        imgs[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert(v,v.getTag().toString().equals(idImgActual.toString()));
            }
        });
        imgs[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert(v,v.getTag().toString().equals(idImgActual.toString()));
            }
        });

        imgs[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert(v,v.getTag().toString().equals(idImgActual.toString()));
            }
        });

        imgs[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert(v,v.getTag().toString().equals(idImgActual.toString()));
            }
        });



    }

    private void alert(View v, Boolean b){
        final AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
        String title="¡Has Perdido!";
        if(b){
            title="¡GANASTE!";
            alert.setPositiveButton("Siguiente Nivel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue with delete
                }
            });
            MediaPlayer.create(v.getContext(), R.raw.relincho).start();
        }
        else{
            MediaPlayer.create(v.getContext(), R.raw.resoplido).start();
        }
        alert.setTitle(title);
        alert.setNegativeButton("Reintentar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        });
        alert.setIcon(android.R.drawable.ic_dialog_alert);
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        elementos.put("Cepillo",R.drawable.cepillo);
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
