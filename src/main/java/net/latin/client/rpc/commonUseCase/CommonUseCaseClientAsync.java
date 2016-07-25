package net.latin.client.rpc.commonUseCase;

import net.latin.client.rpc.GwtRpcInterfaceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CommonUseCaseClientAsync extends GwtRpcInterfaceAsync {

	public void getInitialInfo(AsyncCallback<?> callback);
	public void checkAccess(String serviceName, AsyncCallback<?> callback);
	public void generateTableDocument(String fileName, String fileType, LnwTableDocumentData data, AsyncCallback<?> callback);
	public void onUserExit(AsyncCallback<?> callback);
	public void getCaseDocumentation(String useCaseName, String pageName, AsyncCallback<?> callback);

}
