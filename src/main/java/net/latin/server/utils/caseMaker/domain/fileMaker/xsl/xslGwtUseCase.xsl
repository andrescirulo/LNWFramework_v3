<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0"
    xmlns:java="http://xml.apache.org/xslt/java"
    exclude-result-prefixes="java">
    <xsl:output method="text" />
<xsl:template match="bean">
<xsl:variable name="useCaseName" select="useCaseName"/>
package <xsl:value-of select="server"/>;

import net.latin.server.GwtUseCase;
import <xsl:value-of select="rpc"/>.<xsl:value-of select="useCaseName"/>Client;
import <xsl:value-of select="appName"/>.client.UseCaseNames;

public class <xsl:value-of select="useCaseName"/>Case extends GwtUseCase implements <xsl:value-of select="useCaseName"/>Client {

	protected String getServiceName() {
		return UseCaseNames.<xsl:value-of select="java:net.latin.server.utils.caseMaker.domain.fileMaker.XslUtils.toConstantFormat($useCaseName)"/>;
	}

}



</xsl:template>
</xsl:stylesheet>