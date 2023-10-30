package programming3.rocky.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class HttpHandlerPrinciple implements HttpHandler {
    public void writeResponseBody(OutputStream responseBody, byte[] outputBytes) throws IOException {
        responseBody.write(outputBytes);
        responseBody.flush();
        responseBody.close();
    }

    public void writeResponseBody(OutputStream responseBody, String outputString) throws IOException {
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
        byte[] text = "Method Not Allowed".getBytes(UTF_8);
        httpExchange.sendResponseHeaders(405, text.length);
        writeResponseBody(httpExchange.getResponseBody(), text);
    }

    public void handleUnknownRequest(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().add("Allow", "HEAD");
        byte[] text = "Method Not Allowed".getBytes(UTF_8);
        httpExchange.sendResponseHeaders(405, text.length);
        writeResponseBody(httpExchange.getResponseBody(), text);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
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
        httpExchange.close();
    }
}