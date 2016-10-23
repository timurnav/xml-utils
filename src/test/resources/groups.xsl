<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
    <xsl:output method="html" omit-xml-declaration="yes" indent="yes"/>

    <xsl:template match="/">
        <html>
            <head>
                <title>Testing StAX</title>
            </head>
            <body>
                <xsl:for-each select="//*[name()='Project']">
                    <h1>
                        <xsl:value-of select="@title"/>
                    </h1>
                    <h4>
                        <xsl:value-of select="*[name()='description']"/>
                    </h4>
                    <xsl:variable name="project_title" select="@title"/>
                    <xsl:variable name="underscore">
                        <xsl:text>_</xsl:text>
                    </xsl:variable>
                    <!--http://stackoverflow.com/questions/4420382/can-we-use-dynamic-variable-name-in-the-select-statement-in-xslt-->
                    <table border="1" cellpadding="8" cellspacing="0">
                        <tr>
                            <th>Full name</th>
                            <th>email</th>
                        </tr>
                        <!--<xsl:for-each select="exslt:node-set(concat($first, $project_title, $last))">-->
                        <xsl:for-each
                                select="//*[name()='User'][contains(@groupRefs, concat($project_title, $underscore))]">
                            <tr>
                                <td>
                                    <xsl:value-of select="@fullName"/>
                                </td>
                                <td>
                                    <xsl:value-of select="@email"/>
                                </td>
                            </tr>
                        </xsl:for-each>
                    </table>
                    <hr/>
                </xsl:for-each>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>