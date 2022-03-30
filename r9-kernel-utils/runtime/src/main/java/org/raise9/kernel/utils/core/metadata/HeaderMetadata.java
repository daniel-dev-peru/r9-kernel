package org.raise9.kernel.utils.core.metadata;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeaderMetadata {
  private String requestId;
  private String transactionId;
  private String applicationId;
  private String requestDate;
  private String authorization;
  private String sessionId;
}
