package com.matijakljajic.music_catalog.web.admin;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice("com.matijakljajic.music_catalog.web.admin")
public class AdminExceptionHandler {
  @ExceptionHandler(DataIntegrityViolationException.class)
  public String onConstraint(DataIntegrityViolationException ex, Model model) {
    model.addAttribute("dbError", "Database constraint failed (possibly duplicate or FK).");
    return "admin/error";
  }
}
