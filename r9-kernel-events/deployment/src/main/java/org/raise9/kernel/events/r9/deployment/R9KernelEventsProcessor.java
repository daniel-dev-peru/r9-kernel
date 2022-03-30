package org.raise9.kernel.events.r9.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class R9KernelEventsProcessor {

    private static final String FEATURE = "r9-kernel-events";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
