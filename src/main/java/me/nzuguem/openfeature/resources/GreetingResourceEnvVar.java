package me.nzuguem.openfeature.resources;

import java.util.Objects;

import dev.openfeature.sdk.Client;
import dev.openfeature.sdk.OpenFeatureAPI;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import me.nzuguem.openfeature.configurations.OpenFeatureConfiguration;

@Path("env")
@RunOnVirtualThread
public class GreetingResourceEnvVar {

    private final Client client;

    GreetingResourceEnvVar(OpenFeatureAPI openFeatureAPI) {
        client = openFeatureAPI.getClient(OpenFeatureConfiguration.ENV_VAR_CLIENT_NAME);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

        if (Objects.equals(client.getStringValue("shell", "/bin/bash"), "/bin/zsh")) {
            return "Hello from SHELL Env Var";
        }

        return "Hello from Quarkus REST";
    }
}
