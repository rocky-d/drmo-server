package programming3.rocky.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class HttpHandlerPrinciple implements HttpHandler {
    public final void respondInternalServerError(HttpExchange httpExchange) {
        httpExchange.getResponseHeaders().clear();
        byte[] responseBodyBytes = "Internal Server Error".getBytes(UTF_8);
        try {
            httpExchange.sendResponseHeaders(500, responseBodyBytes.length);
            writeResponseBody(httpExchange.getResponseBody(), responseBodyBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final void handleIOException(IOException ioException, HttpExchange httpExchange) {
        ioException.printStackTrace();
        respondInternalServerError(httpExchange);
    }

    public final void handleJSONException(JSONException jsonException, HttpExchange httpExchange) {
        jsonException.printStackTrace();
        respondInternalServerError(httpExchange);
    }

    public final String readRequestBodyString(InputStream requestBody) {
        return new BufferedReader(new InputStreamReader(requestBody, UTF_8)).lines().collect(Collectors.joining("\n"));
    }

    public final JSONObject readRequestBodyJSONObject(InputStream requestBody) throws JSONException {
        return new JSONObject(readRequestBodyString(requestBody));
    }

    public final void writeResponseBody(OutputStream responseBody, byte[] outputBytes) throws IOException {
        responseBody.write(outputBytes);
        responseBody.flush();
        responseBody.close();
    }

    public final void writeResponseBody(OutputStream responseBody, String outputString) throws IOException {
        writeResponseBody(responseBody, outputString.getBytes(UTF_8));
    }

    public void handleGETRequest(HttpExchange httpExchange) throws IOException {
        handleUnsupportedRequest(httpExchange);
    }

    public void handleHEADRequest(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().add("Allow", "HEAD");
        httpExchange.sendResponseHeaders(200, -1);
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

    public void handleUnsupportedRequest(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().add("Allow", "HEAD");
        byte[] responseBodyBytes = "Method Not Allowed".getBytes(UTF_8);
        httpExchange.sendResponseHeaders(405, responseBodyBytes.length);
        writeResponseBody(httpExchange.getResponseBody(), responseBodyBytes);
    }

    public void handleUnknownRequest(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().add("Allow", "HEAD");
        byte[] responseBodyBytes = "Method Not Allowed".getBytes(UTF_8);
        httpExchange.sendResponseHeaders(405, responseBodyBytes.length);
        writeResponseBody(httpExchange.getResponseBody(), responseBodyBytes);
    }

    @Override
    public final void handle(HttpExchange httpExchange) {
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
            // TODO: status code, log
            handleIOException(ioException, httpExchange);
        } catch (JSONException jsonException) {
            // TODO: status code, log
            handleJSONException(jsonException, httpExchange);
        } finally {
            httpExchange.close();
        }
    }
}