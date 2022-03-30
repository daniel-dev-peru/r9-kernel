package org.raise9.kernel.concurrent.r9.deployment;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import org.raise9.kernel.concurrent.SystemThreadExecute;

class R9KernelConcurrentProcessor {

    private static final String FEATURE = "r9-kernel-concurrent";


    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }


    @BuildStep
    void registerBeans(BuildProducer<AdditionalBeanBuildItem> additionalBeans) {
        additionalBeans.produce(AdditionalBeanBuildItem.builder()
                .addBeanClass(SystemThreadExecute.class.getName())
                .build());
    }
}
