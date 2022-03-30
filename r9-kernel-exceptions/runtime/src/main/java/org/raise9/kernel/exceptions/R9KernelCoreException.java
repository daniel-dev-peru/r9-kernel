package org.raise9.kernel.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class R9KernelCoreException extends R9KernelApplicationException {

  ApiError apiError = new ApiError(KernelErrors.KERNEL_CORE.getCode(),"Error kernel core ", null, null);

}
