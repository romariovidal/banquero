package edu.sistemasoperativos.banquero;

import java.util.ArrayList;

public class Cliente extends Thread {
	
	public static final int ESTADO_SIN_INICIALIZAR = -1;
	public static final int ESTADO_ACTIVO = 0;
	public static final int ESTADO_ESPERA = 1;
	public static final int ESTADO_TERMINADO = 2;
	
	private ArrayList<Integer> recursosNecesarios = null;
	private ArrayList<Integer> recursosObtenidos = null;
	private Cajero cajero = null;
	private int identificador = -1;
	private int estado = -1;
	
	public Cliente(Cajero cajero, ArrayList<Integer> recursosNecesarios, int identificador) {
		this.cajero = cajero;
		this.recursosNecesarios = recursosNecesarios;
		inicializarRecursosObtenidos();
		this.identificador  = identificador;
	}

	private void inicializarRecursosObtenidos() {
		int recurso = 0;
		for(int i=0; i < recursosObtenidos.size(); i++) {
			recursosObtenidos.add(recurso, 0);
		}
	}

	public void run() {
		System.out.println("Proceso " + identificador + " COMENZO");
		while(necesitoRecursos()) {
			try {
				Thread.sleep(tiempoAleatorio());
			} catch (InterruptedException e) {}
			ArrayList<Integer> recursosASolicitar = makeVectorSolicitudRecursos();
			if(cajero.solicitarRecursos(recursosASolicitar,identificador) ) {
				descontarRecursos(recursosASolicitar);
				if(necesitoRecursos()) {
					setEstado(ESTADO_ACTIVO);
					System.out.println("Proceso " + identificador + " ACTIVO");
				}
				else {
					setEstado(ESTADO_TERMINADO);
					cajero.devolverRecursos(recursosObtenidos);
					System.out.println("Proceso " + identificador + " TERMINADO");
					return;
				}
			}
			else {
				setEstado(ESTADO_ESPERA);
				System.out.println("Proceso " + identificador + " EN ESPERA");
				try {
					Thread.sleep(tiempoAleatorio());
				} catch (InterruptedException e) {}
			}
		}
			
	}

	private void descontarRecursos(ArrayList<Integer> recursosASolicitar) {
		
		int recurso = 0;
		for(int cantidadRecurso : recursosASolicitar) {
			recursosObtenidos.add(recurso, recursosObtenidos.get(recurso) + cantidadRecurso);
			recurso++;
		}
	}

	private ArrayList<Integer> makeVectorSolicitudRecursos() {
		ArrayList<Integer> recursos = new ArrayList<Integer>();
		
		int recurso = 0;
		for(int cantidadRecurso : recursosObtenidos) {
			if( (recursosNecesarios.get(recurso) - cantidadRecurso) > 0) {
				recursos.add(recurso, 1);
			}
			else {
				recursos.add(recurso, 0);
			}
		}
		return null;
	}

	private long tiempoAleatorio() {
		return (long) (Math.random()*1000);
	}

	private boolean necesitoRecursos() {
		int recurso = 0;
		for(int recursoObtenido : recursosObtenidos) {
			if ((this.recursosNecesarios.get(recurso) - recursoObtenido) > 0) {
				return true;
			}
			recurso++;
		}
		return false;
	}

	public int getEstado() {
		return estado;
	}

	private void setEstado(int estado) {
		this.estado = estado;
	}
}
