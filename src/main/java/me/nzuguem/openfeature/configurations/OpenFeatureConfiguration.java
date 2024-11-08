package me.nzuguem.openfeature.configurations;

import java.util.HashMap;

import dev.openfeature.contrib.providers.envvar.EnvVarProvider;
import dev.openfeature.contrib.providers.envvar.EnvironmentKeyTransformer;
import dev.openfeature.contrib.providers.flagd.Config;
import dev.openfeature.contrib.providers.flagd.FlagdOptions;
import dev.openfeature.contrib.providers.flagd.FlagdProvider;
import dev.openfeature.contrib.providers.gofeatureflag.GoFeatureFlagProvider;
import dev.openfeature.contrib.providers.gofeatureflag.GoFeatureFlagProviderOptions;
import dev.openfeature.contrib.providers.gofeatureflag.exception.InvalidOptions;
import dev.openfeature.sdk.FeatureProvider;
import dev.openfeature.sdk.ImmutableContext;
import dev.openfeature.sdk.OpenFeatureAPI;
import dev.openfeature.sdk.Value;
import dev.openfeature.sdk.exceptions.OpenFeatureError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

public class OpenFeatureConfiguration {

    public static final String ENV_VAR_CLIENT_NAME = "env-var";
    public static final String GOFF_CLIENT_NAME = "goff";


    @ApplicationScoped
    public OpenFeatureAPI openFeatureAPI(
        @Named("flagd") FeatureProvider flagdProvider,
        @Named("goff") FeatureProvider goFeatureFlagProvider,
        @Named("env-var") FeatureProvider envVarProvider
    ) {

        var openFeatureAPI = OpenFeatureAPI.getInstance();

        // Use flagd as the OpenFeature provider and use default configurations
        try {
           
            // setProviderAndWait : Define the provider and when it comes to evaluating the Flag, it will wait until the Flag Management System is available
            // https://openfeature.dev/docs/reference/technologies/server/java#synchronous
            // openFeatureAPI.setProviderAndWait(new FlagdProvider());

            // setProvider : Define the provider, Define the provider and if the Flag Management System is not available, then the default flag value is returned
            // https://openfeature.dev/docs/reference/technologies/server/java#asynchronous
            openFeatureAPI.setProvider(flagdProvider);
            openFeatureAPI.setProvider(GOFF_CLIENT_NAME, goFeatureFlagProvider);
            openFeatureAPI.setProviderAndWait(ENV_VAR_CLIENT_NAME, envVarProvider);

            // Global EvaluationContext (Static) - API Level
            // https://openfeature.dev/docs/reference/concepts/evaluation-context
            var apiAttrs = new HashMap<String, Value>();
            apiAttrs.put("app", Value.objectToValue("quarkus-open-feature"));
            var apiCtx = new ImmutableContext(apiAttrs);
            openFeatureAPI.setEvaluationContext(apiCtx);

            
        } catch (OpenFeatureError e) {
            throw new RuntimeException("Failed to set OpenFeature provider", e);
        }

        return openFeatureAPI;
    }

    @Produces
    @Named("flagd")
    public FeatureProvider flagdProvider() {

        var flagdProviderOptions = FlagdOptions.builder()
                .host("localhost")
                .port(8013)
                .tls(false)
                .resolverType(Config.Resolver.RPC)
                .build();

        return  new FlagdProvider(flagdProviderOptions);
    }

    @Produces
    @Named("goff")
    public FeatureProvider goFeatureFlagProvider() throws InvalidOptions {

        var goFeatureFlagProviderOptions = GoFeatureFlagProviderOptions
                .builder()
                .endpoint("http://localhost:1031")
                .timeout(1000)
                .build();

        return  new GoFeatureFlagProvider(goFeatureFlagProviderOptions);
    }

    @Produces
    @Named("env-var")
    public FeatureProvider envVarProvider() {
        return new EnvVarProvider(EnvironmentKeyTransformer.toUpperCaseTransformer());
    }
    
}
