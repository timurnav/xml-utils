package com.jquartz.utils.jaxb;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.jquartz.schema.ObjectFactory;
import com.jquartz.schema.Payload;
import com.jquartz.schema.Project;
import com.jquartz.schema.User;
import com.jquartz.utils.AbstractMarshalUnmarshalTest;

import java.io.InputStream;

import static java.util.Collections.disjoint;

/**
 * @author timurnav
 */
public class JaxbParserTest extends AbstractMarshalUnmarshalTest {

    private final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    @Override
    protected String marshal(Object unmarshaled) throws Exception {
        String marshaled = JAXB_PARSER.marshal(unmarshaled);
        JAXB_PARSER.validate(marshaled);
        return marshaled;
    }

    @Override
    protected Object unmarshal(InputStream inputStream) throws Exception {
        return JAXB_PARSER.unmarshal(inputStream);
    }

    @Override
    protected Multimap<Project, User> parseProjectsWithUsers(InputStream inputStream) throws Exception {
        Payload payload = JAXB_PARSER.unmarshal(inputStream);

        Multimap<Project, User> result = TreeMultimap.create(PROJECT_COMPARATOR, USER_COMPARATOR);

        for (User user : payload.getUsers().getUser()) {
            payload.getProjects().getProject().stream()
                    .filter(project -> !disjoint(project.getGroup(), user.getGroupRefs()))
                    .forEach(project -> result.put(project, user));
        }

        return result;
    }

    @Override
    protected String getFileName() {
        return "JAXB";
    }
}