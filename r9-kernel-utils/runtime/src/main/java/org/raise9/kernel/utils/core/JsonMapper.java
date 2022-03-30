package org.raise9.kernel.utils.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.raise9.kernel.utils.core.error.R9KernelUtilException;

@Slf4j
public class JsonMapper {
  private static final ObjectMapper MP = new ObjectMapper();

  public ObjectNode createObjectNode() {
    return MP.createObjectNode();
  }
  public ArrayNode createArrayNode() {
    return MP.createArrayNode();
  }

  public JsonNode stringToJsonNode(String data) {
    try {
      return MP.readTree(data);
    } catch (JsonProcessingException e) {
      log.error("Error  convert data:{}", e.getLocalizedMessage(), e);
      return null;
    }
  }

  public JsonNode objectToJsonNode(Object data) {
    return MP.valueToTree(data);
  }


  public String objectToString(Object data) {
    try {
      return MP.writeValueAsString(data);
    } catch (JsonProcessingException e) {
      log.error("Error  convert data:{}", e.getLocalizedMessage(), e);
      return null;
    }
  }

  public <T> T convertObjectFromString(String data, Class<?> type) {
    try {
      return (T) MP.readValue(data, type);
    } catch (Exception e) {
      log.error("\nError convert data to object {} - {} \n", data, type, e);
      throw new R9KernelUtilException("Error convert data : " + e.getLocalizedMessage());
    }
  }


  public <T> T convertReferFromString(String data, TypeReference type) {
    try {
      return (T) MP.readValue(data, type);
    } catch (Exception e) {
      log.error("\nError convert data to object {} - {} \n", data, type, e);
      throw new R9KernelUtilException("Error convert data : " + e.getLocalizedMessage());
    }
  }

  public <T> T convertObject(JsonNode nodeObject, Class<?> type) {
    try {
      return (T) MP.readValue(nodeObject.toString(), type);
    } catch (Exception e) {
      log.error("Error convert data to object {} ", nodeObject.toString(), e);
      throw new R9KernelUtilException("Error convert data : " + e.getLocalizedMessage());
    }
  }


}
