package com.gilia.controllers;

import com.gilia.builder.MetaDirector;
import com.gilia.exceptions.MetamodelException;
import com.gilia.utils.ResponseError;
import org.everit.json.schema.ValidationException;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

import static com.gilia.utils.Constants.EER_SCHEMA_PATH;
import static com.gilia.utils.Constants.EER_TO_UML;
import static com.gilia.utils.Utils.validateJSON;

/**
 * Controller of the EER_TO_UML endpoint. This controller is in charge of receiving an EER JSON and returning a UML JSON.
 */
@RestController
@CrossOrigin(origins = "*")
public class EerToUmlController {

    @PostMapping(EER_TO_UML)
    public ResponseEntity eerToUml(@RequestBody String payload) {
        MetaDirector director = new MetaDirector();
        JSONParser parser = new JSONParser();
        JSONObject response;

        try {
            validateJSON(payload, EER_SCHEMA_PATH);
            JSONObject eerModelObject = (JSONObject) parser.parse(payload);
            director.createMetamodelFromEER(eerModelObject);
            response = director.generateUML();
        } catch (FileNotFoundException e) {
            ResponseError error = new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage());
            return new ResponseEntity<>(error.toJSONObject(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JSONException | ParseException e) {
            ResponseError error = new ResponseError(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
            return new ResponseEntity<>(error.toJSONObject(), HttpStatus.BAD_REQUEST);
        } catch (ValidationException e) {
            StringBuilder stringBuilder = new StringBuilder();
            e.getCausingExceptions().stream()
                    .map(ValidationException::getMessage)
                    .forEach(stringBuilder::append);
            ResponseError error = new ResponseError(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), stringBuilder.toString());
            return new ResponseEntity<>(error.toJSONObject(), HttpStatus.BAD_REQUEST);
        } catch (MetamodelException e) {
            ResponseError error = new ResponseError(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
            return new ResponseEntity<>(error.toJSONObject(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}