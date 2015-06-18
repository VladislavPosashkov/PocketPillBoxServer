package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import model.Medication;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.persistence.NoResultException;
import java.util.List;

public class Medications extends Controller {

    @Transactional(readOnly = true)
    public static Result getMedication(Integer id) {
        try {
            Object medication = JPA.em().find(Medication.class, id);
            return ok(Json.toJson(medication));
        } catch (NoResultException ex) {
            return noContent();
        }
    }

    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public static Result createMedication() {
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest();
        }

        Medication medication = Json.fromJson(json, Medication.class);
        JPA.em().persist(medication);
        return ok();
    }

    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public static Result updateMedication(Integer id) {
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest();
        }
        Medication medication = Json.fromJson(json, Medication.class);
        JPA.em().merge(medication);
        return ok();
    }

    @Transactional
    public static Result deleteMedication(Integer id) {
        try {
            Object medication = JPA.em().find(Medication.class, id);
            JPA.em().remove(medication);
            return ok();
        } catch (NoResultException ex) {
            return noContent();
        }
    }

    @Transactional(readOnly = true)
    public static Result getAllMedications() {
        List<Medication> medicationList = JPA.em().createQuery("SELECT m FROM medication m").getResultList();
        if (medicationList.isEmpty()) {
            return noContent();
        }
        return ok(Json.toJson(medicationList));
    }
}
