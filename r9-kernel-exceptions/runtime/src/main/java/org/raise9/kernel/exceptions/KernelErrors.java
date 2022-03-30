package org.raise9.kernel.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
enum KernelErrors {

  KERNEL("0000"),KERNEL_UTIL("0001"),KERNEL_CORE("0002"),KERNEL_EVENT("0003"),KERNEL_WEB("0004");

  String code;


}

