package uo.rocky.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import uo.rocky.LogWriter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static uo.rocky.LogWriter.LogEntryType.*;

/**
 * TOWRITE
 *
 * @author Rocky Haotian Du
 */
public abstract class HttpHandlerBase implements HttpHandler {

    public static final String GET_ALLOW = "HEAD";
    public static final String GET_CONTENT_TYPE = "text/plain; charset=utf-8";

    public final void respondInternalServerError(HttpExchange httpExchange, String message) {
        LogWriter.append(ERROR, "Respond \"Internal Server Error\"", message);

        httpExchange.getResponseHeaders().clear();
        httpExchange.getResponseHeaders().add(ResponseHeader.CONTENT_TYPE.call(), GET_CONTENT_TYPE);
        byte[] responseBodyBytes = StatusCode.INTERNAL_SERVER_ERROR.prompt().getBytes(UTF_8);
        try {
            httpExchange.sendResponseHeaders(StatusCode.INTERNAL_SERVER_ERROR.code(), responseBodyBytes.length);
            outputResponseBody(httpExchange.getResponseBody(), responseBodyBytes);
        } catch (IOException ioException) {
            LogWriter.append(ERROR, ioException.getClass().getName() + ": " + ioException.getMessage());

            ioException.printStackTrace(System.err);
            System.exit(-2304);
        }
    }

    public final void respondInternalServerError(HttpExchange httpExchange, Exception exception) {
        respondInternalServerError(httpExchange, exception.getClass().getName() + ": " + exception.getMessage());
    }

    public final void respondBadRequest(HttpExchange httpExchange, String message) throws IOException {
        LogWriter.append(WARNING, "Respond \"Bad Request\"", message);

        httpExchange.getResponseHeaders().clear();
        httpExchange.getResponseHeaders().add(ResponseHeader.CONTENT_TYPE.call(), GET_CONTENT_TYPE);
        byte[] responseBodyBytes = (StatusCode.BAD_REQUEST.prompt() + "\n" + message).getBytes(UTF_8);
        httpExchange.sendResponseHeaders(StatusCode.BAD_REQUEST.code(), responseBodyBytes.length);
        outputResponseBody(httpExchange.getResponseBody(), responseBodyBytes);
    }

    public final void respondBadRequest(HttpExchange httpExchange, RuntimeException runtimeException) throws IOException {
        respondBadRequest(httpExchange, runtimeException.getClass().getName() + ": " + runtimeException.getMessage());
    }

    public final void respondMethodNotAllowed(HttpExchange httpExchange) throws IOException {
        LogWriter.append(WARNING, "Respond \"Method Not Allowed\"");

        httpExchange.getResponseHeaders().clear();
        httpExchange.getResponseHeaders().add(ResponseHeader.ALLOW.call(), GET_ALLOW);
        httpExchange.getResponseHeaders().add(ResponseHeader.CONTENT_TYPE.call(), GET_CONTENT_TYPE);
        byte[] responseBodyBytes = StatusCode.METHOD_NOT_ALLOWED.prompt().getBytes(UTF_8);
        httpExchange.sendResponseHeaders(StatusCode.METHOD_NOT_ALLOWED.code(), responseBodyBytes.length);
        outputResponseBody(httpExchange.getResponseBody(), responseBodyBytes);
    }

    public final Map<String, String> parseQueryParameters(HttpExchange httpExchange) {
        Map<String, String> paramsFromURI = new HashMap<>();
        String uri = httpExchange.getRequestURI().toString();
        for (String param : -1 == uri.indexOf('?') ? new String[]{} : uri.substring(uri.indexOf('?') + 1).split("&")) {
            String[] keyValue = param.split("=", 2);
            if (2 == keyValue.length) {
                paramsFromURI.put(keyValue[0].toUpperCase(), keyValue[1]);
            } else {
                LogWriter.append(WARNING, "Param \"" + param + "\" is not two strings with one equal sign in between.");
            }
        }

        Map<String, String> paramsFromRequestBody = new HashMap<>();
        String requestBody = inputRequestBody(httpExchange.getRequestBody(), UTF_8);
        if (!requestBody.isEmpty()) {
            JSONObject jsonObject = new JSONObject(requestBody);
            for (String key : jsonObject.keySet()) {
                Object value = jsonObject.get(key);
                if (value instanceof String) {
                    paramsFromRequestBody.put(key.toUpperCase(), (String) value);
                } else {
                    LogWriter.append(WARNING, "Value \"" + value + "\" is not an instance of String.");
                }
            }
        }

        return paramsFromURI.isEmpty() ? paramsFromRequestBody : paramsFromURI;
    }

    public final String inputRequestBody(InputStream requestBody, Charset charset) {
        return new BufferedReader(new InputStreamReader(requestBody, charset)).lines().collect(Collectors.joining("\n"));
    }

    public final void outputResponseBody(OutputStream responseBody, byte[] outputBytes) throws IOException {
        responseBody.write(outputBytes);
        responseBody.flush();
        responseBody.close();
    }

    public final void outputResponseBody(OutputStream responseBody, String outputString, Charset charset) throws IOException {
        outputResponseBody(responseBody, outputString.getBytes(charset));
    }

    public void handleGETRequest(HttpExchange httpExchange) throws IOException {
        handleUnsupportedRequest(httpExchange);
    }

    public void handleHEADRequest(HttpExchange httpExchange) throws IOException {
        LogWriter.append(INFO, "Try to handle the HEAD request in " + getClass().getSimpleName() + ".");

        httpExchange.getResponseHeaders().add(ResponseHeader.ALLOW.call(), GET_ALLOW);
        httpExchange.sendResponseHeaders(StatusCode.NO_CONTENT.code(), -1);
    }

    public void handlePOSTRequest(HttpExchange httpExchange) throws IOException {
        handleUnsupportedRequest(httpExchange);
    }

    public void handlePUTRequest(HttpExchange httpExchange) throws IOException {
        handleUnsupportedRequest(httpExchange);
    }

    public void handleDELETERequest(HttpExchange httpExchange) throws IOException {
        handleUnsupportedRequest(httpExchange);
    }

    public void handleCONNECTRequest(HttpExchange httpExchange) throws IOException {
        handleUnsupportedRequest(httpExchange);
    }

    public void handleOPTIONSRequest(HttpExchange httpExchange) throws IOException {
        handleUnsupportedRequest(httpExchange);
    }

    public void handleTRACERequest(HttpExchange httpExchange) throws IOException {
        handleUnsupportedRequest(httpExchange);
    }

    public void handlePATCHRequest(HttpExchange httpExchange) throws IOException {
        handleUnsupportedRequest(httpExchange);
    }

    public void handleUnknownRequest(HttpExchange httpExchange) throws IOException {
        handleUnsupportedRequest(httpExchange);
    }

    public final void handleUnsupportedRequest(HttpExchange httpExchange) throws IOException {
        respondMethodNotAllowed(httpExchange);
    }

    @Override
    public final void handle(HttpExchange httpExchange) {
        LogWriter.append(INFO, "Caught a \"" + httpExchange.getRequestMethod() + "\" request.");

        try {
            switch (httpExchange.getRequestMethod().toUpperCase()) {
                case "GET":
                    handleGETRequest(httpExchange);
                    break;
                case "HEAD":
                    handleHEADRequest(httpExchange);
                    break;
                case "POST":
                    handlePOSTRequest(httpExchange);
                    break;
                case "PUT":
                    handlePUTRequest(httpExchange);
                    break;
                case "DELETE":
                    handleDELETERequest(httpExchange);
                    break;
                case "CONNECT":
                    handleCONNECTRequest(httpExchange);
                    break;
                case "OPTIONS":
                    handleOPTIONSRequest(httpExchange);
                    break;
                case "TRACE":
                    handleTRACERequest(httpExchange);
                    break;
                case "PATCH":
                    handlePATCHRequest(httpExchange);
                    break;
                default:
                    handleUnknownRequest(httpExchange);
                    break;
            }
        } catch (IOException ioException) {
            handleIOException(httpExchange, ioException);
        } catch (Exception unknownException) {
            handleUnknownException(httpExchange, unknownException);
        } finally {
            httpExchange.close();
        }
    }

    public final void handleIOException(HttpExchange httpExchange, IOException ioException) {
        respondInternalServerError(httpExchange, ioException);
    }

    public final void handleUnknownException(HttpExchange httpExchange, Exception unknownException) {
        respondInternalServerError(httpExchange, unknownException);
    }
}