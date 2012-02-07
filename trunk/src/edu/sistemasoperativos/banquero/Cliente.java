package edu.sistemasoperativos.banquero;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Cliente implements Serializable {
	
	public static final int ESTADO_SIN_INICIALIZAR = -1;
	public static final int ESTADO_ACTIVO = 0;
	public static final int ESTADO_ESPERA = 1;
	public static final int ESTADO_TERMINADO = 2;
	
	private HashMap<Integer,Integer> recursosNecesarios = new HashMap<Integer,Integer>();
	private HashMap<Integer,Integer> recursosObtenidos = new HashMap<Integer,Integer>();

	private int idProceso = -1;
	private int estado = ESTADO_SIN_INICIALIZAR;
	
	public Cliente() {
		
	}
	
	public int getCantidadRecursoNecesario(int recurso) {
		Integer rec = recursosNecesarios.get(recurso);
		return (rec != null ? rec : 0);
	}
	
	public void setCantidadRecursoNecesario(int recurso, int cantidad) {
		recursosNecesarios.put(recurso, cantidad);
	}
	
	public int getCantidadRecursoObtenido(int recurso) {
		Integer rec = recursosObtenidos.get(recurso);
		return (rec != null ? rec : 0);
	}
	
	public void setCantidadRecursoObtenido(int recurso, int cantidad) {
		recursosObtenidos.put(recurso, cantidad);
	}
	
	public int getCantidadRecursos() {
		return recursosNecesarios.keySet().size();
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public int getIdProceso() {
		return idProceso;
	}

	public void setIdProceso(int idProceso) {
		this.idProceso = idProceso;
	}
}
