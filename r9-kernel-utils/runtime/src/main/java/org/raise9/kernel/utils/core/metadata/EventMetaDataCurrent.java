package org.raise9.kernel.utils.core.metadata;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@javax.enterprise.context.RequestScoped
@Slf4j
public class EventMetaDataCurrent {

  private static ObjectMapper mapper = new ObjectMapper();
  private String operationId;
  private Map<String, String> mapHeaders;
  private HeaderMetadata headers;


  public void addContextHeaders(HeaderMetadata headers) {
    this.headers = headers;
  }

  public <T extends HeaderMetadata> T getHeadersFromRequest(Class<T> type) {
    if (Objects.nonNull(mapHeaders) && mapHeaders.size() > 0) {
      Map<String, String> mapHeadersTemp = objectToMap(type, mapHeaders);
      T d = mapper.convertValue(mapHeadersTemp, type);
      this.headers = d;
      return d;
    } else {
      return null;
    }
  }

  private Stream<String> listMethodsNames(Method[] declaredMethods) {
    //log.info("DeclaredMethods {}" , declaredMethods.length);
    return Arrays.stream(declaredMethods)

            .filter(p -> !p.getName().startsWith("set"))
            .filter(p -> !p.getName().startsWith("builder"))
            .filter(p -> !p.getName().startsWith("getClass"))
            .filter(p -> !p.getName().startsWith("setClass"))
            //.filter(p -> p.getName().startsWith("get"))


            .map(Method::getName)

            .map(m -> m.contains("get")?m.substring(3):m)
            .map(m -> m.substring(0,1).toLowerCase()+m.substring(1))

            //.peek(p->log.info("mn : {} " , p))
            //.map(m -> m.substring(3))
            //.map(m -> m.substring(0,1).toLowerCase()+m.substring(1))
            ;
  }

  private <T extends HeaderMetadata> Map<String, String> objectToMap(Class<T> type, Map<String, String> base) {
    Map<String, String> hm = new HashMap<>();
    //log.info("getDeclaredMethods ");
    listMethodsNames(type.getDeclaredMethods()).forEach(n -> {
      //log.info("n : {}" , n);
      hm.put(n,base.get(n));
    });
    //log.info("getSuperclass().getDeclaredMethods() ");
    listSuperMethodsNames(type.getSuperclass().getDeclaredMethods()).forEach(n -> {
      //log.info("n : {}" , n);
      hm.put(n,base.get(n));
    });
    return hm;
  }

  private Stream<String> listSuperMethodsNames(Method[] declaredMethods) {
    //log.info("DeclaredMethods {}" , declaredMethods.length);
    return Arrays.stream(declaredMethods)

            .filter(p -> !p.getName().startsWith("builder"))
            .filter(p -> !p.getName().startsWith("set"))
            .filter(p -> !p.getName().startsWith("getClass"))
            .filter(p -> !p.getName().startsWith("setClass"))
            //.filter(p -> p.getName().startsWith("get"))


            .map(Method::getName)

            .map(m -> m.substring(3))
            .map(m -> m.substring(0,1).toLowerCase()+m.substring(1))

            //.peek(p->log.info("mn s: {} " , p))
            ;
  }

}
