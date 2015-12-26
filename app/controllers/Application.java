package controllers;

import java.util.List;

import models.Widget;
import play.data.Form;
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

}
