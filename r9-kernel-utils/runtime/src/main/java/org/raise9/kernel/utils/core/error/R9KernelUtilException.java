package org.raise9.kernel.utils.core.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class R9KernelUtilException extends RuntimeException{

  String message;

}
