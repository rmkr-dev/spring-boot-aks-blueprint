package dev.rmkr.blueprint;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Contract checks for reference manifests under {@code deploy/k8s/}.
 * Keeps sample YAML honest without requiring a live cluster.
 */
class KubernetesManifestContractTest {

    private static String deployment;
    private static String serviceAccount;
    private static String service;
    private static String configMap;
    private static String hpa;
    private static String pdb;
    private static String networkPolicy;
    private static String ingress;

    @BeforeAll
    static void loadManifests() throws Exception {
        deployment = Files.readString(Path.of("deploy/k8s/deployment.yaml"));
        serviceAccount = Files.readString(Path.of("deploy/k8s/serviceaccount.yaml"));
        service = Files.readString(Path.of("deploy/k8s/service.yaml"));
        configMap = Files.readString(Path.of("deploy/k8s/configmap.yaml"));
        hpa = Files.readString(Path.of("deploy/k8s/hpa.yaml"));
        pdb = Files.readString(Path.of("deploy/k8s/pdb.yaml"));
        networkPolicy = Files.readString(Path.of("deploy/k8s/networkpolicy.yaml"));
        ingress = Files.readString(Path.of("deploy/k8s/ingress.yaml"));
    }

    @Test
    void deploymentWiresDedicatedServiceAccountWithoutTokenAutomount() {
        assertThat(deployment).contains("serviceAccountName: spring-boot-aks-blueprint");
        assertThat(deployment).contains("automountServiceAccountToken: false");
        assertThat(serviceAccount).contains("automountServiceAccountToken: false");
        assertThat(serviceAccount).contains("name: spring-boot-aks-blueprint");
    }

    @Test
    void deploymentEnforcesNonRootReadOnlyRootWithTmpEmptyDir() {
        assertThat(deployment).contains("runAsNonRoot: true");
        assertThat(deployment).contains("runAsUser: 10001");
        assertThat(deployment).contains("runAsGroup: 10001");
        assertThat(deployment).contains("fsGroup: 10001");
        assertThat(deployment).contains("readOnlyRootFilesystem: true");
        assertThat(deployment).contains("allowPrivilegeEscalation: false");
        assertThat(deployment).contains("drop: [\"ALL\"]");
        assertThat(deployment).contains("mountPath: /tmp");
        assertThat(deployment).contains("emptyDir: {}");
        assertThat(deployment).contains("seccompProfile:");
        assertThat(deployment).contains("type: RuntimeDefault");
    }

    @Test
    void deploymentProbesUseActuatorPathsOnHttpPort() {
        assertThat(deployment).contains("path: /actuator/health/liveness");
        assertThat(deployment).contains("path: /actuator/health/readiness");
        assertThat(deployment).contains("port: http");
        assertThat(deployment).contains("startupProbe:");
        assertThat(deployment).contains("readinessProbe:");
        assertThat(deployment).contains("livenessProbe:");
    }

    @Test
    void deploymentIncludesSoftTopologySpreadAndPrometheusAnnotations() {
        assertThat(deployment).contains("topologySpreadConstraints:");
        assertThat(deployment).contains("topologyKey: kubernetes.io/hostname");
        assertThat(deployment).contains("whenUnsatisfiable: ScheduleAnyway");
        assertThat(deployment).contains("prometheus.io/scrape: \"true\"");
        assertThat(deployment).contains("prometheus.io/path: \"/actuator/prometheus\"");
        assertThat(deployment).contains("prometheus.io/port: \"8080\"");
    }

    @Test
    void serviceIsClusterIpTargetingNamedHttpPort() {
        assertThat(service).contains("type: ClusterIP");
        assertThat(service).contains("targetPort: http");
        assertThat(service).contains("port: 80");
        assertThat(service).contains("appProtocol: http");
        assertThat(service).contains("app.kubernetes.io/name: spring-boot-aks-blueprint");
    }

    @Test
    void configMapExposesNonSecretRuntimeEnv() {
        assertThat(configMap).contains("SPRING_APPLICATION_NAME: spring-boot-aks-blueprint");
        assertThat(configMap).contains("SERVER_PORT: \"8080\"");
    }

    @Test
    void hpaTargetsDeploymentCpuUtilization() {
        assertThat(hpa).contains("kind: HorizontalPodAutoscaler");
        assertThat(hpa).contains("kind: Deployment");
        assertThat(hpa).contains("name: spring-boot-aks-blueprint");
        assertThat(hpa).contains("averageUtilization: 70");
        assertThat(hpa).contains("minReplicas: 1");
        assertThat(hpa).contains("maxReplicas: 3");
        assertThat(hpa).contains("stabilizationWindowSeconds: 60");
        assertThat(hpa).contains("scaleDown:");
        assertThat(hpa).contains("scaleUp:");
    }

    @Test
    void pdbRequestsMinAvailableOne() {
        assertThat(pdb).contains("kind: PodDisruptionBudget");
        assertThat(pdb).contains("minAvailable: 1");
        assertThat(pdb).contains("app.kubernetes.io/name: spring-boot-aks-blueprint");
    }

    @Test
    void networkPolicyAllowsAppPortAndDocumentsOptionalIngressController() {
        assertThat(networkPolicy).contains("kind: NetworkPolicy");
        assertThat(networkPolicy).contains("port: 8080");
        assertThat(networkPolicy).contains("app.kubernetes.io/component: api-client");
        assertThat(networkPolicy).contains("Optional: allow from Ingress controller");
    }

    @Test
    void ingressRoutesExampleHostToServicePort80() {
        assertThat(ingress).contains("kind: Ingress");
        assertThat(ingress).contains("ingressClassName: nginx");
        assertThat(ingress).contains("host: spring-boot-aks-blueprint.example.local");
        assertThat(ingress).contains("name: spring-boot-aks-blueprint");
        assertThat(ingress).contains("number: 80");
    }
}
