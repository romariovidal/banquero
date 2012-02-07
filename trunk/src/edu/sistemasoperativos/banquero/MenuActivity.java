package edu.sistemasoperativos.banquero;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MenuActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
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
        Banco b = (Banco) data.getExtras().getSerializable("banco");
        if(resultCode==1){
          Toast.makeText(this, "Leido de Banco " + b.getCantidadProcesos() + " Procesos  y " + b.getCantidadRecursos() + " Recursos", 2500).show();
        }
        else{
        	Toast.makeText(this, "No se termino de ingresar datos "+b, 2500).show();
        }
    }

}
