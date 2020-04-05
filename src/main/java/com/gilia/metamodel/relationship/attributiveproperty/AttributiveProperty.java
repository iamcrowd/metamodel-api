package com.gilia.metamodel.relationship.attributiveproperty;

import com.gilia.metamodel.entitytype.DataType;
import com.gilia.metamodel.entitytype.objecttype.ObjectType;
import com.gilia.metamodel.relationship.Relationship;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.gilia.utils.Constants.*;

/**
 * @author Emiliano Rios Gavagnin
 */
public class AttributiveProperty extends Relationship {

    // TODO: Implement domain as Relationship or Object type
    private List<ObjectType> domain; // Actually, this should be Object type or Relationship
    private DataType range;


    public AttributiveProperty(String name) {
        super(name);
    }

    public AttributiveProperty(String name, List<ObjectType> domain, DataType range) {
        super(name, null, null);
        this.domain = domain;
        this.range = range;
    }

    public List<ObjectType> getDomain() {
        return domain;
    }

    public void setDomain(List<ObjectType> domain) {
        this.domain = domain;
    }

    public void addDomain(ObjectType newDomain) {
        this.domain.add(newDomain);
    }

    public DataType getRange() {
        return range;
    }

    public void setRange(DataType range) {
        this.range = range;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AttributiveProperty that = (AttributiveProperty) o;
        return Objects.equals(domain, that.domain) &&
                Objects.equals(range, that.range);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), domain, range);
    }

    @Override
    public String toString() {
        return "AttributiveProperty{" +
                "domain=" + domain +
                ", range=" + range +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * Creates a JSONObject with the relevant information about the Attributive Property object. The JSON format is based on the
     * Metamodel JSON Schema.
     *
     * @return JSONObject with information about the Relationship object
     */
    @Override
    public JSONObject toJSONObject() {
        JSONObject attributiveProperty = new JSONObject();
        JSONArray domainJSON = new JSONArray();

        for (Object entity : domain) {
            domainJSON.add(((ObjectType) entity).getName());
        }

        attributiveProperty.put(KEY_NAME, name);
        attributiveProperty.put(KEY_DOMAIN, domainJSON);
        attributiveProperty.put(KEY_RANGE, range.getName());
        return attributiveProperty;
    }

    public JSONObject toUML() {
        JSONObject attribute = new JSONObject();

        attribute.put(KEY_NAME, name);
        attribute.put(KEY_UML_DATATYPE, range.getName());
        return attribute;
    }


    /**
     * Returns the attributes links according to the EER schema. Note that this
     * method returns a list of attributes due to the fact that an attributive property
     * may have many domains, therefore, many links.
     *
     * @return
     */
    public List<JSONObject> toEERLinks() {
        List<JSONObject> attributes = new ArrayList<>();

        for (Object entity : domain) {
            JSONObject newAttribute = new JSONObject();
            newAttribute.put(KEY_ATTRIBUTE, this.name);
            newAttribute.put(ENTITY_STRING, ((ObjectType) entity).getName());
            newAttribute.put(KEY_TYPE, KEY_ATTRIBUTE);
            attributes.add(newAttribute);
        }
        return attributes;
    }

    /**
     * Returns the attributes objects where datatype and attribute type is described according
     * to the EER scheama. Note that this method returns a list of attributes due to the
     * fact that an attributive property may have many domains, therefore, many attributes objects.
     *    {
     *       "name": "Name",
     *       "type": "normal",
     *       "datatype": "Integer",
     *       "id": "c63",
     *       "timestamp": "",
     *       "position": {
     *         "x": 550,
     *         "y": 310
     *       }
     *     }
     * @return
     */
    public List<JSONObject> toEERAttributes() {
        List<JSONObject> attributes = new ArrayList<>();

        for (Object entity : domain) {
            JSONObject newAttribute = new JSONObject();
            newAttribute.put(KEY_NAME, this.name);
            newAttribute.put(KEY_UML_DATATYPE, this.range.getName());
            newAttribute.put(KEY_TYPE, "normal"); // TODO: This is hardcoded for now
            attributes.add(newAttribute);
        }
        return attributes;
    }
}
