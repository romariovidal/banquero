package edu.sistemasoperativos.banquero;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

public class Banco implements Serializable {

	private HashMap<Integer,Cliente> clientes = new HashMap<Integer,Cliente>();
	private HashMap<Integer,Integer> recursosDisponibles = new HashMap<Integer,Integer>();
	private int cantidadRecursos = -1;
	private int cantidadProcesos = -1;
	
	public Banco() {
		
	}
	
	public void agregarCliente(int proceso, Cliente c) {
		clientes.put(proceso, c);
	}
	
	public Cliente obtenerCliente(int proceso) {
		return clientes.get(proceso);
	}
	
	public void limpiar() {
		clientes.clear();
		recursosDisponibles.clear();
	}
	
	public Collection<Cliente> getClientes() {
		return clientes.values();
	}

	public void setRecursoDisponible(int recurso, int cantidad) {
		recursosDisponibles.put(recurso, cantidad);		
	}
	
	public int getRecursoDisponible(int recurso) {
		Integer rec = recursosDisponibles.get(recurso);
		return (rec != null) ? rec : 0;
	}

	public void setCantidadRecursos(int cantidadRecursos) {
		this.cantidadRecursos = cantidadRecursos;
	}
	
	public int getCantidadRecursos() {
		return cantidadRecursos;
	}

	public void setCantidadProcesos(int cantidadProcesos) {
		this.cantidadProcesos = cantidadProcesos;
	}
	
	public int getCantidadProcesos() {
		return cantidadProcesos ;
	}
}
