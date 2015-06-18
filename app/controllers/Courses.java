package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import model.Course;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.persistence.NoResultException;
import java.util.List;

public class Courses extends Controller {

    @Transactional(readOnly = true)
    public static Result getCourse(Integer id) {
        try {
            Object course = JPA.em().find(Course.class, id);
            return ok(Json.toJson(course));
        } catch (NoResultException ex) {
            return noContent();
        }
    }

    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public static Result createCourse() {
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest();
        }

        Course medication = Json.fromJson(json, Course.class);
        JPA.em().persist(medication);
        return ok();
    }

    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public static Result updateCourse(Integer id) {
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest();
        }
        Course course = Json.fromJson(json, Course.class);
        JPA.em().merge(course);
        return ok();
    }

    @Transactional
    public static Result deleteCourse(Integer id) {
        try {
            Object course = JPA.em().find(Course.class, id);
            JPA.em().remove(course);
            return ok();
        } catch (NoResultException ex) {
            return noContent();
        }
    }

    @Transactional(readOnly = true)
    public static Result getAllCourses() {
        List<Course> courseList = JPA.em().createQuery("SELECT c FROM course c").getResultList();
        if (courseList.isEmpty()) {
            return noContent();
        }
        return ok(Json.toJson(courseList));
    }
}
