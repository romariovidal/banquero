package edu.sistemasoperativos.banquero;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

public class Banco extends Thread implements Serializable {

	private HashMap<Integer, Cliente> clientes = new HashMap<Integer, Cliente>();
	private HashMap<Integer, Integer> recursosDisponibles = new HashMap<Integer, Integer>();
	private int cantidadRecursos = -1;
	private int cantidadProcesos = -1;
	private SimulacionListener escucha = null;

	public Banco() {

	}

	public void run() {
		boolean enEjecucion = true;
		boolean faltaRecursos = false;
		while (enEjecucion & !faltaRecursos) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {}

			faltaRecursos = true;// verificaremos esto
			enEjecucion = false; // si algun proceso se ejecuta en el ciclo,
									// estamos en ejecucion.
			for (Cliente c : clientes.values()) {

				switch (c.getEstado()) {
				case Cliente.ESTADO_ACTIVO:
					recuperarRecursos(c);
					c.setEstado(Cliente.ESTADO_TERMINADO);
					faltaRecursos = false;
					enEjecucion = true;
					break;
				case Cliente.ESTADO_TERMINADO:
					// no hay nada mas que hacer con el cliente
					break;
				default:
					if (estadoSeguroAlAsignarACliente(c)) {
						asignarRecursosSolicitados(c);
						c.setEstado(Cliente.ESTADO_ACTIVO);
						faltaRecursos = false;
						enEjecucion = true;
					} else {
						c.setEstado(Cliente.ESTADO_ESPERA);
					}
				}
			}
			escucha.pasoSimulacion(this);
		}
		escucha.terminoSimulacion(this);
	}

	private boolean estadoSeguroAlAsignarACliente(Cliente c) {
		boolean estadoSeguro = true;
		for (int recurso = 0; recurso < this.cantidadRecursos; recurso++) {
			estadoSeguro = estadoSeguro
					& (c.getCantidadRecursoObtenido(recurso)
							+ this.getRecursoDisponible(recurso) >= c
								.getCantidadRecursoNecesario(recurso));
			if (!estadoSeguro) {
				return false;
			}
		}
		return estadoSeguro;
	}

	private void asignarRecursosSolicitados(Cliente c) {
		for (int recurso = 0; recurso < this.cantidadRecursos; recurso++) {
			int cantidadNecesaria = c.getCantidadRecursoNecesario(recurso)
					- c.getCantidadRecursoObtenido(recurso);
			c.setCantidadRecursoObtenido(recurso,
					c.getCantidadRecursoObtenido(recurso) + cantidadNecesaria);
			this.setRecursoDisponible(recurso,
					this.getRecursoDisponible(recurso) - cantidadNecesaria);
		}
	}

	private void recuperarRecursos(Cliente c) {
		for (int recurso = 0; recurso < this.cantidadRecursos; recurso++) {
			this.setRecursoDisponible(
					recurso,
					this.getRecursoDisponible(recurso)
							+ c.getCantidadRecursoObtenido(recurso));
		}
	}

	public void setSimulacionListener(SimulacionListener sim) {
		this.escucha = sim;
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
		return cantidadProcesos;
	}
}
