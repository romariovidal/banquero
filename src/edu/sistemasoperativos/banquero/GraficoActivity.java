package edu.sistemasoperativos.banquero;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

//Genera el layout y el lienzo y luego le pasa el banquero para que lo pinte
public class GraficoActivity extends Activity implements SimulacionListener {

	
	// No borre esto, es necesario para el handler.
    private GraficoActivity yo = null;
    private Lienzo lienzo = null;
	private Banco banco = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	banco = (Banco) this.getIntent().getExtras().get("banco");
    	banco.setSimulacionListener(this);
        
        LinearLayout linearLayout = new LinearLayout(this);
        setContentView(linearLayout);     
        lienzo = new Lienzo(this);
       linearLayout.addView(lienzo,LayoutParams.WRAP_CONTENT , LayoutParams.FILL_PARENT);
    }

    public void onResume() {
    	super.onResume();
    	banco.start();
    }

    
	
	private Handler hand = new Handler() {
		
		public void handleMessage(Message mes) {
			Banco banco = (Banco) mes.obj;
			lienzo.refrescar(banco);
		}
	};

	public void pasoSimulacion(Banco banco) {
		//No borre esto, es necesario para el handler.
		yo = this;
		Message msg = new Message();
		msg.obj = banco;
		hand.sendMessage(msg);
	}

	public void terminoSimulacion(Banco banco) {
		//AQUI SE LE INFORMA QUE SE TERMINO LA SIMULACION
		Message msg = new Message();
		msg.obj = banco;
		hand.sendMessage(msg);
		banco = null;
	}
}