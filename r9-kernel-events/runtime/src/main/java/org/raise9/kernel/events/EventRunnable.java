package org.raise9.kernel.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import org.raise9.kernel.utils.core.metadata.HeaderMetadata;

@Inherited
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface EventRunnable {

  StatusEvent eventType() default StatusEvent.CREATE;

  @Nonbinding Class<? extends HeaderMetadata> header() default HeaderMetadata.class;

  @Nonbinding Class<?> objectType() default Object.class;

}
