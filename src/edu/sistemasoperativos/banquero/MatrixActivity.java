package edu.sistemasoperativos.banquero;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

public class MatrixActivity extends Activity {

	Button boton = null;
	Button asignar = null;
	
	
	int indice=0;												//indice para filas al crear la interfaz de matriz de recursos asignados, con este se maneja la etiqueta de las filas
	int indicematrizzdos=0;										//indice que sirve de etiqueta para las filas de  datos en la matriz de recursos maximos
	int indice_mat_asignados=0;         						//indice para vector de matriz de recursos asignados
	

	EditText cella=null;										//celda para vector de recursos disponibles
    EditText cell=null ;										//celda para matriz de recursos asignados
    EditText cellb=null;										//celda para matriz de recursos maximos
    
    EditText procesos=null;
    EditText recursos=null;
    
    List<EditText> recdisp = new ArrayList<EditText>();				//Arreglo que contiene los datos de recursos disponibles
    List<EditText> columnas = new ArrayList<EditText>();			//Arreglo que contiene los datos de cada columna de la matriz de recursos asignados
    List<EditText> columnasmax = new ArrayList<EditText>();			//Arreglo que contiene los datos de cada columna de la matriz de recursos maximos
    List<TableRow> filaschequeadas= new ArrayList<TableRow>();		//arreglo que contiene las filas para controlar cuales se han chequeado
    
    int asignados[][]=null;											//matriz de recursos asignados
    int maximos[][]=null;											//matriz de recursos maximos requeridos
    int disponibles[]=null;											//vector de recursos disponibles
    int filachequeada=0;    										//contador de filas verificadas
    
    ArrayList<Integer> dispauxiliar = new ArrayList<Integer>();		//Arreglo auxiliar para almacenar los recursos disponibles y operar con ellos
    
    
    boolean banderamayor=false;										//bandera para verificar que la suma ha sido mayor o igual
    boolean banderaexe=true;										//bandera para determinar si hay datos a operar
    
    int procesoenejecucion;											//variable para especificar la fila actual en ejecución

    Integer filas;													//numero de filas
    Integer columnasmatriz;											//numero de columnas
    
    Toast procesoejec;												//Toast para desplegar el proceso en ejecución
    
    TableLayout table=null;											//Layout para la interfaz de matriz de recursos asignados
    TableRow row=null ;												//elemento fila para la interfaz de rcursos asignados
  
    
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        procesos = (EditText) findViewById(R.id.editText1);
        recursos = (EditText) findViewById(R.id.editText2);
        
        procesoejec=Toast.makeText(this,"Ingrese datos", Toast.LENGTH_SHORT);
    	
        
        boton= (Button) findViewById(R.id.button1);					//boton para generar las matrices
        asignar = (Button) findViewById(R.id.button2);				//boton para asignar valores ingresados a las matrices con las que operará
        //asignar.setEnabled(false);
        //asignar.setBackgroundColor(Color.TRANSPARENT);
        asignar.setVisibility(View.INVISIBLE);						//no mostrar boton hasta que se creen las matrices
        
        boton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				if ( procesos.getText().length()==0 || recursos.getText().length()==0 ){
					  procesoejec.show();
					  System.out.println("INGRESE elementos");	
				}
				else {crear_matriz();								//crear la interfaz de matrices
				//asignar.setEnabled(true);
				asignar.setVisibility(View.VISIBLE);				//hacer visible el boton para iniciar calculos
				}										
					
			}
		});
        
        asignar.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				asignarvalores();									//asignar valores a las matrices
			}
		});
   	
    }
    
    
    //Crear matrices en interfaz
    public void crear_matriz(){
    	
    System.out.println("Hola");										//punto de control -borrar-
    
    //definir campos de texto
    procesos = (EditText) findViewById(R.id.editText1);
    filas = Integer.valueOf(procesos.getText().toString());				//obtener numero de procesos o filas ingresados
    
    recursos = (EditText) findViewById(R.id.editText2);
    columnasmatriz = Integer.valueOf(recursos.getText().toString());	//obtener numero de columnas o recursos ingresados
    
    //definir tabla y fila de recursos disponibles
    TableLayout recdisponibles = new TableLayout(this);
    TableRow filarecdisponibles = new TableRow(this);
    
    //crear celdas correspondientes a cada columna y adherirlas a la fila
    for (int i = 0; i < columnasmatriz; i++) {
    	cella = new EditText(this);
    	cella.setWidth(70);												//establecer ancho de celda
        cella.setHeight(20);											//establecer alto de celda
        cella.setInputType(InputType.TYPE_CLASS_NUMBER);				//especificar tipo de dato que recibe
        filarecdisponibles.addView(cella);
        recdisp.add(cella);												//adherir celda al vector de recursos disponibles
 
    }
    
    //adherir fila a la tabla
    recdisponibles.addView(filarecdisponibles, new TableLayout.LayoutParams(
            LayoutParams.FILL_PARENT,
            LayoutParams.WRAP_CONTENT));
    
    //adherir tabla en el sector respectivo de la interfaz
    LinearLayout ltablardisp = (LinearLayout) findViewById(R.id.linearLayout9);
    ltablardisp.addView(recdisponibles);
    
  
    System.out.println("filas "+ filas);								//punto de control -Borrar-
    System.out.println("columnas "+ columnasmatriz);					//punto de control -Borrar-
      
    
    table = new TableLayout(this);
    // se puede definir tambien un TableLayout table = (TableLayout) findViewById(R.id.tableLayout1);
    
    //iniciar recorrido para la creación de la matriz de recursos asignados
    for (int i = 0; i < filas; i++) {
        row = new TableRow(this);										//creacion de una nueva fila
        row.setId(indice); 												//asignar indice a la fila
        TextView nombre= new TextView(this);							//etiqueta para el numero de fila
        nombre.setText(String.valueOf(indice));							//asignar valor a la etiqueta
        
        row.addView(nombre);											//adherir etiqeuta a la fila
        
        //recorrido por columnas
        for (int j = 0; j < columnasmatriz; j++) {
            cell = new EditText(this);									//creacion de campo de Texto que representa las celdas de la matriz
  
            cell.setWidth(70);											//establcer ancho de celda
            cell.setHeight(20);											//establecer alto de celda
          
            cell.setInputType(InputType.TYPE_CLASS_NUMBER);				//especificar tipo de dato que recibe
            row.addView(cell);							
            filaschequeadas.add(row);									//adherir fila a vector que contiene las filas chequeadas
            columnas.add(cell);											//adherir celda al arreglo de recursos asigandos

            int id= cell.getId();										//punto de control -borrar-
            System.out.println("este es un id"+id);						//punto de control -borrar-
           					
  
        }

        	table.addView(row, new TableLayout.LayoutParams(			//adherir elementos a la tabla que representa la matriz de recursos asignados
            LayoutParams.FILL_PARENT,
            LayoutParams.WRAP_CONTENT));
        	
        	indice++;													//incrementar indice de filas
  		
    }

    LinearLayout ltabla = (LinearLayout) findViewById(R.id.linearLayout6);
    ltabla.addView(table);												//adherir tabla a la interfez
    
    
    TableLayout mrecursosn = new TableLayout(this);						//Tabla para la matriz de recursos maximos
    
    //Creación de la matriz de recursos maximos
    for (int i = 0; i < filas; i++) {
    	
        TableRow row = new TableRow(this);								//creación de fila
        row.setId(indicematrizzdos); 									//asignacion de id
        TextView nombre= new TextView(this);							//creacion de etiqueta de fila
        nombre.setText(String.valueOf(indicematrizzdos));				//asignar valor de etiqueta de filas
        row.addView(nombre);											//adherir etiqueta de numero de fila a las filas
        
        //recorrido por columnas
        for (int j = 0; j < columnasmatriz; j++) {
            cellb = new EditText(this);									//creacion de celdas para la matriz
            cellb.setWidth(70);											//determinacion de ancho de celda
            cellb.setHeight(20);										//determinacion de alto de celda
            cellb.setInputType(InputType.TYPE_CLASS_NUMBER);			//especificacion del tipo de dato que recibe
            row.addView(cellb);											//adherir celda a la fila
            columnasmax.add(cellb);										//adherir celda a vector de recursos maximos
        }
        	mrecursosn.addView(row, new TableLayout.LayoutParams(		//adherir filas a la tabla de recursos maximos
            LayoutParams.FILL_PARENT,
            LayoutParams.WRAP_CONTENT));
        	
        	indicematrizzdos++;											//incrementar indice que sirve de etiqueta
    }
    
    	LinearLayout ltablarn = (LinearLayout) findViewById(R.id.linearLayout7);
    	ltablarn.addView(mrecursosn);
    	
    	//boton.setEnabled(false);										//deshabiliar botón de generación de matrices para operar solo con los datos de recursos y procesos inicialmente ingresados
    	boton.setVisibility(View.INVISIBLE);							//ocultar boton
    	
    }
    
    //pasar valores a las matrices respectivas para operar con ellos
    public void asignarvalores(){
    	
    	banderaexe=true;												//reasignación de valor para la bandera que controla la existencia de recursos
    	
    	//verificar si hay datos en las celdas del vector de recursos disponibles 
    	for(int i=0; i < recdisp.size(); i++){
         	if (((recdisp.get(i).getText().length())==0)){
         		
         		banderaexe=false;										//asignar valor falso a la bandera en caso que no existan recursos, por tanto no se pueden realizar las operaciones del algoritmo
         		continue;
         		
         	}
         	else {banderaexe=true;
         		 break;}

         }
    	//en caso de que no haya algún dato en el vector no se puede iniciar las operaciones
    	if(banderaexe==false){
    		
    		System.out.println("No hay datos");							//punto de control -borrar-
    		
    		//configuracion de mensaje para indicar que no hay ejecucion de procesos en vista de que no hay recursos disponibles
        	procesoejec=Toast.makeText(this,"No hay procesos en ejecucion", 
        			Toast.LENGTH_SHORT);
        	
        	System.out.println("llego al toast");						//punto de control -borrar-
        		
        	procesoejec.show();											//despliegue de mensaje	
    	}
    	//en caso de que el valor de la bandera de ejecución sea cierto, esto indica que se pueden iniciar las operaciones
    	else{ 
    	for (int a=0;a<filaschequeadas.size();a++){
    		
    		//poner transparencia al fondo de la fila, esto limpia el fondo cuando se quiere volver a ejecutar
    			filaschequeadas.get(a).setBackgroundColor(Color.TRANSPARENT);	
    	}
    	
    	//creacion de matrices y vector dada la dimension ingresada. Estas que contendran los datos a operar
    	asignados = new int[filas][columnasmatriz];						//Matriz de recursos asignados			
    	maximos = new int[filas][columnasmatriz];						//Matriz de recursos máximos requeridos
    	disponibles = new int[columnasmatriz];							//Vector de recursos disponibles
    	
    	filachequeada=filas;											//iniciar el valor de filas a chequear
    	
    	System.out.println("cantidad de filas"+ filachequeada);			//punto de cotrol-borrar-
    
    	
    	Log.e("Matriz uno","recursos asginados");					//punto de cotrol-borrar-
    	
    	
    	//verificar contenido de los vectores y matrices por consola
    	String[] strings = new String[columnas.size()];
        
    	//asignar valor cero a las celdas contenidas en el vector de recursos asignados
        for(int i=0; i < columnas.size(); i++){
        	
        	if (((columnas.get(i).getText().length())==0)){
        		columnas.get(i).setText("0");
        		
        	}
        	
        	//Punto de control. Verificar datos del vector
            strings[i] = columnas.get(i).getText().toString();
            System.out.println(strings[i]);
        }
        
        Log.e("matriz dos","datos matriz maximos");				//punto de cotrol-borrar-
        
    	String[] stringsmax = new String[columnasmax.size()];
    	
    	//asignar valor cero a las celdas contenidas en el vector de recursos maximos requeridos
        for(int i=0; i < columnasmax.size(); i++){
        	
        	if (((columnasmax.get(i).getText().length())==0)){
        		columnasmax.get(i).setText("0");
        		
        	}
        	
        	//Punto de control. Verificar datos del vector
            stringsmax[i] = columnasmax.get(i).getText().toString();
            System.out.println(stringsmax[i]); 
        }
        
        Log.e("matriz uno","datos matriz asignados entrados");	//punto de cotrol-borrar-
        
        //Recorrido para asignar valores a la matriz de recursos asignados
        for (int i = 0; i < filas; i++) {
        	for (int j = 0; j < columnasmatriz; j++) {
        		
        		//Asignar valores contenidos en los vectores a las matrices respectivas
            	asignados[i][j]= Integer.valueOf(columnas.get(indice_mat_asignados).getText().toString());
            	
            	maximos[i][j]= Integer.valueOf(columnasmax.get(indice_mat_asignados).getText().toString());
            	
            	indice_mat_asignados++;										//incrementar indice de vector. Fue iniciado en cero
            	
            	Log.e("matriz uno","datos matriz de asignados entrados");	//punto de cotrol-borrar-
            	
            	
            	System.out.println(asignados[i][j]);						//Punto de cotrol. Mostrar en consola datos de la matriz de recursos asignados
       
            	Log.e("matriz uno","datos matriz de maximos entrados");		//punto de cotrol-borrar-
            	System.out.println(maximos[i][j]);							//Punto de cotrol. Mostrar en consola datos de la matriz de máximos requeridos
                
            }
            	
        }
        
        //reiniciar valores de indices en caso de una nueva ejecución
        indice=0;
    	indice_mat_asignados=0;
    
        int suma = maximos[1][0]+asignados[1][0];	//punto de control -borrar-
        System.out.println("resultado suma"+suma);	//punto de control -borrar-
        
    
        Log.e("matriz uno","datos vector recursos disponibles");			//punto de control -borrar-
        String[] stringsrdisp = new String[recdisp.size()];
        
       //asignar valor cero a las celdas contenidas en el vector de recursos disponibles -se puede mover arriba-
        for(int i=0; i < recdisp.size(); i++){
        	if (((recdisp.get(i).getText().length())==0)){
        		recdisp.get(i).setText("0");

        	}
        	
        	//Punto de control. Verificar contenido del vector
            stringsrdisp[i] = recdisp.get(i).getText().toString();
            System.out.println(stringsrdisp[i]); 
        }
        
        //pasar recursos disponibles contenidos en el vector a arreglo
        for(int i=0; i < recdisp.size(); i++){
        	
        	disponibles[i]= Integer.valueOf(recdisp.get(i).getText().toString());
        	
        }
        
        
    	
        /*recorrido para sumar elementos del arreglo que contiene los recursos disponibles con la matriz de asignados.
        El resultado se compara contra el maximo*/
        while(filachequeada!=0){
        
        for (int i = 0; i < filas; i++) {
            
        	//verificar si la fila está marcada como ejecutada
        	if((asignados[i][0])== -1){
        		continue;												//continuar el recorrido de filas
        	}
        	dispauxiliar.clear();										//limpiar vector de datos auxiliares

            for (int j = 0; j < columnasmatriz; j++) {

            	//verficar si la suma es mayor o igual al máximo de recursos necesarios
            	if((asignados[i][j]+disponibles[j]>= maximos[i][j]))
            		{
            		
            		System.out.println(asignados[i][j]+disponibles[j]);	//Punto de control: Verificar suma en consola
            		System.out.println("Mayor"); 
            		
            		
            		procesoenejecucion=i;								//inidicar proceso o fila ejecutada
        
            		dispauxiliar.add((asignados[i][j]+disponibles[j]));	//mover valor de la suma al arreglo de valores auxiliares
            		
            		banderamayor=true;									//indicar en la bandera que hay un recurso que el proceso puede usar
            		continue;											//continuar recorrido en la siguiente columna
            	}
            	//si la suma no es mayor o igual indicarlo en la bandera
            	else{
            		
            		System.out.println(asignados[i][j]+disponibles[j]);	//Punto de control. verificar en consola el resultado de la suma
            		System.out.println("No es mayor"); 
            		
            		banderamayor=false;									//indicar que el proceso no se puede ejecutar
            		dispauxiliar.clear();								//limpiar arreglo auxiliar
            		break;												//suspender recorrido de columnas e inicar en la nueva fila
            	}
            }
            //verificar si luego de las comparaciones en cada columna de una fila el resultado fue maor 
            //y no se alteró la bandera, por tanto el proceso se puede ejecutar
            if(banderamayor==true){
            	
            	String procesoejecutado= "Proceso En ejecución P"+procesoenejecucion;      //Crear string de mensaje que indica la fila o proceso con resultados satisfactorios
            	
            	//Configurar mensaje que indica el proceso que se ejecuta
            	procesoejec=Toast.makeText(this,procesoejecutado, Toast.LENGTH_SHORT);
            	
            	System.out.println("llego al toast");										//punto de control -borrar-
            	
            	procesoejec.show();															//mostrar mensaje
            	
            	
            	
            	System.out.println("proceso en ejecucion "+procesoenejecucion); 			//punto de control. Verificar en consola fila en ejecución
            	
            	asignados[procesoenejecucion][0]= -1;										//marcar fila o proceso como ejecutado
            	
            	System.out.println("vector segnalado"+(asignados[procesoenejecucion][0]+ 5));//punto de control. -borrar-
            	
            	//recorrido en arreglo de filas chequeadas
            	for (int a=0;a<filaschequeadas.size();a++){
            		
            		//verificar si el indice de la fila corresponde al proceso o fila que se ha marcado como ejecutado
            		if(filaschequeadas.get(a).getId()==procesoenejecucion){
            			
            			//poner color de fondo a las filas, indicando los procesos que se han ejecutado
            			filaschequeadas.get(a).setBackgroundColor(Color.argb(150, 0, 255, 0));
            			
            			//System.out.println("paso a eliminar fila");						//punto de control -borrar-
            			//table.removeView(filaschequeadas.get(a));							//segunda opción:remover filas que hacen referencia a procesos ejecutados
            			
            			/*Asignar valores cero en la interfaz a las filas de procesos ejecutados.
            			Esto para indicar que se usaron y se devolvieron los recursos asignados.
            			Internamente la fila está marcada en la matríz como ejecutada por lo que no se toca*/
            			
            			for (int s=1;s<(filaschequeadas.get(a).getChildCount());s++){
            			EditText editada = (EditText) filaschequeadas.get(a).getChildAt(s);	//obtener hijos o celdas en la fila
                    	editada.setText("0");												//asignar valor cero al hijo o celda de la fila
            			}
            		}
            	}
            	//recorrido para actualizar el arreglo de recursos disponibles
            	for(int k=0;k<recdisp.size();k++){

            		System.out.println("vector auxiliar"+dispauxiliar.get(k));				//punto de control. Verificar contenido del arreglo de datos auxiliares
            		
            		disponibles[k]=dispauxiliar.get(k);										//actualizar arreglo de recursos disponibles con los valores del arreglo auxiliar
            		
            		System.out.println("nuevo valor de k "+disponibles[k]);					//punto de control. Verificar nuevo contenido del vector
            		
            		
            		recdisp.get(k).setText(String.valueOf(disponibles[k])) ;				//actualizar valores del vector de recursos disponibles para mostrarlos en la interfaz
            		
            	}
            	
            
            }//fin true
    
        }//fin operaciones
        
        filachequeada--;														//decrementar filas chequeadas 
    }//fin chequeo de filas
        
    //verificar si hubo un resultado que no fue mayor al maximo, por lo que la bandera no se modificó. Esto indica un estado no seguro
    if(banderamayor==false){
        	
    		//configurar y desplegar mensaje que indica que se ha llegado a un estado no seguro
        	procesoejec=Toast.makeText(this,
    				"Estado no seguro, se ha llegado a un interbloqueo!", Toast.LENGTH_SHORT);
        	procesoejec.show();
    }    
        
   }//cierre del else en caso de que hayan elementos o procesos en ejecución   
        
   
   }//fin metodo


}