package edu.sistemasoperativos.banquero;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

public class MenuActivity extends Activity {

	private Banco banco = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
	}

	public void onClickComenzarSimulacion(View v) {
		if (banco == null) {
			Toast.makeText(this,
					"Debe Ingresar primero los datos de la simulacion", 2500)
					.show();
			return;
		}

		int requestCode = 0;
		Intent intent = new Intent(this, GraficoActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.putExtra("banco", banco);
		startActivityForResult(intent, requestCode);
	}

	public void onClickAcercaDe(View v) {

	}

	public void onClickIngresarDatosSimulacion(View v) {
		int requestCode = 0;
		Intent intent = new Intent(this, IngresoDatosSimulacionActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			banco = (Banco) data.getExtras().getSerializable("banco");
			if (resultCode == 1) {
				Toast.makeText(
						this,
						"Leido de Banco " + banco.getCantidadProcesos()
								+ " Procesos  y " + banco.getCantidadRecursos()
								+ " Recursos", 2500).show();
			} else {
				Toast.makeText(
						this,
						"Debe dar aceptar en el boton de ingreso de datos para comenzar la simulacion",
						2500).show();
			}
		}
	}

}
