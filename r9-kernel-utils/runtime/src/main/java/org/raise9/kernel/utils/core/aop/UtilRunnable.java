package org.raise9.kernel.utils.core.aop;

import java.io.Serializable;
import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import lombok.extern.slf4j.Slf4j;
import org.raise9.kernel.utils.core.annotations.Runnable;

@Slf4j
@Interceptor
@Runnable
@Priority(Interceptor.Priority.LIBRARY_BEFORE + 10)
public class UtilRunnable implements Serializable {

  @AroundInvoke
  Object interceptor(InvocationContext context) throws Exception {
    //log.info("interceptor aop");
    return context.proceed();
  }

}