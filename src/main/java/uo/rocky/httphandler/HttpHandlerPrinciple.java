package uo.rocky.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class HttpHandlerPrinciple implements HttpHandler {
    public final void respondInternalServerError(HttpExchange httpExchange) {
        httpExchange.getResponseHeaders().clear();
        byte[] responseBodyBytes = "Internal Server Error".getBytes(UTF_8);
        try {
            httpExchange.sendResponseHeaders(500, responseBodyBytes.length);
            outputResponseBody(httpExchange.getResponseBody(), responseBodyBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final void respondMethodNotAllowed(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().add("Allow", "HEAD");
        byte[] responseBodyBytes = "Method Not Allowed".getBytes(UTF_8);
        httpExchange.sendResponseHeaders(405, responseBodyBytes.length);
        outputResponseBody(httpExchange.getResponseBody(), responseBodyBytes);
    }

    public final void handleIOException(IOException ioException, HttpExchange httpExchange) {
        ioException.printStackTrace();
        respondInternalServerError(httpExchange);
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
            handleIOException(ioException, httpExchange);
        } finally {
            httpExchange.close();
        }
    }
}