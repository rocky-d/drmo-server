package uo.rocky.httphandler;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;
import uo.rocky.LogWriter;
import uo.rocky.entity.Coordinate;

import java.io.IOException;
import java.sql.SQLException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static uo.rocky.LogWriter.LogEntryType.INFO;
import static uo.rocky.LogWriter.LogEntryType.WARNING;

/**
 * TODO
 *
 * @author Rocky Haotian Du
 */
public final class CoordinatesHttpHandler extends HttpHandlerBase {

    public static final String GET_CONTEXT = "/coordinates";
    public static final String GET_ALLOW = "GET, HEAD, POST";
    public static final String GET_CONTENT_TYPE = "application/json; charset=utf-8";

    @Override
    public void handleGETRequest(HttpExchange httpExchange) throws IOException {
        LogWriter.appendEntry(INFO, getClass().getSimpleName() + " is trying to handle the GET request.");

        try {
            String results = Coordinate.selectCoordinateWithCommentsJSONString(parseQueryParameters(httpExchange));
//            System.out.println(results);
            httpExchange.getResponseHeaders().add(ResponseHeader.CONTENT_TYPE.call(), GET_CONTENT_TYPE);
            httpExchange.sendResponseHeaders(StatusCode.OK.code(), results.getBytes(UTF_8).length);
            outputResponseBody(httpExchange.getResponseBody(), results, UTF_8);
        } catch (RuntimeException runtimeException) {
            respondBadRequest(httpExchange, runtimeException);
        } catch (SQLException sqlException) {
            respondInternalServerError(httpExchange, sqlException);
        }
    }

    @Override
    public void handleHEADRequest(HttpExchange httpExchange) throws IOException {
        LogWriter.appendEntry(INFO, getClass().getSimpleName() + " is trying to handle the HEAD request.");

        httpExchange.getResponseHeaders().add(ResponseHeader.ALLOW.call(), GET_ALLOW);
        httpExchange.sendResponseHeaders(StatusCode.OK.code(), -1);
    }

    @Override
    public void handlePOSTRequest(HttpExchange httpExchange) throws IOException {
        LogWriter.appendEntry(INFO, getClass().getSimpleName() + " is trying to handle the POST request.");

        try {
            if (Coordinate.valueOf(new JSONObject(inputRequestBody(httpExchange.getRequestBody(), UTF_8))).insertSQL()) {
                LogWriter.appendEntry(INFO, "INSERT coordinate succeeded.");
                httpExchange.sendResponseHeaders(StatusCode.OK.code(), -1);
            } else {
                LogWriter.appendEntry(WARNING, "INSERT coordinate failed.");
                respondBadRequest(httpExchange, "The data provided does not meet the definition of the relational data tables");
            }
        } catch (RuntimeException runtimeException) {
            respondBadRequest(httpExchange, runtimeException);
        } catch (SQLException sqlException) {
            respondInternalServerError(httpExchange, sqlException);
        }
    }
}