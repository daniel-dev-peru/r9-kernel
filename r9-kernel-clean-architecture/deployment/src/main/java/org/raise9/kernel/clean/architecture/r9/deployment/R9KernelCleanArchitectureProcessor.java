package org.raise9.kernel.clean.architecture.r9.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class R9KernelCleanArchitectureProcessor {

    private static final String FEATURE = "r9-kernel-clean-architecture";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
