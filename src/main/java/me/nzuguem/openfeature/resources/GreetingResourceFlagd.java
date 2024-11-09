package me.nzuguem.openfeature.resources;

import java.util.HashMap;

import dev.openfeature.sdk.Client;
import dev.openfeature.sdk.ImmutableContext;
import dev.openfeature.sdk.MutableContext;
import dev.openfeature.sdk.OpenFeatureAPI;
import dev.openfeature.sdk.Value;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import me.nzuguem.openfeature.services.HelloAiService;

@Path("flagd")
@RunOnVirtualThread
public class GreetingResourceFlagd {

    private final Client client;

    private final HelloAiService helloAiService;


    GreetingResourceFlagd(OpenFeatureAPI openFeatureAPI, HelloAiService helloAiService) {

        this.helloAiService = helloAiService;

        client = openFeatureAPI.getClient();
        var clientAttrs = new HashMap<String, Value>();
        // Global EvaluationContext (Static) - Client Level
        clientAttrs.put("client", Value.objectToValue("flagd"));
        var clientCtx = new ImmutableContext(clientAttrs);
        client.setEvaluationContext(clientCtx);

    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

        // Evaluate welcome-message feature flag
        if (this.client.getBooleanValue("welcome-message", false)) {
            return "Hello from Flagd !";
        }

        return "Hello from Quarkus REST";
    }

    @GET
    @Path("ctx")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloTargeting() {

        // Dynamic EvaluationContext (Static) - Request Level
        var requestAttrs = new HashMap<String, Value>();
        requestAttrs.put("locale", Value.objectToValue("fr"));
        var requestCtx = new MutableContext(requestAttrs);

        return this.client.getStringValue("greeting-locale", "Hello", requestCtx);
    }

    @GET
    @Path("ai")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloAi() {

        if (this.client.getBooleanValue("use-ai", false)) {
            return this.helloAiService.hello("Tell me hello !");
        }

        return "Hello from Quarkus REST";
    }
}
