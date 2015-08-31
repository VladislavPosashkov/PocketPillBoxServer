package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import model.Medication;
import model.User;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.persistence.NoResultException;

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
    public static Result createMedication(String id) {
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest();
        }
        User user = (User) JPA.em().createQuery("SELECT u from pillbox_user u where u.facebookKey = '" + id +"'").getSingleResult();
        Medication medication = new Medication();
        medication.setOwner(user);
        medication.setTitle(json.get("title").textValue());
        medication.setCount(json.get("count").intValue());
        medication.setExpirationDate(json.get("expirationDate").textValue());

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
    public static Result getAllMedications(String id) {
        User user = (User) JPA.em().createQuery("select  u FROM  pillbox_user u where u.facebookKey = '" + id + "'").getSingleResult();
        return ok(Json.toJson(user.getMedications()));
    }
}
