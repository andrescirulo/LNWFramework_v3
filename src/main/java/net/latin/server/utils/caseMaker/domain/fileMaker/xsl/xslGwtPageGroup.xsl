<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0"
    xmlns:java="http://xml.apache.org/xslt/java"
    exclude-result-prefixes="java">
    <xsl:output method="text" />
<xsl:template match="bean">
<xsl:variable name="useCaseName" select="useCaseName"/>
<xsl:variable name="firstPage" select="firstPage"/>
<xsl:variable name="pages" select="pages"/>
package <xsl:value-of select="group"/>;

import <xsl:value-of select="appName"/>.client.UseCaseNames;
import com.google.gwt.core.client.GWT;
import net.latin.client.rpc.GwtRpc;
import net.latin.client.rpc.GwtServerCreator;
import net.latin.client.widget.base.GwtPageGroup;
import <xsl:value-of select="rpc"/>.<xsl:value-of select="useCaseName"/>Client;

<xsl:for-each select="pageName">
import <xsl:value-of select="$pages"/>.<xsl:value-of select="."/>;
</xsl:for-each>

public class <xsl:value-of select="useCaseName"/>Group extends GwtPageGroup implements GwtServerCreator {

<xsl:for-each select="pageName">
<xsl:variable name="page" select="."/>
		public static final String <xsl:value-of select="java:net.latin.server.utils.caseMaker.domain.fileMaker.XslUtils.toConstantFormat($page)"/> = "<xsl:value-of select="."/>";
</xsl:for-each>

		protected String registerFirstPage() {
			return <xsl:value-of select="java:net.latin.server.utils.caseMaker.domain.fileMaker.XslUtils.toConstantFormat($firstPage)"/>;
		}

		protected void registerPages() {
		<xsl:for-each select="pageName">
		<xsl:variable name="page" select="."/>
			this.addPage(<xsl:value-of select="java:net.latin.server.utils.caseMaker.domain.fileMaker.XslUtils.toConstantFormat($page)"/>, new <xsl:value-of select="."/>());
		</xsl:for-each>
		}

		protected void registerRpcServers() {
			GwtRpc.getInstance().addServer( UseCaseNames.<xsl:value-of select="java:net.latin.server.utils.caseMaker.domain.fileMaker.XslUtils.toConstantFormat($useCaseName)"/>, "<xsl:value-of select="java:net.latin.server.utils.caseMaker.domain.fileMaker.XslUtils.unCapitalize($useCaseName)"/>.gwt" , this );
		}

		public Object createServer() {
			return GWT.create(<xsl:value-of select="useCaseName"/>Client.class);
		}

}



</xsl:template>
</xsl:stylesheet>