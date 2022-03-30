package org.raise9.kernel.open.api.r9.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class R9KernelOpenApiProcessor {

    private static final String FEATURE = "r9-kernel-open-api";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
