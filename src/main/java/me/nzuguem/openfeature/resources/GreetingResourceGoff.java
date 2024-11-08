package me.nzuguem.openfeature.resources;

import java.util.HashMap;
import java.util.UUID;

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
import jakarta.ws.rs.core.Response;
import me.nzuguem.openfeature.configurations.OpenFeatureConfiguration;

@Path("goff")
@RunOnVirtualThread
public class GreetingResourceGoff {

    private final Client client;


    GreetingResourceGoff(OpenFeatureAPI openFeatureAPI) {

        client = openFeatureAPI.getClient(OpenFeatureConfiguration.GOFF_CLIENT_NAME);
        var clientAttrs = new HashMap<String, Value>();
        // Global EvaluationContext (Static) - Client Level
        clientAttrs.put("client", Value.objectToValue("goff"));
        var clientCtx = new ImmutableContext(clientAttrs);
        client.setEvaluationContext(clientCtx);

    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

        var requestCtx = new MutableContext(UUID.randomUUID().toString());

        // Evaluate welcome-message feature flag
        if (this.client.getBooleanValue("welcome-message", false, requestCtx)) {
            return "Hello from Goff !";
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
        var requestCtx = new MutableContext(UUID.randomUUID().toString(), requestAttrs);

        return this.client.getStringValue("greeting-locale", "Hello", requestCtx);
    }

    @GET
    @Path("rollout/percentage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response helloRolloutPercentage() {

        // Dynamic EvaluationContext (Static) - Request Level
        var requestAttrs = new HashMap<String, Value>();
        requestAttrs.put("locale", Value.objectToValue("fr"));
        var requestCtx = new MutableContext(UUID.randomUUID().toString(), requestAttrs);

        var json = this.client.getObjectValue("percentage-feature-flag", new Value(), requestCtx);

        return Response.ok(json.asStructure().asObjectMap()).build();

    }
}
