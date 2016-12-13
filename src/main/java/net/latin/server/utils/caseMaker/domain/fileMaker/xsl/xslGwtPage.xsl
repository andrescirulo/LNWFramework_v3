<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0"
    xmlns:java="http://xml.apache.org/xslt/java"
    exclude-result-prefixes="java">
    <xsl:output method="text" />
<xsl:output method="text" />
<xsl:param name="pageName"/>
<xsl:template match="bean">
<xsl:variable name="useCaseName" select="useCaseName"/>
package <xsl:value-of select="pages"/>;

import net.latin.client.rpc.GwtAsyncCallback;
import net.latin.client.widget.base.GwtPage;
import <xsl:value-of select="appName"/>.client.UseCaseNames;
import <xsl:value-of select="rpc"/>.<xsl:value-of select="useCaseName"/>ClientAsync;
import net.latin.client.rpc.GwtRpc;

public class <xsl:value-of select="$pageName"/> extends GwtPage {

	private <xsl:value-of select="useCaseName"/>ClientAsync server;

	public <xsl:value-of select="$pageName"/>() {

		//Configuramos el server con la interfaz asyncronica y el nombre del caso de uso
		server = (<xsl:value-of select="useCaseName"/>ClientAsync)GwtRpc.getInstance().getServer( UseCaseNames.<xsl:value-of select="java:net.latin.server.utils.caseMaker.domain.fileMaker.XslUtils.toConstantFormat($useCaseName)"/> );

	}

	/**
	* Cuando la página se hace visible
	*/
	public void onVisible() {

	}

}



</xsl:template>
</xsl:stylesheet>