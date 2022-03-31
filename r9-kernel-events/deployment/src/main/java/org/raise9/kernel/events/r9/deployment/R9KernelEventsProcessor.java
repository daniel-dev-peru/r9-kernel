package org.raise9.kernel.events.r9.deployment;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import org.jboss.jandex.DotName;
import org.raise9.kernel.events.EventRunnable;
import org.raise9.kernel.events.*;
import org.raise9.kernel.events.actions.*;

class R9KernelEventsProcessor {

    private static final String FEATURE = "r9-kernel-events";

    private static final DotName Event_Runnable = DotName.createSimple(EventRunnable.class.getName());
    private static final DotName Event_Runnable_Aspect = DotName.createSimple(EventRunnableAspect.class.getName());

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void registerBeans(BuildProducer<AdditionalBeanBuildItem> additionalBeans) {
        additionalBeans.produce(AdditionalBeanBuildItem.builder()
                .addBeanClass(Event_Runnable.toString())
                .addBeanClass(Event_Runnable_Aspect.toString())
                .addBeanClass(EventDataConvertImpl.class.getName())
                .addBeanClass(EventsFlowConsumer.class.getName())
                .addBeanClass(ProduceEventFLow.class.getName())
                .addBeanClass(StateHistoryEventApiImpl.class.getName())
                .addBeanClass(AddHistoryServiceImpl.class.getName())
                .addBeanClass(GetHistoryServiceImpl.class.getName())
                .addBeanClass(GetHistoryLastStateServiceImpl.class.getName())
                //.addBeanClass(ThreadExecute.class.getName())
                .build());
    }

}
