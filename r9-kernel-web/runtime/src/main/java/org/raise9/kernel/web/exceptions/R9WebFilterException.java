package org.raise9.kernel.web.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.raise9.kernel.exceptions.ApiError;
import org.raise9.kernel.exceptions.R9KernelWebException;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class R9WebFilterException extends R9KernelWebException {
  ApiError apiError = new ApiError(WebKernelErrors.KERNEL_FILTER_WEB.getCode(),"Error kernel filter web ", null, null);

}
