package com.mlntcandy.netask.ntservapiclient;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mlntcandy.netask.ntservapi.Config;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

enum APIResult {
    NetworkError,
    Success
}
class APIResponseSingular {
    public String type;
    public String target;
    public String payload;

    public APIResponseSingular(String type, String target, String payload) {
        this.type = type;
        this.target = target;
        this.payload = payload;
    }

    public void execute() {
        APIResponseExecutorRegister.execute(this);
    }
}

class APIResponse {
    public APIResult result;
    public APIResponseSingular[] actions;

    APIResponse(APIResult result, APIResponseSingular[] actions) {
        this.result = result;
        this.actions = actions;
    }

    public boolean isSuccessful() {
        return result == APIResult.Success && actions != null;
    }

    public void execute() {
        for (APIResponseSingular a : actions) {
            a.execute();
        }
    }
}

public class APIClient {
    static final String endpoint = Config.API_ENDPOINT.get();
    static final String server_token = Config.API_ENDPOINT.get();
    static final boolean supports_hex = true;
    private static final Gson gson = new Gson();

    static final HttpClient client = HttpClient
            .newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    private static CompletableFuture<APIResponse> request(String endpoint, JsonObject payload) {
        if (!supports_hex) {
            payload.add("noHexColorSupport", new JsonPrimitive(true));
        }
        payload.add("serverToken", new JsonPrimitive(server_token));

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(APIClient.endpoint + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(payload)))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply((o) -> {
                    APIResponseSingular[] res = gson.fromJson(o, APIResponseSingular[].class);
                    return new APIResponse(APIResult.Success, res);
                })
                .exceptionally((throwable -> new APIResponse(APIResult.NetworkError, null)));
    }

    private static CompletableFuture<Void> requestThenExecute(String endpoint, JsonObject payload) {
        return request(endpoint, payload).thenAccept(APIResponse::execute);
    }

    public static CompletableFuture<Void> sendMessage(String[] args, UUID sender) {
        JsonObject payload = new JsonObject();
        JsonArray argsJson = new JsonArray();
        for (String a : args) {
            argsJson.add(a);
        }
        payload.add("args", argsJson);
        payload.add("senderUuid", new JsonPrimitive(sender.toString()));
        return requestThenExecute("serverApi/cmd", payload);
    }
}