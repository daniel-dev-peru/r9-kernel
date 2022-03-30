package org.raise9.kernel.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class R9KernelEventException extends R9KernelApplicationException {

  ApiError apiError = new ApiError(KernelErrors.KERNEL_EVENT.getCode(),"Error kernel event ", null, null);

}
