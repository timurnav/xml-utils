package com.jquartz.utils;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.io.Resources;
import com.jquartz.schema.Project;
import com.jquartz.schema.User;
import com.jquartz.utils.jaxb.JaxbParserTest;
import j2html.tags.ContainerTag;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Scanner;

import static j2html.TagCreator.*;
import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

/**
 * @author timurnav
 */
public abstract class AbstractMarshalUnmarshalTest {

    private static final String PAYLOAD_XML = "/payload.xml";

    protected static final Comparator<User> USER_COMPARATOR = Comparator.comparing(User::getValue).thenComparing(User::getEmail);
    protected static final Comparator<Project> PROJECT_COMPARATOR = Comparator.comparing(Project::getTitle);

    @Test
    public void unmarshalObjectThanMarshalObjectAndCheck() throws Exception {
        for (String filePath : ImmutableSet.of("/city.xml", PAYLOAD_XML)) {
            String result;
            try (InputStream inputStream = openStream(filePath)) {
                Object unmarshaled = unmarshal(inputStream);
                result = marshal(unmarshaled);
            }
            String readRawXml = readString(filePath);
            assertXMLEqual(result, readRawXml);
        }
    }

    @Test
    public void outAsHtml() throws Exception {
        try (InputStream inputStream = openStream(PAYLOAD_XML)) {
            outHtml(parseProjectsWithUsers(inputStream));
        }
    }

    private void outHtml(Multimap<Project, User> projectsWithUsers) throws Exception {
        ContainerTag body = body();

        projectsWithUsers.keySet()
                .forEach(project -> {
                    final ContainerTag table = table().with(tr().with(th("Full name"), th("email")));
                    projectsWithUsers.get(project).forEach(u -> table.with(tr().with(td(u.getFullName()), td(u.getEmail()))));
                    table.setAttribute("border", "1");
                    table.setAttribute("cellpadding", "8");
                    table.setAttribute("cellspacing", "0");

                    body.with(h1(project.getTitle()), h4(project.getDescription()), table, hr());

                });

        try (Writer writer = Files.newBufferedWriter(Paths.get("out", getFileName() + ".html"))) {
            String page = html().with(
                    head().with(title("Testing " + getFileName())),
                    body
            ).render();
            writer.write(page);
        }
    }

    protected abstract String marshal(Object unmarshaled) throws Exception;

    protected abstract Object unmarshal(InputStream inputStream) throws Exception;

    protected abstract Multimap<Project, User> parseProjectsWithUsers(InputStream inputStream) throws Exception;

    protected abstract String getFileName();

    private InputStream openStream(String cityFile) throws IOException {
        return Resources.getResource(JaxbParserTest.class, cityFile).openStream();
    }

    private String readString(String cityFile) throws IOException {
        try (InputStream inputStream = Resources.getResource(JaxbParserTest.class, cityFile).openStream()) {
            Scanner s = new Scanner(inputStream).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
    }
}
