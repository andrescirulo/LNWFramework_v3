package net.latin.client.test.inicio.rpc;

import net.latin.client.rpc.GwtRpcInterface;
import net.latin.client.test.inicio.pages.domain.Persona;
import net.latin.client.widget.base.RespuestRPC;
import net.latin.client.widget.base.SimpleRespuestRPC;

public interface InicioTestClient extends GwtRpcInterface{

	SimpleRespuestRPC getTextoInicial();
	
	RespuestRPC<Persona> getListaPersonas();
}
