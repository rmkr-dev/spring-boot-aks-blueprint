package dev.rmkr.blueprint.observability;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.info.Info;

import static org.assertj.core.api.Assertions.assertThat;

class BlueprintInfoContributorTest {

    @Test
    void contributesBlueprintSection() {
        Info.Builder builder = new Info.Builder();
        new BlueprintInfoContributor().contribute(builder);
        Info info = builder.build();

        assertThat(info.getDetails()).containsKey("blueprint");
        @SuppressWarnings("unchecked")
        var blueprint = (java.util.Map<String, Object>) info.getDetails().get("blueprint");
        assertThat(blueprint).containsEntry("stack", "spring-boot-aks");
        assertThat(blueprint).containsEntry("java", "21");
    }
}
