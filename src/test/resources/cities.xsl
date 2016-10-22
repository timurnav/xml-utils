<xsl:stylesheet version="1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text" omit-xml-declaration="yes" indent="no"/>
    <xsl:strip-space elements="*"/>
    <xsl:template match="//*[name()='City']">
        <xsl:copy-of select="." />
        <xsl:text>&#xa;</xsl:text><!--/n-->
    </xsl:template>
    <xsl:template match="text()"/>
</xsl:stylesheet>