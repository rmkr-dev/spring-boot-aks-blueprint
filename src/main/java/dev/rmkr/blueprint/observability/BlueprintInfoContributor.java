package dev.rmkr.blueprint.observability;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Adds a compact {@code blueprint} section to {@code /actuator/info}.
 */
@Component
public class BlueprintInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("blueprint", Map.of(
                "stack", "spring-boot-aks",
                "java", "21",
                "purpose", "personal engineering blueprint"));
    }
}
