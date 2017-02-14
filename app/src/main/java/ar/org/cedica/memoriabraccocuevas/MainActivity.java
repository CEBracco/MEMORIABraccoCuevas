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
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private String word;
    private String gender;
    private Integer nivelAct = 0;
    String actualLevelPref;
    ArrayList<String> selected;
    Map<String,Integer> componenteAMemorizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setIcon(R.drawable.cedica_logo);

        toolbar.setLogo(R.mipmap.ic_launcher);


        //map nombre y posicion de imagen
        componenteAMemorizar = this.setMapGame();

        //este claves seguro va a desaparecer, no contaba con el preferences
        ArrayList<String> claves = new ArrayList<String>(componenteAMemorizar.keySet());
        List<Integer> elementosEnPantalla = new ArrayList<Integer>();

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());


        //obtener el array
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Set<String> selectedTemp = new HashSet<>();
        selectedTemp.add("caballo");
        selectedTemp.add("aros");
        selectedTemp.add("bozal");
        selectedTemp.add("zanahoria");
        Set<String> set = sp.getStringSet("images", selectedTemp);

        selected = new ArrayList<String>(set);
        Collections.sort(selected);
        Integer a = selected.size();

        word = this.obtenerPalabra();



        gender=SP.getString("pref_voice_gender","m");
         actualLevelPref=SP.getString("pref_level","Experto");
        Integer cantImg=setCantImg(actualLevelPref);


        Button playButton = (Button) this.findViewById(R.id.button_play_sound);
        playButton.setText(word);
        final MediaPlayer mp = MediaPlayer.create(this, MainActivity.this.getResources().getIdentifier(gender+"_"+word.toLowerCase().replace(' ','_'),"raw",MainActivity.this.getPackageName()));
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
            }
        });




        //esto deberia usar la imagen seleccionada.
        final Integer idImgActual = componenteAMemorizar.get(word);
        //final Integer idImgActual = this.nextElemen(arrHarcodeado,posActual);


        //es un arreglo dinamico, la longitud va a depender del nivel,
        ImageView[] imgs = imgToView(cantImg);



        final List<Integer> assetsImg = setListAssets(selected);
        int Max=(selected.size());// rango para las imagenes
        int Min=0;
        Random random = new Random();


        //depende del nivel
        Integer randPosition =(random.nextInt(cantImg));
        Log.d("randposition",randPosition.toString());

//        imgs[(randPosition)].setBackgroundResource(componenteAMemorizar.get(word));
        //viejo
        imgs[(randPosition)].setImageResource(componenteAMemorizar.get(word));
        imgs[randPosition].setTag(componenteAMemorizar.get(word));
        int rand =((int)(Math.random()*(Max-Min))+Min);
        elementosEnPantalla.add(componenteAMemorizar.get(word));


        //depende del nivel
        for(int i=0; i<cantImg; i++) {
            boolean flag = true;
            while (flag) {
                if (i != randPosition) {
                    if (assetsImg.get(rand) != null && !elementosEnPantalla.contains(assetsImg.get(rand))) {
                        elementosEnPantalla.add(assetsImg.get(rand));
//                        imgs[i].setBackgroundResource(assetsImg.get(rand));

                        imgs[i].setImageResource(assetsImg.get(rand));
                        imgs[i].setTag(assetsImg.get(rand));
                        flag = false;
                    }
                    rand = ((int) (Math.random() * (Max - Min)) + Min);
                }else{
                    flag = false;
                }
            }

        }

        //mover a metodo aparte que setee las imagenes segun el nivel.
        for (int i=0; i<cantImg; i++){
            imgs[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setPadding(5,0,5,0);
                    v.setBackgroundResource(R.color.colorFocus);
                    alert(v,v.getTag().toString().equals(idImgActual.toString()));
                }
            });
        }



    }
    private Integer nextPos(Integer sizeArray,Integer posAct){

        if (sizeArray>posAct+1){
            return posAct+1;
        }else{
            return 0;
        }

    }

    private String palabraActual(Integer posAct){


        if (selected.size()>posAct+1){
            return selected.get(posAct+1);

        }else{
            return selected.get(0);
        }

    }

    private String obtenerPalabra(){
        SharedPreferences sc = PreferenceManager.getDefaultSharedPreferences(this);
        Integer contador = sc.getInt("Contador",0);

        String palabra=palabraActual(contador);
        contador = nextPos(selected.size(),contador);

        SharedPreferences.Editor mEdit1 = sc.edit();
        mEdit1.putInt("Contador",contador);
        mEdit1.commit();
        return palabra;
    }

    private void alert(final View v, Boolean b){
        final AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
        String title="¡Has Perdido!";
        if(b){
            title="¡GANASTE!";
            alert.setPositiveButton("Siguiente Nivel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue with delete

                    v.setBackgroundResource(0);
                    v.setPadding(0,0,0,0);
                    recreate();
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
                v.setBackgroundResource(0);
                v.setPadding(0,0,0,0);
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
    //devuelve la cantidad de imagenes segun el nivel.
    private int setCantImg(String actualLevelPref){
        int cantSeleccionImagen=0;
        switch (actualLevelPref){
            case "Inicial":
            cantSeleccionImagen= 1;
            break;
            case "Medio":
            cantSeleccionImagen= 2;
            break;
            case "Avanzado":
            cantSeleccionImagen= 3;
            break;
            default:
            cantSeleccionImagen= 4;
            break;
        }

        return cantSeleccionImagen;

    }

    //devuelve un arreglo de imagenes segun el nivel.
    private ImageView[] imgToView(Integer cant) {
        ImageView[] imgs = new ImageView[cant];

        for(int i=0; i < imgs.length ; i++){
            int resId=getResources().getIdentifier("imageView"+i,"id",getPackageName());
            imgs[i]=(ImageView) findViewById(resId);
        }

        return imgs;
    }


    //seteo un arreglo con todos los ids de las imagenes para usar como posibles selecciones
    private List<Integer> setListAssets(List<String> imgSeleccionadas){
        List<Integer> assetsImg = new ArrayList<Integer>();
        for(int i=0; i<imgSeleccionadas.size(); i++) {
            assetsImg.add(componenteAMemorizar.get(imgSeleccionadas.get(i)));
        }
       /* assetsImg.add(R.drawable.bajomontura);
        assetsImg.add(R.drawable.cola);
        assetsImg.add(R.drawable.bozal);
        assetsImg.add(R.drawable.casco);
        assetsImg.add(R.drawable.caballo);
        assetsImg.add(R.drawable.zanahoria);
        assetsImg.add(R.drawable.cepillo);
        assetsImg.add(R.drawable.aros);
        assetsImg.add(R.drawable.arriador);
        assetsImg.add(R.drawable.cabezada);
        assetsImg.add(R.drawable.cinchon_de_volteo);
        assetsImg.add(R.drawable.cascos);
        assetsImg.add(R.drawable.riendas);
        assetsImg.add(R.drawable.rasqueta);
        assetsImg.add(R.drawable.pasto);
        assetsImg.add(R.drawable.orejas);
        assetsImg.add(R.drawable.monturin);
        assetsImg.add(R.drawable.ojos);
        assetsImg.add(R.drawable.crines);
        assetsImg.add(R.drawable.cuerda);
        assetsImg.add(R.drawable.pelota);
        assetsImg.add(R.drawable.fusta);
        assetsImg.add(R.drawable.escarba_vasos);
        assetsImg.add(R.drawable.palos);
        assetsImg.add(R.drawable.matra);
        assetsImg.add(R.drawable.montura);*/
        return assetsImg;
    }

    private Map<String,Integer> setMapGame(){
        Map<String,Integer> elementos = new HashMap<String,Integer>();
        
        elementos.put("aros",R.drawable.aros);
        elementos.put("arriador",R.drawable.arriador);
        elementos.put("bajomontura",R.drawable.bajomontura);
        elementos.put("bozal",R.drawable.bozal);
        elementos.put("caballo",R.drawable.caballo);
        elementos.put("cabezada",R.drawable.cabezada);
        elementos.put("casco",R.drawable.casco);
        elementos.put("cascos",R.drawable.cascos);
        elementos.put("cepillo",R.drawable.cepillo);
        elementos.put("cinchon de volteo",R.drawable.cinchon_de_volteo);
        elementos.put("cola",R.drawable.cola);
        elementos.put("crines",R.drawable.crines);
        elementos.put("cuerda",R.drawable.cuerda);
        elementos.put("escarba vasos",R.drawable.escarba_vasos);
        elementos.put("fusta",R.drawable.fusta);
        elementos.put("matra",R.drawable.matra);
        elementos.put("montura",R.drawable.montura);
        elementos.put("monturin",R.drawable.monturin);
        elementos.put("ojos",R.drawable.ojos);
        elementos.put("orejas",R.drawable.orejas);
        elementos.put("palos",R.drawable.palos);
        elementos.put("pasto",R.drawable.pasto);
        elementos.put("pelota",R.drawable.pelota);
        elementos.put("rasqueta",R.drawable.rasqueta);
        elementos.put("riendas",R.drawable.riendas);
        elementos.put("zanahoria",R.drawable.zanahoria);

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

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences SP=PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if(isPreferencesChanged(SP)){
            recreate();
           // word=SP.getString("pref_level_word","Caballo");
            gender=SP.getString("pref_voice_gender","m");
            String actualLevelPref=SP.getString("pref_level","Experto");
        }
    }

    private boolean isPreferencesChanged(SharedPreferences SP){
        //String actualWordPref=SP.getString("pref_level_word","Caballo");
        String actualGenderPref=SP.getString("pref_voice_gender","m");

        return  (gender != null && !actualGenderPref.equals(gender));
    }
}
