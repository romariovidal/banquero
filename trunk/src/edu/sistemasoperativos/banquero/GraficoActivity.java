package edu.sistemasoperativos.banquero;


import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

//Genera el layout y el lienzo y luego le pasa el banquero para que lo pinte
public class GraficoActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayout1);
       // Lienzo fondo=new Lienzo(this, Banco);        
       // linearLayout.addView(fondo);
    }
}