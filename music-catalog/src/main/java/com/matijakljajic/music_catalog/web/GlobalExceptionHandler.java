// src/main/java/.../web/GlobalExceptionHandler.java
package com.matijakljajic.music_catalog.web;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(DataIntegrityViolationException.class)
  public String onConstraint(DataIntegrityViolationException ex, Model model) {
    model.addAttribute("dbError", "Database constraint failed (possibly duplicate or FK).");
    return "admin/error";
  }
}
