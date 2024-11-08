package me.nzuguem.openfeature.configurations;

import java.util.Map;

import dev.openfeature.contrib.providers.gofeatureflag.exception.InvalidOptions;
import dev.openfeature.sdk.FeatureProvider;
import dev.openfeature.sdk.NoOpProvider;
import dev.openfeature.sdk.providers.memory.Flag;
import dev.openfeature.sdk.providers.memory.InMemoryProvider;
import io.quarkus.test.Mock;
import jakarta.inject.Named;

public class TestOpenFeatureConfiguration {

    @Mock
    @Named("flagd")
    public FeatureProvider flagdProvider() {
        return new NoOpProvider();
    }

    @Mock
    @Named("goff")
    public FeatureProvider goFeatureFlagProvider() throws InvalidOptions {

        var flags = Map.<String, Flag<?>>ofEntries(
            Map.entry("welcome-message", Flag.<Boolean>builder()
                        .variant("on", Boolean.TRUE)
                        .variant("off", Boolean.FALSE)
                        .defaultVariant("on")
                        .build())
        );

        return new InMemoryProvider(flags);
    }

    @Mock
    @Named("env-var")
    public FeatureProvider envVarProvider() {
        return new NoOpProvider();
    }
    
}
