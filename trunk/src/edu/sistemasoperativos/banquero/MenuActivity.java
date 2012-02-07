package edu.sistemasoperativos.banquero;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

public class MenuActivity extends Activity implements SimulacionListener {
	
	private Banco banco = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
	}
	
	public void onClickComenzarSimulacion(View v) {
		if(banco == null) {
			Toast.makeText(this, "Debe Ingresar primero los datos de la simulacion", 2500).show();
			return;
		}
		banco.setSimulacionListener(this);
		banco.start();
	}
	
	public void onClickAcercaDe(View v) {
		
	}
	
	public void onClickIngresarDatosSimulacion(View v) {
		int requestCode = 0;
		Intent intent = new Intent(this,IngresoDatosSimulacionActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivityForResult(intent, requestCode);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        banco = (Banco) data.getExtras().getSerializable("banco");
        if(resultCode==1){
          Toast.makeText(this, "Leido de Banco " + banco.getCantidadProcesos() + " Procesos  y " + banco.getCantidadRecursos() + " Recursos", 2500).show();
        }
        else{
        	Toast.makeText(this, "Debe dar aceptar en el boton de ingreso de datos para comenzar la simulacion", 2500).show();
        }
    }
	
	private MenuActivity yo = null;
	
	private Handler hand = new Handler() {
		
		public void handleMessage(Message mes) {

			Toast.makeText(yo,mes.obj.toString(),2500).show();
			
		}
	};

	public void pasoSimulacion(Banco banco) {
		yo = this;
		String mensaje = "";
		for(Cliente c : banco.getClientes()) {
			mensaje += "P"+c.getIdProceso() + " ESTADO ";
			switch(c.getEstado()) {
			
			case Cliente.ESTADO_ACTIVO:
				mensaje+= "Activo. ";
				break;
			case Cliente.ESTADO_ESPERA:
				mensaje+= "Espera. ";
				break;
			case Cliente.ESTADO_SIN_INICIALIZAR:
				mensaje+= "Sin iniciar. ";
				break;
			case Cliente.ESTADO_TERMINADO:
				mensaje+= "Terminado. ";
				break;
			}
		}

		Message msg = new Message();
		msg.obj = mensaje;
		hand.sendMessage(msg);
	}

	public void terminoSimulacion(Banco banco) {
		Message msg = new Message();
		msg.obj = "Simulacion Terminada";
		hand.sendMessage(msg);
		banco = null;
	}

}
