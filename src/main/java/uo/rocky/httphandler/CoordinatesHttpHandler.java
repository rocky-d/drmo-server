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
 * TOWRITE
 *
 * @author Rocky Haotian Du
 */
public final class CoordinatesHttpHandler extends HttpHandlerBase {

    public static final String GET_CONTEXT = "/coordinates";
    public static final String GET_ALLOW = "GET, HEAD, POST";
    public static final String GET_CONTENT_TYPE = "application/json; charset=utf-8";

    @Override
    public void handleGETRequest(HttpExchange httpExchange) throws IOException {
        LogWriter.append(INFO, "Try to handle the GET request in " + getClass().getSimpleName() + ".");

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
        LogWriter.append(INFO, "Try to handle the HEAD request in " + getClass().getSimpleName() + ".");

        httpExchange.getResponseHeaders().add(ResponseHeader.ALLOW.call(), GET_ALLOW);
        httpExchange.sendResponseHeaders(StatusCode.OK.code(), -1);
    }

    @Override
    public void handlePOSTRequest(HttpExchange httpExchange) throws IOException {
        LogWriter.append(INFO, "Try to handle the POST request in " + getClass().getSimpleName() + ".");

        try {
            if (Coordinate.valueOf(new JSONObject(inputRequestBody(httpExchange.getRequestBody(), UTF_8))).insertSQL()) {
                LogWriter.append(INFO, "INSERT coordinate succeeded.");
                httpExchange.sendResponseHeaders(StatusCode.OK.code(), -1);
            } else {
                LogWriter.append(WARNING, "INSERT coordinate failed.");
                respondBadRequest(httpExchange, "The data provided does not meet the definition of the relational data tables");
            }
        } catch (RuntimeException runtimeException) {
            respondBadRequest(httpExchange, runtimeException);
        } catch (SQLException sqlException) {
            respondInternalServerError(httpExchange, sqlException);
        }
    }
}