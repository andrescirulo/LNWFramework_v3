<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0"
    xmlns:java="http://xml.apache.org/xslt/java"
    exclude-result-prefixes="java">
    <xsl:output method="text" />
<xsl:template match="bean">
package <xsl:value-of select="rpc"/>;

import net.latin.client.rpc.GwtRpcInterface;

public interface <xsl:value-of select="useCaseName"/>Client extends GwtRpcInterface {


}
</xsl:template>
</xsl:stylesheet>