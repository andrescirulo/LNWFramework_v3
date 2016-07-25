package net.latin.server.commonUseCase;

import net.latin.client.widget.base.CustomBean;
import net.latin.client.widget.menu.data.MenuItem;
import net.latin.server.security.login.LnwMenuBuilder;

public class DefaultCommonUseCaseClientImpl implements CommonUseCaseImpl {

	@Override
	public CustomBean getInitialInfo() {
		return new CustomBean();
	}

	public MenuItem getGwtMenu() {
		return LnwMenuBuilder.buildGwtMenu();
	}

	@Override
	public String getCaseDocumentation(String useCaseName, String pageName) {
		return "Actualmente no existe documentación para esta sección";
	}

}
