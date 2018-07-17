import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import io.javalin.Context;
import io.javalin.Javalin;
import io.javalin.translator.template.JavalinMustachePlugin;

import java.sql.*;

import static io.javalin.translator.template.TemplateUtil.model;

public class TestOnly {
    public static void main(String[] args) {
        Javalin app = Javalin.start(7000);
        app.get("/", TestOnly::handle);
        app.get("/template", TestOnly::serverTemp);
        app.get("/importdata", TestOnly::inportData);

    }

    private static void handle(Context ctx) {
        try {
            Connection conn = DriverManager.getConnection(System.getenv("database_url"));
            System.out.println("Connection Successful");
            PreparedStatement stm = conn.prepareStatement("select * from test");
            ResultSet res = stm.executeQuery();
            String responseText = "";
            while (res.next()) {
                int restult1 = res.getInt("id");
                String restult2 = res.getString("name");
                responseText += " Id: " + restult1 + " Name: " + restult2;
            }
            ctx.result(responseText);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    private static void serverTemp(Context ctx) {
        try {
            MustacheFactory factory = new DefaultMustacheFactory();
            JavalinMustachePlugin.configure(factory);
            ctx.renderMustache("templateFile.mustache", model("name", "Mohamed"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private static void inportData(Context ctx) {
        Connection conn;
        try {
            conn = DriverManager.getConnection(System.getenv("database_url"));
            System.out.println("Connection is Successful");
            PreparedStatement stm = conn.prepareStatement("insert into income (date, hours, description, amount) values('2018-07-17', ?, ?, ?)");
            stm.setInt(1, 20);
            stm.setString(2, "Week2");
            stm.setInt(3, 15050);
            stm.execute();
           // conn.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
