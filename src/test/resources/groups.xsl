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
                    <xsl:sort select="@title"/>
                    <xsl:variable name="project_title" select="@title"/>
                    <xsl:variable name="users_xpath" select="//*[name()='User'][contains(@groupRefs, $project_title)]"/>
                    <xsl:if test="$users_xpath">
                        <h1>
                            <xsl:value-of select="@title"/>
                        </h1>
                        <h4>
                            <xsl:value-of select="*[name()='description']"/>
                        </h4>
                        <table border="1" cellpadding="8" cellspacing="0">
                            <tr>
                                <th>Full name</th>
                                <th>email</th>
                            </tr>
                            <xsl:for-each
                                    select="$users_xpath">
                                <xsl:sort select="@fullName"/>
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
                    </xsl:if>
                </xsl:for-each>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>