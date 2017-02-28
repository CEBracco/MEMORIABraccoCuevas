package ar.org.cedica.memoriabraccocuevas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    private String word;
    private String gender;
    private Integer nivelAct = 0;
    String actualLevelPref;
    ArrayList<String> selected;
    ArrayList<String> imagenesDistractoras;
    Map<String,Integer> componenteAMemorizar;


    private Boolean timerStopped=true;
    private Handler timer=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(!timerStopped){
                MediaPlayer.create(MainActivity.this, R.raw.resoplido).start();

                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                String title="¡Tiempo Agotado!";

                alert.setTitle(title);
                alert.setNegativeButton("Reintentar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        startTimer();
                    }
                });
                alert.setIcon(android.R.drawable.ic_dialog_alert);
                alert.show();
            }
        }
    };

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

        Set<String> set = sp.getStringSet("images", getDefaultSelectedWords());

        selected = new ArrayList<String>(set);

        Collections.sort(selected);
        Integer a = selected.size();

        word = this.obtenerPalabra();



        gender=SP.getString("pref_voice_gender","m");
        actualLevelPref=SP.getString("pref_level","Experto");
        final Integer cantImg=setCantImg(actualLevelPref);


        Button playButton = (Button) this.findViewById(R.id.button_play_sound);
        playButton.setText(word.replace('_',' '));
        final MediaPlayer mp = MediaPlayer.create(this, MainActivity.this.getResources().getIdentifier(gender+"_"+word.toLowerCase().replace(' ','_'),"raw",MainActivity.this.getPackageName()));
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
            }
        });




        final Integer idImgActual = componenteAMemorizar.get(word);


        //es un arreglo dinamico, la longitud va a depender del nivel,
        ImageView[] imgs = imgToView(cantImg);



        final List<Integer> assetsImg = setListAssets(claves);
        int Max=(claves.size());// rango para las imagenes
        int Min=0;
        Random random = new Random();


        //depende del nivel
        Integer randPosition =(random.nextInt(cantImg));

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
                    timerStopped=true;
                    v.setPadding(5,0,5,0);
                    v.setBackgroundResource(R.color.colorFocus);
                    alert(v,v.getTag().toString().equals(idImgActual.toString()));
                }
            });
        }

        //reproduzco por primera vez el audio
        mp.start();

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
        SharedPreferences.Editor mEdit1 = sc.edit();
        if(contador==0){
            mEdit1.putBoolean("Finaliza",false);
        }
        if((contador+1)==selected.size()){
            mEdit1.putBoolean("Finaliza",true);
        }
       
        String palabra=palabraActual(contador);
        contador = nextPos(selected.size(),contador);


        mEdit1.putInt("Contador",contador);
        mEdit1.commit();
        return palabra;
    }
    private Boolean finalizoNivel(){
        SharedPreferences sc = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean finalizo = sc.getBoolean("Finaliza",false);
        return finalizo;
    }

    private void alert(final View v, Boolean b){
        String title="¡Has Perdido!";
        if(b){
            if(finalizoNivel()){
                MediaPlayer.create(v.getContext(), R.raw.relincho).start();
                final AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                title="¡GANASTE!";
                alert.setTitle(title);
                alert.setPositiveButton("Siguiente Nivel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        v.setBackgroundResource(0);
                        v.setPadding(0,0,0,0);
                        setNivel();
                        recreate();
                    }
                });


                alert.setNegativeButton("Repetir Nivel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        v.setBackgroundResource(0);
                        v.setPadding(0,0,0,0);
                        resetContador();
                        recreate();
                    }
                });
                alert.setIcon(android.R.drawable.ic_dialog_alert);
                alert.show();

            }else{
                MediaPlayer mp=MediaPlayer.create(v.getContext(), R.raw.relincho);
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        v.setBackgroundResource(0);
                        v.setPadding(0,0,0,0);
                        recreate();
                    }
                });
                mp.start();
            }
        }
        else{
            MediaPlayer mp=MediaPlayer.create(v.getContext(), R.raw.resoplido);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    v.setBackgroundResource(0);
                    v.setPadding(0,0,0,0);
                }
            });
            mp.start();
        }
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
    private void setNivel(){
        List<String> nivels = new ArrayList<String>();
        nivels.add("Inicial");
        nivels.add("Medio");
        nivels.add("Avanzado");
        nivels.add("Experto");

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Integer pos =nivels.indexOf(actualLevelPref);
        if(pos==nivels.size()-1) {
            pos = 0;
        }else{
            pos = pos + 1;
        }
        SharedPreferences.Editor mEdit1 = sp.edit();
        mEdit1.putString("pref_level",nivels.get(pos));
        mEdit1.commit();

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
        return assetsImg;
    }

    private Map<String,Integer> setMapGame(){
        Map<String,Integer> elementos = new HashMap<String,Integer>();
        
        elementos.put("aros",R.drawable.aros);
        elementos.put("arreador",R.drawable.arreador);
        elementos.put("bajomontura",R.drawable.bajomontura);
        elementos.put("bozal",R.drawable.bozal);
        elementos.put("caballo",R.drawable.caballo);
        elementos.put("cabezada",R.drawable.cabezada);
        elementos.put("casco",R.drawable.casco);
        elementos.put("cascos",R.drawable.cascos);
        elementos.put("cepillo",R.drawable.cepillo);
        elementos.put("cinchon_de_volteo",R.drawable.cinchon_de_volteo);
        elementos.put("cola",R.drawable.cola);
        elementos.put("crines",R.drawable.crines);
        elementos.put("cuerda",R.drawable.cuerda);
        elementos.put("escarbavasos",R.drawable.escarbavasos);
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
        startTimer();

        SharedPreferences SP=PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if(isPreferencesChanged(SP)){
            gender=SP.getString("pref_voice_gender","m");
            String actualLevelPref=SP.getString("pref_level","Experto");
            recreate();
        }
    }

    private boolean isPreferencesChanged(SharedPreferences SP){
        String actualGenderPref=SP.getString("pref_voice_gender","m");
        String levelPref=SP.getString("pref_level","Experto");
        Set<String> imagesPref = SP.getStringSet("images", getDefaultSelectedWords());

        return  (gender != null && !actualGenderPref.equals(gender)) || (actualLevelPref != null && !levelPref.equals(actualLevelPref)) || imagesChanged(imagesPref);
    }

    private Boolean equalsArray(ArrayList<String> selectedPref){
        if(selected != null && selectedPref.size() == selected.size()){
            if(selectedPref.containsAll(selected)){
                return true;
            }
        }
        return false;
    }

    private Boolean imagesChanged(Set<String> imagesPref){
        if(!equalsArray(new ArrayList<String>(imagesPref))){
            resetContador();
            return true;
        }
        return false;
    }

    private void resetContador(){
        SharedPreferences sc = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEdit1 = sc.edit();
        mEdit1.putBoolean("Finaliza",false);
        mEdit1.putInt("Contador",0);
        mEdit1.commit();
    }

    private  void startTimer(){
        SharedPreferences SP=PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if(SP.getBoolean("pref_enable_time",false)){
            timerStopped=false;
            Integer segundos=Integer.parseInt(SP.getString("pref_time_value","10"));
            timer.sendMessageDelayed(timer.obtainMessage(),segundos*1000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerStopped=true;
    }

    private Set<String> getDefaultSelectedWords(){
        HashSet<String> wordsSet= new HashSet<String>();

        wordsSet.add("aros");
        wordsSet.add("arreador");
        wordsSet.add("bajomontura");
        wordsSet.add("bozal");
        wordsSet.add("caballo");
        wordsSet.add("cabezada");
        wordsSet.add("casco");
        wordsSet.add("cascos");
        wordsSet.add("cepillo");
        wordsSet.add("cinchon_de_volteo");
        wordsSet.add("cola");
        wordsSet.add("crines");
        wordsSet.add("cuerda");
        wordsSet.add("escarbavasos");
        wordsSet.add("fusta");
        wordsSet.add("matra");
        wordsSet.add("montura");
        wordsSet.add("monturin");
        wordsSet.add("ojos");
        wordsSet.add("orejas");
        wordsSet.add("palos");
        wordsSet.add("pasto");
        wordsSet.add("pelota");
        wordsSet.add("rasqueta");
        wordsSet.add("riendas");
        wordsSet.add("zanahoria");

        return wordsSet;
    }
}
