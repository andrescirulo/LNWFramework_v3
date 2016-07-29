package net.latin.server.test.inicio;

import net.latin.client.test.inicio.rpc.InicioTestClient;
import net.latin.client.widget.base.SimpleRespuestRPC;
import net.latin.server.GwtUseCase;

public class InicioTestCase extends GwtUseCase implements InicioTestClient {

	@Override
	protected String getServiceName() {
		return "InicioTestCase";
	}

	@Override
	public SimpleRespuestRPC getTextoInicial() {
		SimpleRespuestRPC res = new SimpleRespuestRPC();
		res.setMensaje("Este es el mensaje de bienvenida");
		return res;
	}

}
