<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0"
    xmlns:java="http://xml.apache.org/xslt/java"
    exclude-result-prefixes="java">
<xsl:output method="text" />
<xsl:template match="bean">
<xsl:variable name="useCaseName" select="useCaseName"/>
package <xsl:value-of select="constants"/>;

public class <xsl:value-of select="useCaseName"/>Constants  {

	public static final String TITLE = "title";



}



</xsl:template>
</xsl:stylesheet>