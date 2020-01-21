package com.gilia.metastrategies

import com.gilia.metamodel.Metamodel
import com.gilia.metamodel.entitytype.objecttype.ObjectType
import com.gilia.metamodel.relationship.Relationship
import com.gilia.metamodel.role.Role
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import spock.lang.Specification

class UmlToMetaTest extends Specification {

    void 'createModel'() {

    }

    void 'identifyClasses correctly'() {
        given:
        JSONParser parser = new JSONParser()
        String umlModelString = "{\"namespaces\":{\"ontologyIRI\":[{\"prefix\":\"crowd\",\"value\":\"http://crowd.fi.uncoma.edu.ar#\"}],\"defaultIRIs\":[{\"prefix\":\"rdf\",\"value\":\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"},{\"prefix\":\"rdfs\",\"value\":\"http://www.w3.org/2000/01/rdf-schema#\"},{\"prefix\":\"xsd\",\"value\":\"http://www.w3.org/2001/XMLSchema#\"},{\"prefix\":\"owl\",\"value\":\"http://www.w3.org/2002/07/owl#\"}],\"IRIs\":[]},\"classes\":[{\"name\":\"http://crowd.fi.uncoma.edu.ar#Class1\",\"attrs\":[],\"methods\":[],\"position\":{\"x\":433,\"y\":179}},{\"name\":\"http://crowd.fi.uncoma.edu.ar#Class2\",\"attrs\":[],\"methods\":[],\"position\":{\"x\":822,\"y\":181}},{\"name\":\"http://crowd.fi.uncoma.edu.ar#Class3\",\"attrs\":[],\"methods\":[],\"position\":{\"x\":724,\"y\":353}},{\"name\":\"http://crowd.fi.uncoma.edu.ar#Class4\",\"attrs\":[],\"methods\":[],\"position\":{\"x\":935,\"y\":350}}],\"links\":[{\"name\":\"http://crowd.fi.uncoma.edu.ar#Rel1\",\"classes\":[\"http://crowd.fi.uncoma.edu.ar#Class1\",\"http://crowd.fi.uncoma.edu.ar#Class2\"],\"multiplicity\":[\"0..*\",\"0..*\"],\"roles\":[\"http://crowd.fi.uncoma.edu.ar#role1\",\"http://crowd.fi.uncoma.edu.ar#role2\"],\"type\":\"association\"},{\"name\":\"http://crowd.fi.uncoma.edu.ar#s1\",\"parent\":\"http://crowd.fi.uncoma.edu.ar#Class2\",\"classes\":[\"http://crowd.fi.uncoma.edu.ar#Class4\",\"http://crowd.fi.uncoma.edu.ar#Class3\"],\"multiplicity\":null,\"roles\":null,\"type\":\"generalization\",\"constraint\":[\"disjoint\",\"covering\"],\"position\":{\"x\":866,\"y\":310}}],\"owllink\":[\"\"]}"
        JSONObject umlModelObject = parser.parse(umlModelString)
        JSONArray umlClasses = umlModelObject.get("classes")
        UmlToMeta model = new UmlToMeta()

        ObjectType firstExpectedObjectType = new ObjectType("http://crowd.fi.uncoma.edu.ar#Class1")
        ObjectType secondExpectedObjectType = new ObjectType("http://crowd.fi.uncoma.edu.ar#Class2")
        ObjectType thirdExpectedObjectType = new ObjectType("http://crowd.fi.uncoma.edu.ar#Class3")
        ObjectType fourthExpectedObjectType = new ObjectType("http://crowd.fi.uncoma.edu.ar#Class4")

        when:
        model.identifyClasses(umlClasses)
        Metamodel metamodel = model.getModel()

        then:
        ArrayList entities = metamodel.getEntities()
        entities.get(0) == firstExpectedObjectType
        entities.get(1) == secondExpectedObjectType
        entities.get(2) == thirdExpectedObjectType
        entities.get(3) == fourthExpectedObjectType

    }

    void 'identifyAssociations correctly'() {
        given:
        JSONParser parser = new JSONParser()
        String umlModelString = "{\"namespaces\":{\"ontologyIRI\":[{\"prefix\":\"crowd\",\"value\":\"http://crowd.fi.uncoma.edu.ar#\"}],\"defaultIRIs\":[{\"prefix\":\"rdf\",\"value\":\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"},{\"prefix\":\"rdfs\",\"value\":\"http://www.w3.org/2000/01/rdf-schema#\"},{\"prefix\":\"xsd\",\"value\":\"http://www.w3.org/2001/XMLSchema#\"},{\"prefix\":\"owl\",\"value\":\"http://www.w3.org/2002/07/owl#\"}],\"IRIs\":[]},\"classes\":[{\"name\":\"http://crowd.fi.uncoma.edu.ar#Class1\",\"attrs\":[],\"methods\":[],\"position\":{\"x\":433,\"y\":179}},{\"name\":\"http://crowd.fi.uncoma.edu.ar#Class2\",\"attrs\":[],\"methods\":[],\"position\":{\"x\":822,\"y\":181}},{\"name\":\"http://crowd.fi.uncoma.edu.ar#Class3\",\"attrs\":[],\"methods\":[],\"position\":{\"x\":724,\"y\":353}},{\"name\":\"http://crowd.fi.uncoma.edu.ar#Class4\",\"attrs\":[],\"methods\":[],\"position\":{\"x\":935,\"y\":350}}],\"links\":[{\"name\":\"http://crowd.fi.uncoma.edu.ar#Rel1\",\"classes\":[\"http://crowd.fi.uncoma.edu.ar#Class1\",\"http://crowd.fi.uncoma.edu.ar#Class2\"],\"multiplicity\":[\"0..*\",\"0..*\"],\"roles\":[\"http://crowd.fi.uncoma.edu.ar#role1\",\"http://crowd.fi.uncoma.edu.ar#role2\"],\"type\":\"association\"},{\"name\":\"http://crowd.fi.uncoma.edu.ar#s1\",\"parent\":\"http://crowd.fi.uncoma.edu.ar#Class2\",\"classes\":[\"http://crowd.fi.uncoma.edu.ar#Class4\",\"http://crowd.fi.uncoma.edu.ar#Class3\"],\"multiplicity\":null,\"roles\":null,\"type\":\"generalization\",\"constraint\":[\"disjoint\",\"covering\"],\"position\":{\"x\":866,\"y\":310}}],\"owllink\":[\"\"]}"
        JSONObject umlModelObject = parser.parse(umlModelString)
        JSONArray umlLinks = umlModelObject.get("links")
        UmlToMeta model = new UmlToMeta()


        ObjectType firstExpectedEntity = new ObjectType("http://crowd.fi.uncoma.edu.ar#Class1")
        ObjectType secondExpectedEntity = new ObjectType("http://crowd.fi.uncoma.edu.ar#Class2")
        Role firstExpectedRole = new Role("http://crowd.fi.uncoma.edu.ar#role1")
        Role secondExpectedRole = new Role("http://crowd.fi.uncoma.edu.ar#role2")

        ArrayList expectedEntities = new ArrayList()
        expectedEntities.add(firstExpectedEntity)
        expectedEntities.add(secondExpectedEntity)

        ArrayList expectedRoles = new ArrayList()
        expectedRoles.add(firstExpectedRole)
        expectedRoles.add(secondExpectedRole)

        Relationship expectedRelationship = new Relationship("http://crowd.fi.uncoma.edu.ar#Rel1", expectedEntities, expectedRoles)

        when:
        model.identifyAssociations(umlLinks)
        Metamodel metamodel = model.getModel()
        ArrayList relationships = metamodel.getRelationships()
        ArrayList entities = metamodel.getEntities()
        ArrayList roles = metamodel.getRoles()

        then:
        relationships.size() == 1
        entities.size() == 2
        roles.size() == 2
        relationships.get(0) == expectedRelationship

    }
}