package org.raise9.kernel.web.r9.deployment;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.AdditionalIndexedClassesBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import org.raise9.kernel.web.filter.LogFilter;
import org.raise9.kernel.web.filter.ValidateAndAddRequestIdFilter;
import org.raise9.kernel.web.filter.ValidateAndAddTransactionIdFilter;
import org.raise9.kernel.web.filter.WebRequestHeadersFilter;

class R9KernelWebProcessor {

  private static final String FEATURE = "r9-kernel-web";

  @BuildStep
  FeatureBuildItem feature() {
    return new FeatureBuildItem(FEATURE);
  }

  @BuildStep
  void registerAnnotations(BuildProducer<AdditionalBeanBuildItem> additionalBeans) {
    additionalBeans.produce(AdditionalBeanBuildItem.builder()
            .addBeanClass(WebRequestHeadersFilter.class.getName())
            .build());
  }

  @BuildStep
  void contributeClassesToIndex(BuildProducer<AdditionalIndexedClassesBuildItem> additionalIndexedClasses) {
    additionalIndexedClasses.produce(new AdditionalIndexedClassesBuildItem(ValidateAndAddRequestIdFilter.class.getName(),
            ValidateAndAddTransactionIdFilter.class.getName(),
            WebRequestHeadersFilter.class.getName(),
            LogFilter.class.getName()));
  }

}
