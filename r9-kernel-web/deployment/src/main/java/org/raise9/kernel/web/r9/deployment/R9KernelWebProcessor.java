package org.raise9.kernel.web.r9.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class R9KernelWebProcessor {

    private static final String FEATURE = "r9-kernel-web";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
