package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import model.User;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.persistence.NoResultException;
import java.util.List;


public class Users extends Controller {

    @Transactional(readOnly = true)
    public static Result getUser(Integer id) {
        try {
            Object user = JPA.em().find(User.class, id);
            return ok(Json.toJson(user));
        } catch (NoResultException ex) {
            return noContent();
        }
    }

    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public static Result createUser() {
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest();
        }

        User user = Json.fromJson(json, User.class);
        JPA.em().persist(user);
        return ok();
    }

    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public static Result updateUser(Integer id) {
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest();
        }
        User user = Json.fromJson(json, User.class);
        JPA.em().merge(user);
        return ok();
    }

    @Transactional
    public static Result deleteUser(Integer id) {
        try {
            Object user = JPA.em().find(User.class, id);
            JPA.em().remove(user);
            return ok();
        } catch (NoResultException ex) {
            return noContent();
        }
    }

    @Transactional(readOnly = true)
    public static Result getAllUsers() {
        List<User> userList = JPA.em().createQuery("SELECT u FROM pillbox_user u").getResultList();
        if (userList.isEmpty()) {
            return noContent();
        }
        return ok(Json.toJson(userList));
    }
}
