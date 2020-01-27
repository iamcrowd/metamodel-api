package com.gilia.metamodel.constraint.cardinality;

import com.gilia.exceptions.CardinalitySyntaxException;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import java.util.Objects;

import static com.gilia.utils.Constants.*;

public class ObjectTypeCardinality extends CardinalityConstraint { // TODO: 1:1 Mapping

    private String minCardinality;
    private String maxCardinality;

    public ObjectTypeCardinality(String cardinality) throws CardinalitySyntaxException {
        super();
        if (cardinality.matches(CARDINALITY_REGEX)) {
            String[] cardinalities = cardinality.split(CARDINALITY_DIVIDER_REGEX);
            this.minCardinality = cardinalities[0]; // TODO: Check that ranges are valid
            this.maxCardinality = cardinalities[1];
        } else {
            throw new CardinalitySyntaxException(CARDINALITY_SYNTAX_ERROR + " " + StringUtils.capitalize(ROLE_STRING) + " " + name);
        }
    }

    public ObjectTypeCardinality(String name, String cardinality) throws CardinalitySyntaxException {
        super(name);
        if (cardinality.matches(CARDINALITY_REGEX)) {
            String[] cardinalities = cardinality.split(CARDINALITY_DIVIDER_REGEX);
            this.minCardinality = cardinalities[0]; // TODO: Check that ranges are valid
            this.maxCardinality = cardinalities[1];
        } else {
            throw new CardinalitySyntaxException(CARDINALITY_SYNTAX_ERROR + " " + StringUtils.capitalize(ROLE_STRING) + " " + name);
        }
    }

    public ObjectTypeCardinality(String name, String minCardinality, String maxCardinality) {
        super(name);
        this.minCardinality = minCardinality; // TODO: Check that ranges are valid
        this.maxCardinality = maxCardinality;
    }

    public String getMinCardinality() {
        return minCardinality;
    }

    public void setMinCardinality(String minCardinality) {
        this.minCardinality = minCardinality;
    }

    public String getMaxCardinality() {
        return maxCardinality;
    }

    public void setMaxCardinality(String maxCardinality) {
        this.maxCardinality = maxCardinality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ObjectTypeCardinality that = (ObjectTypeCardinality) o;
        return Objects.equals(minCardinality, that.minCardinality) &&
                Objects.equals(maxCardinality, that.maxCardinality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), minCardinality, maxCardinality);
    }

    public JSONObject toJSONObject() {
        JSONObject objectTypeCardinalityConstraint = new JSONObject();

        objectTypeCardinalityConstraint.put("name", name);
        objectTypeCardinalityConstraint.put("minimum", minCardinality);
        objectTypeCardinalityConstraint.put("maximum", maxCardinality);

        return objectTypeCardinalityConstraint;
    }
}
