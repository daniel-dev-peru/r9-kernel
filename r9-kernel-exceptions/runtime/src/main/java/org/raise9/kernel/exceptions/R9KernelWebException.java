package org.raise9.kernel.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class R9KernelWebException extends R9KernelApplicationException {

  ApiError apiError = new ApiError(KernelErrors.KERNEL_WEB.getCode(),"Error kernel web ", null, null);

}
