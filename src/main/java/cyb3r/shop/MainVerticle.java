package cyb3r.shop;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    JsonObject config = new JsonObject().put("url", "jdbc:h2:mem:default").put("driver_class", "");
    SQLClient client = JDBCClient.createShared(vertx, config);
    client.getConnection(res -> {
      if (res.succeeded()) {

        SQLConnection connection = res.result();

        connection.query("SELECT FIELD_13, FIELD_21 FROM SHOP.V1.DATA ORDER BY FIELD_13, FIELD_21", res2 -> {
          if (res2.succeeded()) {

            ResultSet rs = res2.result();
            rs.getColumnNames().forEach(System.out::println);
          }
        });
      } else {
        // Failed to get connection - deal with it
      }
    });
    //    SQLClient client = JDBCClient.createShared(vertx)
    vertx.createHttpServer().requestHandler(req -> {
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x!");
    }).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}
