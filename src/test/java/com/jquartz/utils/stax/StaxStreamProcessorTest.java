package com.jquartz.utils.stax;

import com.jquartz.schema.*;
import com.jquartz.utils.AbstractMarshalUnmarshalTest;

import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * @author timurnav
 */
public class StaxStreamProcessorTest extends AbstractMarshalUnmarshalTest {

    private static final String PROJECT = "Project";
    private static final String USERS = "Users";
    private static final String GROUP = "Group";
    private static final String CITIES = "Cities";

    @Override
    protected Payload unmarshal(InputStream inputStream) throws Exception {
        try (StaxStreamProcessor processor = new StaxStreamProcessor(inputStream)) {

            Payload payload = new Payload();

            Payload.Projects projects = new Payload.Projects();
            projects.getProject().addAll(readProjects(processor));
            payload.setProjects(projects);

            Payload.Cities cities = new Payload.Cities();
            cities.getCity().addAll(readCities(processor));
            payload.setCities(cities);

            List<Group> groups = projects.getProject().stream()
                    .flatMap(project -> project.getGroup().stream())
                    .collect(Collectors.toList());

            Payload.Users users = new Payload.Users();
            users.getUser().addAll(readUsers(processor, groups, cities.getCity()));
            payload.setUsers(users);

            return payload;
        }
    }

    @Override
    protected String marshal(Payload payload) throws Exception {
        //todo implement
        return "";
    }

    @Override
    protected String getFileName() {
        return "StAX";
    }

    private List<User> readUsers(StaxStreamProcessor processor, List<Group> groups, List<City> cities) throws XMLStreamException {
        List<User> users = new ArrayList<>();
        while (processor.doUntilAny(START_ELEMENT, "User") != null) {
            User user = new User();
            users.add(user);
            user.setFullName(processor.getAttribute("fullName"));
            user.setEmail(processor.getAttribute("email"));
            user.setFlag(FlagType.valueOf(processor.getAttribute("flag").toUpperCase()));
            String cityId = processor.getAttribute("city");
            user.setCity(cities.stream()
                    .filter(c -> c.getId().equals(cityId))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "City with id " + cityId + " is not found!")));
            String groupRefsAttribute = Optional.ofNullable(processor.getAttribute("groupRefs")).orElse("");
            List<String> groupRefs = Arrays.asList(groupRefsAttribute.split(" "));
            user.getGroupRefs().addAll(
                    groups.stream().filter(g -> groupRefs.contains(g.getName())).collect(Collectors.toList())
            );
        }
        return users;
    }

    private List<City> readCities(StaxStreamProcessor processor) throws XMLStreamException {
        List<City> cities = new ArrayList<>();
        String event;
        while ((event = processor.doUntilAny(START_ELEMENT, "City", USERS)) != null
                && !event.equals(USERS)) {
            City city = new City();
            cities.add(city);
            city.setId(processor.getAttribute("id"));
            city.setValue(processor.getValue(START_ELEMENT));
        }
        return cities;
    }

    private List<Project> readProjects(StaxStreamProcessor processor) throws XMLStreamException {
        List<Project> projects = new ArrayList<>();
        String event = processor.doUntilAny(START_ELEMENT, PROJECT, CITIES);
        while (event != null && !event.equals(CITIES)) {
            Project project = new Project();
            projects.add(project);
            project.setTitle(processor.getAttribute("title"));
            project.setDescription(processor.getElementValue("description"));
            List<Group> projectGroups = new ArrayList<>();
            while ((event = processor.doUntilAny(START_ELEMENT, GROUP, PROJECT, CITIES)) != null
                    && !event.equals(PROJECT) && !event.equals(CITIES)) {
                Group group = new Group();
                projectGroups.add(group);
                group.setName(processor.getAttribute("name"));
                group.setType(GroupType.valueOf(processor.getAttribute("type")));
            }
            project.getGroup().addAll(projectGroups);
        }
        return projects;
    }
}