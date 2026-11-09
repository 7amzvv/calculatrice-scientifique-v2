package com.calculatrice.controller;

import com.calculatrice.service.ServiceCalculatrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calculator")
@CrossOrigin(origins = "*")
public class CalculatriceController {

    @Autowired
    private ServiceCalculatrice serviceCalculatrice;

    @PostMapping("/calculate")
    public CalculResponse calculate(@RequestBody CalculRequest request) {
        try {
            double result = serviceCalculatrice.calculer(request.getExpression());
            return new CalculResponse(result, null);
        } catch (Exception e) {
            return new CalculResponse(null, e.getMessage());
        }
    }
}