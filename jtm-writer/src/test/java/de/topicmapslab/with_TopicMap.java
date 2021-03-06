package de.topicmapslab;

import org.junit.Before;
import org.tmapi.core.*;

import java.util.ArrayList;

/**
 * Author:  mhoyer
 * Created: 27.10.2010 17:39:06
 */
public class with_TopicMap {
    protected TopicMapSystem tms;
    protected TopicMap topicMap;

    @Before public void given_a_TopicMap() throws TMAPIException {
        tms = TopicMapSystemFactory.newInstance().newTopicMapSystem();
        topicMap = tms.createTopicMap("class:" + getClass().getName());
    }

    protected Association createAssociation(String typeName, String... roles) {
        Topic assocType = createSimpleNamedTopic(typeName);
        Association association = topicMap.createAssociation(assocType);

        for(String role: roles)
        {
            String[] roleTypeAndPlayer = role.split(":");

            if (roleTypeAndPlayer.length >= 2) {
                association.createRole(
                        createSimpleNamedTopic(roleTypeAndPlayer[0]),
                        createSimpleNamedTopic(roleTypeAndPlayer[1])
                );
            }
        }

        return association;
    }

    protected Topic createSimpleNamedTopic(String typeName) {
        return createTopic(typeName, getClass().getPackage().getName() + "/" + typeName);
    }

    protected TopicMap createTopicMap(String locator) throws TopicMapExistsException {
        return tms.createTopicMap("class:" + locator);
    }

    protected Topic createTopic(String name, String subjectIdentifier)
    {
        Topic topic = topicMap.createTopicBySubjectIdentifier(createLocator(subjectIdentifier));
        topic.createName(name);

        return topic;
    }

    protected Locator createLocator(String locator)
    {
        return tms.createLocator("class:" + locator);
    }

    protected String getIdentifier(Topic topic) {
        return ((Locator)topic.getSubjectIdentifiers().toArray()[0]).getReference();
    }

    protected Name createName(String name, String... themes)
    {
        ArrayList<Topic> scope = new ArrayList<Topic>();
        String mergedName = name;

        for(String theme: themes)
        {
            scope.add(createSimpleNamedTopic(theme));
            mergedName = mergedName.concat(theme);
        }

        Topic temp = createSimpleNamedTopic(name);
        return temp.createName(createSimpleNamedTopic("MultiScopedNameType"),
                        mergedName,
                        scope);
    }

    protected Occurrence createOccurrence(String name, String... themes)
    {
        ArrayList<Topic> scope = new ArrayList<Topic>();
        String mergedName = name;

        for(String theme: themes)
        {
            scope.add(createSimpleNamedTopic(theme));
            mergedName = mergedName.concat(theme);
        }

        Topic temp = createSimpleNamedTopic(name);
        return temp.createOccurrence(createSimpleNamedTopic("MultiScopedNameType"),
                        mergedName,
                        scope);
    }

    protected Variant createVariant(String name, String... themes)
    {
        ArrayList<Topic> scope = new ArrayList<Topic>();
        String mergedName = name;

        for(String theme: themes)
        {
            scope.add(createSimpleNamedTopic(theme));
            mergedName = mergedName.concat(theme);
        }

        if (scope.isEmpty()) scope.add(createSimpleNamedTopic("variant"));

        Name baseName = createName("base" + name);
        return baseName.createVariant(mergedName, scope);
    }}
