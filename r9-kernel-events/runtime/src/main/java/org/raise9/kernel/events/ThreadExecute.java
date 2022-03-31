package org.raise9.kernel.events;
/*
import io.quarkus.runtime.StartupEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Default;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Slf4j
@Singleton
public class ThreadExecute {

  @ConfigProperty(name = "ebfm.thread.min", defaultValue = "10")
  int core;
  @ConfigProperty(name = "ebfm.thread.max", defaultValue = "100")
  int max;
  @ConfigProperty(name = "ebfm.thread.time", defaultValue = "100")
  int time;

  private ExecutorService executor = null;

  void onStart(@Observes StartupEvent event) {
    ThreadPoolExecutor pool = new ThreadPoolExecutor(core, max, time, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    executor = pool;
    log.info("Started ThreadExecute core : {} max : {} time ms : {}", core, max, time);
  }

  public ExecutorService emit() {
    return executor;
  }


}
*/