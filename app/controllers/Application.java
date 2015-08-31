package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class Application extends Controller {

    public static Result main(String any){
        return ok(main.render());
    }

    public static Result landing() {
        return ok(landing.render());
    }

    public static Result pillList() {
        return ok(pillsList.render());
    }

    public static Result createPill() {
        return ok(createPill.render());
    }

    public static Result editPill() {
        return ok(pillEdit.render());
    }
}
