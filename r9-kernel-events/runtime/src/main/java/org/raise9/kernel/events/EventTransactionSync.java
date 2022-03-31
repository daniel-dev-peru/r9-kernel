package org.raise9.kernel.events;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventTransactionSync<T> implements Serializable {

  private String transactionId;

  private String requestId;

  private String type;

  private StatusEvent status;

  private EventTransactionSync principal;

  private JsonNode data;

  private Class<T> cd;

  private T object;

  private String error;

  private Map<String, String> mapHeaders;

}
