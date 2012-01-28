package com.matrix.prueba;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

public class MatrixActivity extends Activity {
    /** Called when the activity is first created. */
	Button boton = null;
	Button asignar = null;
	int indice=0;
	int indice_mat_asignados=0;
	
	EditText cella=null;
    EditText cell=null ;
    EditText cellb=null;
    
    EditText procesos=null;
    EditText recursos=null;
    
    List<EditText> recdisp = new ArrayList<EditText>();
    List<EditText> columnas = new ArrayList<EditText>();
    List<EditText> columnasmax = new ArrayList<EditText>();
    
    int asignados[][]=null;
    int maximos[][]=null;
    Integer disponibles[]=null;
    
    Integer filas;
    Integer columnasmatriz;
    
    //int cprocesos;
    //int crecursos;
    
    //InputMethodManager imm;
    
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
       // imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);	
        
        boton= (Button) findViewById(R.id.button1);
       
        boton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				crear_matriz();
			}
		});
        
        asignar = (Button) findViewById(R.id.button2);
        asignar.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				asignarvalores();
			}
		});
   	
    }
    
    
    //Crear matrices en interfaz
    public void crear_matriz(){
    	
    System.out.println("Hola");
    
    //definir campos de texto
    procesos = (EditText) findViewById(R.id.editText1);
    
    //imm.hideSoftInputFromWindow(procesos.getWindowToken(), 0);
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(procesos.getApplicationWindowToken(), 0);
    /*Integer*/ filas = Integer.valueOf(procesos.getText().toString());
    
    recursos = (EditText) findViewById(R.id.editText2);
    
    //imm.hideSoftInputFromWindow(recursos.getWindowToken(), 0);
    
    /*Integer*/ columnasmatriz = Integer.valueOf(recursos.getText().toString());
    
    //int p= 5;
    //p= p+n;
    
    //definir tabla y fila
    TableLayout recdisponibles = new TableLayout(this);
    TableRow filarecdisponibles = new TableRow(this);
    
    //crear celdas correspondientes a cada columna y adherirlas a la fila
    for (int i = 0; i < columnasmatriz; i++) {
    	cella = new EditText(this);
    	cella.setWidth(70);
        cella.setHeight(20);
        cella.setInputType(InputType.TYPE_CLASS_NUMBER);
        filarecdisponibles.addView(cella);
        recdisp.add(cella);
 
    }
    
    //adherir fila a la tabla
    recdisponibles.addView(filarecdisponibles, new TableLayout.LayoutParams(
            LayoutParams.FILL_PARENT,
            LayoutParams.WRAP_CONTENT));
    
    //adherir tabla en el sector respectivo de la interfaz
    LinearLayout ltablardisp = (LinearLayout) findViewById(R.id.linearLayout9);
    ltablardisp.addView(recdisponibles);
    
  
    System.out.println("filas "+ filas);
    System.out.println("columnas "+ columnasmatriz);
      
    
    TableLayout table = new TableLayout(this);
    // se puede definir tambien un TableLayout table = (TableLayout) findViewById(R.id.tableLayout1);
    for (int i = 0; i < filas; i++) {
        TableRow row = new TableRow(this);
        for (int j = 0; j < columnasmatriz; j++) {
            /*EditText*/ cell = new EditText(this);
            cell.setId(indice);
           
            //cell.setText("(" + i + ", " + j + ")"); 
            //cell.setText(" ");
           
            cell.setWidth(70);
            cell.setHeight(20);
            cell.setHighlightColor(25);
            cell.setInputType(InputType.TYPE_CLASS_NUMBER);
            row.addView(cell);
            
            columnas.add(cell);
            int id= cell.getId();
        
            System.out.println(id);
            indice++;
  
        }

        	table.addView(row, new TableLayout.LayoutParams(
            LayoutParams.FILL_PARENT,
            LayoutParams.WRAP_CONTENT));
  		
    }

    LinearLayout ltabla = (LinearLayout) findViewById(R.id.linearLayout6);
    ltabla.addView(table);
    
    
    TableLayout mrecursosn = new TableLayout(this);
    
    for (int i = 0; i < filas; i++) {
        TableRow row = new TableRow(this);
        for (int j = 0; j < columnasmatriz; j++) {
            /*EditText*/ cellb = new EditText(this);
            //cellb.setText("(" + i + ", " + j + ")");
            cellb.setWidth(70);
            cellb.setHeight(20);
            cellb.setInputType(InputType.TYPE_CLASS_NUMBER);
            row.addView(cellb);
            columnasmax.add(cellb);
        }
        	mrecursosn.addView(row, new TableLayout.LayoutParams(
            LayoutParams.FILL_PARENT,
            LayoutParams.WRAP_CONTENT));
    }
    
    	LinearLayout ltablarn = (LinearLayout) findViewById(R.id.linearLayout7);
    	ltablarn.addView(mrecursosn);
    	
    	boton.setEnabled(false);
    	
    }
    
    //pasar valores a las matrices respectivas para operar con ellos
    public void asignarvalores(){
    	
    	asignados = new int[filas][columnasmatriz];
    	
    	maximos = new int[filas][columnasmatriz];
    	//cprocesos= Integer.valueOf(columnas.get(5).getText());
    	
    	//recorrido para ver el contenido de cada matriz representado en dos vectores
    	Log.e("matriz uno","datos matriz asignados");
    	String[] strings = new String[columnas.size()];

        for(int i=0; i < columnas.size(); i++){
            strings[i] = columnas.get(i).getText().toString();
            System.out.println(strings[i]);
 
        }
        
        Log.e("matriz uno","datos matriz asignados entrados");
        
        //asignar valores a la matriz de asignados
        for (int i = 0; i < filas; i++) {
            
            for (int j = 0; j < columnasmatriz; j++) {
            	
            	//String elemento=columnas.get(indice_mat_asignados).getText().toString();
            	
            	//asignados[i][j]= Integer.valueOf(elemento);
            	asignados[i][j]= Integer.valueOf(columnas.get(indice_mat_asignados).getText().toString());
            	
            	maximos[i][j]= Integer.valueOf(columnasmax.get(indice_mat_asignados).getText().toString());
            	
            	indice_mat_asignados++;
            	
            	Log.e("matriz uno","datos matriz de asignados entrados");
            	System.out.println(asignados[i][j]);
            	
            	
            	 Log.e("matriz uno","datos matriz de maximos entrados");
            	System.out.println(maximos[i][j]);
                
            }
            	
        }
        
        int suma = maximos[1][0]+asignados[1][0];
        System.out.println("resultado suma"+suma);
        
        Log.e("matriz dos","datos matriz maximos");
    	String[] stringsmax = new String[columnasmax.size()];

        for(int i=0; i < columnasmax.size(); i++){
            stringsmax[i] = columnasmax.get(i).getText().toString();
            System.out.println(stringsmax[i]); 
        }
        
        Log.e("matriz uno","datos vector recursos disponibles");
        String[] stringsrdisp = new String[recdisp.size()];
        for(int i=0; i < recdisp.size(); i++){
            stringsrdisp[i] = recdisp.get(i).getText().toString();
            System.out.println(stringsrdisp[i]); 
        }
    	
        
        for (int i = 0; i < filas; i++) {
            
            for (int j = 0; j < columnasmatriz; j++) {
            	
            	//String elemento=columnas.get(indice_mat_asignados).getText().toString();
            	
            	//asignados[i][j]= Integer.valueOf(elemento);
            	
            	if((asignados[i][j]+Integer.valueOf(recdisp.get(j).getText().toString())> maximos[i][j]  ))
            			{
            		
            		System.out.println(asignados[i][j]+Integer.valueOf(recdisp.get(j).getText().toString())); 
            		System.out.println("Mayor"); 
            	}
                
            }
            	
        }//fin operaciones
        
    	
    }
    
}