package net.latin.client.test.inicio.pages;

import java.util.Date;

import gwt.material.design.client.ui.table.cell.TextColumn;
import net.latin.client.rpc.GwtRpc;
import net.latin.client.test.inicio.pages.domain.Persona;
import net.latin.client.test.inicio.rpc.InicioTestClientAsync;
import net.latin.client.widget.base.GwtPage;
import net.latin.client.widget.table.GwtMaterialTable;

public class TablasTestPage extends GwtPage {

	private InicioTestClientAsync server;
	private GwtMaterialTable<Persona> table;

	public TablasTestPage() {
		server = (InicioTestClientAsync)GwtRpc.getInstance().getServer( "InicioTestCase" );
		
		table = new GwtMaterialTable<Persona>();
		table.addColumn(new TextColumn<Persona>(){
			public String getValue(Persona object) {
				return object.getNombre();
			}
		},"Nombre");
		table.addColumn(new TextColumn<Persona>(){
			public String getValue(Persona object) {
				return object.getApellido();
			}
		},"Apellido");
		table.addColumn(new TextColumn<Persona>(){
			public String getValue(Persona object) {
				Date fecha =object.getFechaNacimiento(); 
				return fecha.getDate() + "/" + fecha.getMonth() + "/" + (fecha.getYear() + 1900);
			}
		},"Fecha Nacimiento");
		
		this.add(table);
	}
	
}
