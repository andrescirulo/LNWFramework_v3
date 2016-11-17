package net.latin.client.rpc.commonUseCase;

import com.google.gwt.user.client.rpc.AsyncCallback;

import net.latin.client.rpc.GwtRpcInterfaceAsync;

public interface CommonUseCaseClientAsync extends GwtRpcInterfaceAsync {

	public void getInitialInfo(AsyncCallback<?> callback);
	public void checkAccess(String serviceName, AsyncCallback<?> callback);
	public void generateTableDocument(String fileName, String fileType, LnwTableDocumentData data, AsyncCallback<?> callback);
	public void onUserExit(AsyncCallback<?> callback);
	public void getCaseDocumentation(String useCaseName, String pageName, AsyncCallback<?> callback);

}
