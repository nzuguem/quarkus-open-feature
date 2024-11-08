package me.nzuguem.openfeature.resources;


import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
@RunOnVirtualThread
public class GreetingResource {


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

        return "Hello from Quarkus REST";
    }
}
