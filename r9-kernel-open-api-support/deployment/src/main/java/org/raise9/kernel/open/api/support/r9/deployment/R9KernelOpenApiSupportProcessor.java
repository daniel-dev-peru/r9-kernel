package org.raise9.kernel.open.api.support.r9.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class R9KernelOpenApiSupportProcessor {

    private static final String FEATURE = "r9-kernel-open-api-support";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
