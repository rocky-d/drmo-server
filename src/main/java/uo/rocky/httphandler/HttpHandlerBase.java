package uo.rocky.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class HttpHandlerBase implements HttpHandler {
    private static final String GET_ALLOW = "HEAD";
    private static final String GET_CONTENT_TYPE = "text/plain; charset=utf-8";

    public final void respondInternalServerError(HttpExchange httpExchange, Exception exception) {  // TODO
        httpExchange.getResponseHeaders().clear();
        httpExchange.getResponseHeaders().add(ResponseHeader.CONTENT_TYPE.call(), GET_CONTENT_TYPE);
        byte[] responseBodyBytes = StatusCode.INTERNAL_SERVER_ERROR.prompt().getBytes(UTF_8);
        try {
            httpExchange.sendResponseHeaders(StatusCode.INTERNAL_SERVER_ERROR.code(), responseBodyBytes.length);
            outputResponseBody(httpExchange.getResponseBody(), responseBodyBytes);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            throw new RuntimeException(ioException);
        }
    }

    public final void respondBadRequest(HttpExchange httpExchange, String message) throws IOException {
        httpExchange.getResponseHeaders().clear();
        httpExchange.getResponseHeaders().add(ResponseHeader.CONTENT_TYPE.call(), GET_CONTENT_TYPE);
        byte[] responseBodyBytes = (StatusCode.BAD_REQUEST.prompt() + "\n" + message).getBytes(UTF_8);
        httpExchange.sendResponseHeaders(StatusCode.BAD_REQUEST.code(), responseBodyBytes.length);
        outputResponseBody(httpExchange.getResponseBody(), responseBodyBytes);
    }

    public final void respondMethodNotAllowed(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().clear();
        httpExchange.getResponseHeaders().add(ResponseHeader.ALLOW.call(), GET_ALLOW);
        httpExchange.getResponseHeaders().add(ResponseHeader.CONTENT_TYPE.call(), GET_CONTENT_TYPE);
        byte[] responseBodyBytes = StatusCode.METHOD_NOT_ALLOWED.prompt().getBytes(UTF_8);
        httpExchange.sendResponseHeaders(StatusCode.METHOD_NOT_ALLOWED.code(), responseBodyBytes.length);
        outputResponseBody(httpExchange.getResponseBody(), responseBodyBytes);
    }

    public final void handleIOException(HttpExchange httpExchange, IOException ioException) {
        ioException.printStackTrace();
        respondInternalServerError(httpExchange, ioException);
    }

    public final String inputRequestBody(InputStream requestBody) {
        return new BufferedReader(new InputStreamReader(requestBody, UTF_8)).lines().collect(Collectors.joining("\n"));
    }

    public final void outputResponseBody(OutputStream responseBody, byte[] outputBytes) throws IOException {
        responseBody.write(outputBytes);
        responseBody.flush();
        responseBody.close();
    }

    public final void outputResponseBody(OutputStream responseBody, String outputString) throws IOException {
        outputResponseBody(responseBody, outputString.getBytes(UTF_8));
    }

    public void handleGETRequest(HttpExchange httpExchange) throws IOException {
        handleUnsupportedRequest(httpExchange);
    }

    public void handleHEADRequest(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().add(ResponseHeader.ALLOW.call(), GET_ALLOW);
        httpExchange.sendResponseHeaders(StatusCode.OK.code(), -1);
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
        respondMethodNotAllowed(httpExchange);
    }

    public void handleUnknownRequest(HttpExchange httpExchange) throws IOException {
        respondMethodNotAllowed(httpExchange);
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
            handleIOException(httpExchange, ioException);
        } finally {
            httpExchange.close();
        }
    }
}