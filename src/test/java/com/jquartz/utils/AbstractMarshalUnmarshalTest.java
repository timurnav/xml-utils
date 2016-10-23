package com.jquartz.utils;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.google.common.io.Resources;
import com.jquartz.schema.Payload;
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
import static java.util.Collections.disjoint;
import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

/**
 * @author timurnav
 */
public abstract class AbstractMarshalUnmarshalTest {

    private static final String PAYLOAD_XML = "/payload.xml";

    private static final Comparator<User> USER_COMPARATOR = Comparator.comparing(User::getFullName).thenComparing(User::getEmail);
    private static final Comparator<Project> PROJECT_COMPARATOR = Comparator.comparing(Project::getTitle);

    //@Test todo implement unmarshaling in all child classes first
    public void unmarshalObjectThanMarshalObjectAndCheck() throws Exception {
        for (String filePath : ImmutableSet.of(PAYLOAD_XML)) {
            String result;
            try (InputStream inputStream = openStream(filePath)) {
                Payload unmarshaled = unmarshal(inputStream);
                result = marshal(unmarshaled);
            }
            String readRawXml = readString(filePath);
            assertXMLEqual(result, readRawXml);
        }
    }

    @Test
    public void outAsHtml() throws Exception {
        String htmlAsString;
        try (InputStream inputStream = openStream(PAYLOAD_XML)) {
            htmlAsString = getHtml(inputStream);
        }
        try (Writer writer = Files.newBufferedWriter(Paths.get("out", getFileName() + ".html"))) {
            writer.write(htmlAsString);
        }
    }

    protected String getHtml(InputStream inputStream) throws Exception {
        Payload payload = unmarshal(inputStream);
        Multimap<Project, User> projectUserMap = mapUsersOnProject(payload);
        return getHtmlAsString(projectUserMap);
    }

    protected Multimap<Project, User> mapUsersOnProject(Payload payload) {
        Multimap<Project, User> result = TreeMultimap.create(PROJECT_COMPARATOR, USER_COMPARATOR);
        for (User user : payload.getUsers().getUser()) {
            payload.getProjects().getProject().stream()
                    .filter(project -> !disjoint(project.getGroup(), user.getGroupRefs()))
                    .forEach(project -> result.put(project, user));
        }
        return result;
    }

    protected String getHtmlAsString(Multimap<Project, User> projectsWithUsers) {
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
        ContainerTag html = html().with(
                head().with(title("Testing " + getFileName())),
                body
        );
        return html.render();
    }

    protected abstract String marshal(Payload payload) throws Exception;

    protected abstract Payload unmarshal(InputStream inputStream) throws Exception;

    protected abstract String getFileName();

    protected InputStream openStream(String cityFile) throws IOException {
        return Resources.getResource(AbstractMarshalUnmarshalTest.class, cityFile).openStream();
    }

    private String readString(String cityFile) throws IOException {
        try (InputStream inputStream = Resources.getResource(JaxbParserTest.class, cityFile).openStream()) {
            Scanner s = new Scanner(inputStream).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
    }
}
