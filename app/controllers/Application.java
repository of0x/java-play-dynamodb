package controllers;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import models.Widget;
import play.data.Form;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.WidgetService;
import views.html.index;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result widgets() {
        List<Widget> widgets = WidgetService.getWidgets();
        return ok(views.html.widgets.render(widgets));
    }

    public static Result addWidget() {
        Form<Widget> x = Form.form(Widget.class).bindFromRequest();
        Widget widget = new Widget((String)x.data().get("name"), (String)x.data().get("description"), Integer.parseInt(x.data().get("price")));
        WidgetService.putWidget(widget);
        return redirect(routes.Application.widgets());
    }


    public static Result summary() {
        List<Widget> widgets = WidgetService.getWidgets();
        return ok(Widget.jsonlist(widgets));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result put() {
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest("Expecting Json data");
        }

        try {
            String name = json.findPath("name").textValue();
            String desc = json.findPath("description").textValue();
            Integer price = json.findPath("price").asInt();
            Widget widget = new Widget(name, desc, price);
            WidgetService.putWidget(widget);
            response().setHeader(ETAG, name);
        } catch (Exception ex) {
            return  badRequest("Failed to put");
        }
        return ok();
    }
}
