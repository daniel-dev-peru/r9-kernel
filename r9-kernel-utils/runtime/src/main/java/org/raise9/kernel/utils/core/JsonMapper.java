package org.raise9.kernel.utils.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.raise9.kernel.exceptions.ApiError;
import org.raise9.kernel.exceptions.ApiErrorDetail;
import org.raise9.kernel.exceptions.R9KernelUtilException;

@Slf4j
public class JsonMapper {
  private static final ObjectMapper MP = new ObjectMapper();
  private final String CODE_ERROR = "kU_1000";

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
      log.error("Error convert data to object {} - {}", data, type, e);
      ApiErrorDetail apiErrorDetail = new ApiErrorDetail();
      apiErrorDetail.setComponent("JsonMapper");
      apiErrorDetail.setDescription("Error convert data to object");
      ApiError apiError = new ApiError(CODE_ERROR, "Error convert data to object convertObjectFromString", "Software", Arrays.asList(apiErrorDetail));
      throw new R9KernelUtilException(apiError);
    }
  }


  public <T> T convertReferFromString(String data, TypeReference type) {
    try {
      return (T) MP.readValue(data, type);
    } catch (Exception e) {
      log.error("Error convert data to object {} - {}", data, type, e);
      ApiErrorDetail apiErrorDetail = new ApiErrorDetail();
      apiErrorDetail.setComponent("JsonMapper");
      apiErrorDetail.setDescription("Error convert data to object");
      ApiError apiError = new ApiError(CODE_ERROR, "Error convert data to object convertReferFromString", "Software", Arrays.asList(apiErrorDetail));
      throw new R9KernelUtilException(apiError);
    }
  }

  public <T> T convertObject(JsonNode nodeObject, Class<?> type) {
    try {
      return (T) MP.readValue(nodeObject.toString(), type);
    } catch (Exception e) {
      log.error("Error convert data to object {} ", nodeObject.toString(), e);
      ApiErrorDetail apiErrorDetail = new ApiErrorDetail();
      apiErrorDetail.setComponent("JsonMapper");
      apiErrorDetail.setDescription("Error convert data to object");
      ApiError apiError = new ApiError(CODE_ERROR, "Error convert data to object convertObject", "Software", Arrays.asList(apiErrorDetail));
      throw new R9KernelUtilException(apiError);
    }
  }


}
