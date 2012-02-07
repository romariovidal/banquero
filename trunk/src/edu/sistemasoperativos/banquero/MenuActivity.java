package edu.sistemasoperativos.banquero;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends Activity {
	
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
        if(resultCode==1){
          Banco b = (Banco) data.getExtras().getSerializable("banco");
          Toast.makeText(this, "Leido de Banco " + b.getCantidadProcesos() + " Procesos  y " + b.getCantidadRecursos() + " Recursos", 2500).show();
        }
        else{
            
        }
    }

}
