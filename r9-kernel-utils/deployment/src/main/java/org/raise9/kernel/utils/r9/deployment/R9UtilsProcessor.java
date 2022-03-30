package org.raise9.kernel.utils.r9.deployment;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.AdditionalIndexedClassesBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import org.jboss.jandex.DotName;
import org.raise9.kernel.utils.core.GenerateCorrelation;
import org.raise9.kernel.utils.core.UtilsBeans;
import org.raise9.kernel.utils.core.aop.UtilRunnable;
import org.raise9.kernel.utils.core.metadata.EventMetaDataCurrent;
import org.raise9.kernel.utils.core.metadata.HttpDataCurrent;

class R9UtilsProcessor {

    private static final String FEATURE = "r9-kernel-utils-extension";
    private static final DotName COUNTED_BINDING = DotName.createSimple(Runnable.class.getName());
    private static final DotName COUNTED_INTERCEPTOR = DotName.createSimple(UtilRunnable.class.getName());

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem build() {
        return AdditionalBeanBuildItem.unremovableOf(UtilsBeans.class);
    }

    @BuildStep
    void contributeClassesToIndex(BuildProducer<AdditionalIndexedClassesBuildItem> additionalIndexedClasses) {
        additionalIndexedClasses.produce(new AdditionalIndexedClassesBuildItem(GenerateCorrelation.class.getName()));
    }

    @BuildStep
    void registerAnnotations(BuildProducer<AdditionalBeanBuildItem> additionalBeans) {
        additionalBeans.produce(AdditionalBeanBuildItem.builder()
                .addBeanClass(COUNTED_BINDING.toString())
                .addBeanClass(COUNTED_INTERCEPTOR.toString())
                .addBeanClass(EventMetaDataCurrent.class.getName())
                .addBeanClass(HttpDataCurrent.class.getName())
                //.addBeanClass(ApplicationExceptionGeneralHandler.class.getName())
                .build());
    }

}
