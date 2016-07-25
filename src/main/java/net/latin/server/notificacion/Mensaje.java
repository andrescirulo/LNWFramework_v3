package net.latin.server.notificacion;

import java.util.Calendar;

public class Mensaje{
		private int id;
		private String texto;
		private String tipo;
		private Calendar fechaDesde;
		private Calendar fechaHasta;
		
		public int getId() {
			return id;
		}
		public String getString() {
			return getTipo() + ";" + getTexto();
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getTexto() {
			return texto;
		}
		public void setTexto(String texto) {
			this.texto = texto;
		}
		public String getTipo() {
			return tipo;
		}
		public void setTipo(String tipo) {
			this.tipo = tipo;
		}
		public Calendar getFechaHasta() {
			return fechaHasta;
		}
		public void setFechaHasta(Calendar fechaHasta) {
			this.fechaHasta = fechaHasta;
		}
		public Calendar getFechaDesde() {
			return fechaDesde;
		}
		public void setFechaDesde(Calendar fechaDesde) {
			this.fechaDesde = fechaDesde;
		}
		
	}
