package net.latin.client.widget.uploader.rpc;

import net.latin.client.rpc.GwtRpcInterface;
import net.latin.client.widget.base.SimpleRespuestRPC;

public interface GwtDownloadFileClient extends GwtRpcInterface {
	SimpleRespuestRPC prepareForDownload(String fileId);
}
