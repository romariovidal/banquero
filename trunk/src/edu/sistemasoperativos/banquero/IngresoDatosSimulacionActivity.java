package edu.sistemasoperativos.banquero;

import android.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class IngresoDatosSimulacionActivity extends Activity {
	

	 private ArrayAdapter<String> adapterSpinnerProcesos = null;
	 private ArrayAdapter<String> adapterSpinnerRecursos = null;
	 
	 
	 private Banco banco = new Banco();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ingresodatos);
		
		adapterSpinnerProcesos = new ArrayAdapter<String>(this,layout.simple_spinner_item);
		adapterSpinnerProcesos.setDropDownViewResource(layout.simple_spinner_dropdown_item);
		Spinner spinnerProcesos = (Spinner) this.findViewById(R.id.spinnerProcesos);
		spinnerProcesos.setAdapter(adapterSpinnerProcesos);
		
		adapterSpinnerRecursos = new ArrayAdapter<String>(this,layout.simple_spinner_item);
		adapterSpinnerRecursos.setDropDownViewResource(layout.simple_spinner_dropdown_item);
		Spinner spinnerRecursos = (Spinner)this.findViewById(R.id.spinnerRecursos);
		spinnerRecursos.setAdapter(adapterSpinnerRecursos);
		
		Spinner spinnerRecursosDisponibles = (Spinner)this.findViewById(R.id.spinnerSeleccionRecursosDisponibles);
		spinnerRecursosDisponibles.setAdapter(adapterSpinnerRecursos);
		
	}
	
	public void onClickAceptarParametrosGenerales(View v) {
		adapterSpinnerProcesos.clear();
		adapterSpinnerRecursos.clear();
		
		banco.limpiar();
		
		
		
		EditText editTextProcesos = (EditText) this.findViewById(R.id.editTextCantidadProcesos);
		EditText editTextRecursos = (EditText) this.findViewById(R.id.editTextCantidadRecursos);
		
		String sCantidadProcesos = editTextProcesos.getText().toString();
		int cantidadProcesos = Integer.parseInt(sCantidadProcesos);
		
		String sCantidadRecursos = editTextRecursos.getText().toString();
		int cantidadRecursos = Integer.parseInt(sCantidadRecursos);
		

		banco.setCantidadRecursos(cantidadRecursos);
		banco.setCantidadProcesos(cantidadProcesos); 
		
		for(int proceso = 0; proceso < cantidadProcesos; proceso++) {
			adapterSpinnerProcesos.add("Proceso N "+proceso);
			Cliente c = new Cliente();
			c.setIdProceso(proceso);
			banco.agregarCliente(proceso, c);
		}
		
		for(int recurso = 0; recurso < cantidadRecursos; recurso++) {
			adapterSpinnerRecursos.add("Recurso N "+recurso);
		}
	}
	
	public void onClickSetearRecursoMaximo(View v) {
		Spinner spinnerProcesos = (Spinner) this.findViewById(R.id.spinnerProcesos);
		Spinner spinnerRecursos = (Spinner)this.findViewById(R.id.spinnerRecursos);
		EditText editTextCantidadRecurso = (EditText)this.findViewById(R.id.editTextCantidadRecursos);
		
		int selectedProceso = spinnerProcesos.getSelectedItemPosition();
		int selectedRecurso = spinnerRecursos.getSelectedItemPosition();
		int cantidadRecurso = Integer.parseInt(editTextCantidadRecurso.getText().toString());
		
		if(selectedProceso == AdapterView.INVALID_POSITION || selectedRecurso == AdapterView.INVALID_POSITION) {
			mensajeUsuario("Seleccione el recurso y el proceso");
			return;
		}
		
		if(((RadioButton) this.findViewById(R.id.radioButtonNecesario)).isChecked()) {
			banco.obtenerCliente(selectedProceso).setCantidadRecursoNecesario(selectedRecurso, cantidadRecurso);
			mensajeUsuario("Recurso Maximo guardado");
		}
		else {
			banco.obtenerCliente(selectedProceso).setCantidadRecursoObtenido(selectedRecurso, cantidadRecurso);
			mensajeUsuario("Recurso Asignado guardado");
		}
		
	}
	
	public void onClickFinalizarIngresoDatos(View v) {
		Intent in = new Intent();
		in.putExtra("banco", banco);
        setResult(1,in);
		this.finish();
	}
	
	public void onClickSetearRecursosDisponibles(View v) {
		Spinner spinnerRecursosDisponibles = (Spinner) this.findViewById(R.id.spinnerSeleccionRecursosDisponibles);
		EditText editTextCantidadRecursoDisponible = (EditText)this.findViewById(R.id.editTextIngresoRecursosDisponibles);
		
		String sCantidadRecursoDisponible = editTextCantidadRecursoDisponible.getText().toString();
		if(sCantidadRecursoDisponible.equalsIgnoreCase("")) {
			mensajeUsuario("Debe ingresar una cantidad para el recurso disponible del banco");
			return;
		}
		
		if(spinnerRecursosDisponibles.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
			mensajeUsuario("Debe seleccionar un recurso antes de ingresarlo a la lista de disponibles");
			return;
		}
		banco.setRecursoDisponible(spinnerRecursosDisponibles.getSelectedItemPosition(), Integer.parseInt(sCantidadRecursoDisponible));
		mensajeUsuario("Recurso Disponible guardado");
	}

	private void mensajeUsuario(String mensaje) {
		Toast.makeText(this, mensaje, 2500).show();
	}
	
	

}
