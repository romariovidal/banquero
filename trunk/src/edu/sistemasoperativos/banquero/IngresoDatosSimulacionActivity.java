package edu.sistemasoperativos.banquero;

import android.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class IngresoDatosSimulacionActivity extends Activity {
	

	 private ArrayAdapter<String> adapterSpinnerProcesos = null;
	 private ArrayAdapter<String> adapterSpinnerRecursos = null;
	 private boolean modoMatriz = false;
	 
	 
	 private Banco banco = new Banco();
	
	@Override
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
		if(modoMatriz) {
			actualizarTablasEnBaseAModelo();
		}
	}
	
	public void onClickSetearRecursoMaximo(View v) {
		Spinner spinnerProcesos = (Spinner) this.findViewById(R.id.spinnerProcesos);
		Spinner spinnerRecursos = (Spinner)this.findViewById(R.id.spinnerRecursos);
		EditText editTextCantidadRecurso = (EditText)this.findViewById(R.id.editTextCantidadRecursosIngreso);
		
		int selectedProceso = spinnerProcesos.getSelectedItemPosition();
		int selectedRecurso = spinnerRecursos.getSelectedItemPosition();
		int cantidadRecurso = Integer.parseInt(editTextCantidadRecurso.getText().toString());
		
		if(selectedProceso == AdapterView.INVALID_POSITION || selectedRecurso == AdapterView.INVALID_POSITION) {
			mensajeUsuario("Seleccione el recurso y el proceso");
			return;
		}
		
		if(((RadioButton) this.findViewById(R.id.radioButtonNecesario)).isChecked()) {
			banco.obtenerCliente(selectedProceso).setCantidadRecursoNecesario(selectedRecurso, cantidadRecurso);
			mensajeUsuario("Recurso Maximo guardado (" +selectedProceso + "," + selectedRecurso+")="+cantidadRecurso );
		}
		else {
			banco.obtenerCliente(selectedProceso).setCantidadRecursoObtenido(selectedRecurso, cantidadRecurso);
			mensajeUsuario("Recurso Asignado guardado (" +selectedProceso + "," + selectedRecurso+")="+cantidadRecurso );
		}
		
	}
	
	public void onClickFinalizarIngresoDatos(View v) {
		if(modoMatriz) {
			//actualizamos los datos en el modelo, de acuerdo a lo que el usuario ingreso en las tablas.
			actualizarModeloEnBaseATablas();
		}
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
	
	public void onclickVerMatrices(View v) {
		actualizarGuiPorCambioModo();
		
		ViewSwitcher viewSwitcherIngreso = (ViewSwitcher) this.findViewById(R.id.viewSwitcherIngreso);
		viewSwitcherIngreso.showNext();
	}
	
	public void onClickVerIngresoDatos(View v) {
		actualizarGuiPorCambioModo();
		
		ViewSwitcher viewSwitcherIngreso = (ViewSwitcher) this.findViewById(R.id.viewSwitcherIngreso);
		viewSwitcherIngreso.showPrevious();
	}
	
	@Override
	public void onBackPressed() {
		if(this.banco.getCantidadProcesos() > 0 & this.banco.getCantidadRecursos() > 0) {
			this.onClickFinalizarIngresoDatos(null);
		}
		else {
			mensajeUsuario("Debe Seleccionar cuantos procesos y recursos va a utilizar, presione finalizar cuando haya terminado");
		}
	}
	
	private void actualizarModeloEnBaseATablas() {
		TableLayout tablaLayoutRecursosMaximos = (TableLayout) this.findViewById(R.id.tableLayoutTablaMaximo);
		
		TableLayout tablalayoutRecursosAsignados = (TableLayout) this.findViewById(R.id.tableLayoutAsignado);
		
		TableRow rowVectorRecursosDisponibles = (TableRow) this.findViewById(R.id.tableRowVectorRecursosDisponibles);
		
		for(int proceso = 1; proceso < tablaLayoutRecursosMaximos.getChildCount(); proceso++) {
			TableRow filaRecursoMaximo = (TableRow) tablaLayoutRecursosMaximos.getChildAt(proceso);
			TableRow filaRecursoAsignado = (TableRow) tablalayoutRecursosAsignados.getChildAt(proceso);
			
			for(int recurso = 1; recurso < filaRecursoMaximo.getChildCount(); recurso++) {
				EditText editTextRecursoMaximo = (EditText) filaRecursoMaximo.getChildAt(recurso);
				EditText editTextRecursoAsignado = (EditText) filaRecursoAsignado.getChildAt(recurso);
				int cantidadRecursoNecesario = Integer.parseInt(editTextRecursoMaximo.getText().toString());
				int cantidadRecursoAsignado = Integer.parseInt(editTextRecursoAsignado.getText().toString());
				
				banco.obtenerCliente(proceso-1).setCantidadRecursoNecesario(recurso-1, cantidadRecursoNecesario);
				banco.obtenerCliente(proceso-1).setCantidadRecursoObtenido(recurso-1, cantidadRecursoAsignado);
			}
		}
		
		for(int recurso = 0; recurso < rowVectorRecursosDisponibles.getChildCount(); recurso++) {
			EditText editTextRecursoDisponible = (EditText) rowVectorRecursosDisponibles.getChildAt(recurso);
			int cantidadRecursoDisponible = Integer.parseInt(editTextRecursoDisponible.getText().toString());
			banco.setRecursoDisponible(recurso, cantidadRecursoDisponible);
		}
	}
	
	private OnTouchListener touchListener = new OnTouchListener() {

		public boolean onTouch(View v, MotionEvent event) {
			EditText e = (EditText)v;
			e.setSelection(0, e.getText().length());
			return false;
		}
		
	};
	
	private void actualizarTablasEnBaseAModelo() {
		TableLayout tablaLayoutRecursosMaximos = (TableLayout) this.findViewById(R.id.tableLayoutTablaMaximo);
		tablaLayoutRecursosMaximos.removeAllViews();
		
		TableLayout tablalayoutRecursosAsignados = (TableLayout) this.findViewById(R.id.tableLayoutAsignado);
		tablalayoutRecursosAsignados.removeAllViews();
		
		TableRow rowVectorRecursosDisponibles = (TableRow) this.findViewById(R.id.tableRowVectorRecursosDisponibles);
		TableRow rowTituloVectorRecursosDisponibles = (TableRow) this.findViewById(R.id.tableRowTituloVectorRecursosDisponibles);
		rowTituloVectorRecursosDisponibles.removeAllViews();
		rowVectorRecursosDisponibles.removeAllViews();
		
		//INGRESO DE LA MATRIZ DE MAXIMOS Y ASIGNADOS REQUERIDOS
		


		TableRow rowMaxRequerido = new TableRow(this);
		TableRow rowAsignado= new TableRow(this);
		TextView nombreRecursoR = new TextView(this);
		TextView nombreRecursoA = new TextView(this);
		nombreRecursoR.setText("P/R");
		nombreRecursoA.setText("P/R");
		rowMaxRequerido.addView(nombreRecursoR);
		rowAsignado.addView(nombreRecursoA);
		for(int i = 0; i < banco.getCantidadRecursos(); i++) {
			nombreRecursoR = new TextView(this);
			nombreRecursoA = new TextView(this);
			nombreRecursoR.setText("R"+i);
			nombreRecursoA.setText("R"+i);
			rowMaxRequerido.addView(nombreRecursoR);
			rowAsignado.addView(nombreRecursoA);
		}
		tablaLayoutRecursosMaximos.addView(rowMaxRequerido);
		tablalayoutRecursosAsignados.addView(rowAsignado);
		
		for(Cliente c : banco.getClientes()) {
			rowMaxRequerido = new TableRow(this);
			rowAsignado= new TableRow(this);
			
			TextView viewTextProcesoR = new TextView(this);
			TextView viewTextProcesoA = new TextView(this);
			viewTextProcesoR.setText("P"+c.getIdProceso());
			viewTextProcesoA.setText("P"+c.getIdProceso());
			rowMaxRequerido.addView(viewTextProcesoR);
			rowAsignado.addView(viewTextProcesoA);
			
			for(int i = 0; i < banco.getCantidadRecursos(); i++) {
				EditText textoRecursoMaximo = new EditText(this);
				textoRecursoMaximo.setOnTouchListener(touchListener);
				textoRecursoMaximo.setInputType(InputType.TYPE_CLASS_NUMBER);
				EditText textoRecursoAsignado = new EditText(this);
				textoRecursoAsignado.setOnTouchListener(touchListener);
				textoRecursoAsignado.setInputType(InputType.TYPE_CLASS_NUMBER);
				
				textoRecursoMaximo.setText(c.getCantidadRecursoNecesario(i)+"");
				textoRecursoAsignado.setText(c.getCantidadRecursoObtenido(i)+"");
				rowMaxRequerido.addView(textoRecursoMaximo);
				rowAsignado.addView(textoRecursoAsignado);
			}
			tablaLayoutRecursosMaximos.addView(rowMaxRequerido);
			tablalayoutRecursosAsignados.addView(rowAsignado);
		}
		
		for(int i = 0; i < banco.getCantidadRecursos(); i++) {
			TextView nombreRecurso = new TextView(this);
			nombreRecurso.setText("R"+i);
			rowTituloVectorRecursosDisponibles.addView(nombreRecurso);
			
			EditText editTextRecurso = new EditText(this);
			editTextRecurso.setOnTouchListener(touchListener);
			editTextRecurso.setInputType(InputType.TYPE_CLASS_NUMBER);
			editTextRecurso.setText(banco.getRecursoDisponible(i)+"");
			rowVectorRecursosDisponibles.addView(editTextRecurso);
		}
	}
	
	private void actualizarGuiPorCambioModo() {
		if(modoMatriz) {
			if(banco == null) {
				banco = new Banco();
			}
			actualizarModeloEnBaseATablas();
		}
		else {
			actualizarTablasEnBaseAModelo();
		}
		modoMatriz = !modoMatriz;
	}

	private void mensajeUsuario(String mensaje) {
		Toast.makeText(this, mensaje, 2500).show();
	}
	
	

}
