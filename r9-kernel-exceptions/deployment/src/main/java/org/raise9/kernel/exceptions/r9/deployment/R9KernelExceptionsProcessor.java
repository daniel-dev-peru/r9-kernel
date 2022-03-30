package org.raise9.kernel.exceptions.r9.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class R9KernelExceptionsProcessor {

    private static final String FEATURE = "r9-kernel-exceptions";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
