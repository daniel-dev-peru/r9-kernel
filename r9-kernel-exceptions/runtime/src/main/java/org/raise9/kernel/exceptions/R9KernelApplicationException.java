package org.raise9.kernel.exceptions;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class R9KernelApplicationException extends RuntimeException implements Serializable {

  ApiError apiError = new ApiError(KernelErrors.KERNEL.getCode(),"Error level 0", null, null);

}