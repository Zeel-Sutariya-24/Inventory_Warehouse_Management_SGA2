
package InventoryWarehouse.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(
            IllegalArgumentException ex, Model model) {

        model.addAttribute("errorTitle", "Record Not Found");
        model.addAttribute(
                "errorMessage",
                ex.getMessage() != null
                        ? ex.getMessage()
                        : "The requested record could not be found."
        );

        return "error/not-found";
    }

    @ExceptionHandler({
        NoHandlerFoundException.class,
        NoResourceFoundException.class
    })
    public String handlePageNotFound(Exception ex, Model model) {

        model.addAttribute("errorTitle", "Page Not Found");
        model.addAttribute(
                "errorMessage",
                "The page or resource you requested does not exist."
        );

        return "error/not-found";
    }
}
