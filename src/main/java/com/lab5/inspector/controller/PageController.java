package com.lab5.inspector.controller;

import com.lab5.inspector.entity.WaitingApart;
import com.lab5.inspector.service.IngInspectdataService;
import com.lab5.inspector.service.LoginDbService;
import com.lab5.inspector.service.WaitingApartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "*")
@Controller
public class PageController {

    @Autowired
    private LoginDbService loginDbService;
    @Autowired
    private WaitingApartService waitingApartService;
    @Autowired
    private IngInspectdataService ingInspectdataService;


    @GetMapping({"/", "/index", "/home"})
    public String index(Model model) {
        List<WaitingApart> pendingAparts = waitingApartService.getApartmentsByStatus("pending");
        List<WaitingApart> progressAparts = waitingApartService.getApartmentsByStatus("progress");
        List<WaitingApart> completeAparts = waitingApartService.getApartmentsByStatus("complete");


        log.info("Number of Pending Aparts: " + pendingAparts.size());
        log.info("Number of Progress Aparts: " + progressAparts.size());
        log.info("Number of Complete Aparts: " + completeAparts.size());

        // Add them to the model
        model.addAttribute("pendingAparts", pendingAparts);
        model.addAttribute("progressAparts", progressAparts);
        model.addAttribute("completeAparts", completeAparts);


        return "index";
    }

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public String report(Model model) {
        // Implementation of report method
        return "report";
    }

    @GetMapping("/login")
    public String login(Model model, @RequestParam(required = false) String error) {

        if (error != null && !error.isEmpty()) {
            model.addAttribute("loginError", error);
        }
        return "login";
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam("username") String username, @RequestParam("password") String password, RedirectAttributes redirectAttributes) {
        if (loginDbService.validateLogin(username, password)) {
            redirectAttributes.addFlashAttribute("username", username);
            log.info("Username : "+  username + " , Password : " + password);
            return new ModelAndView("redirect:/index");
        } else {

            redirectAttributes.addAttribute("error", "Login failed! Try again!");
            return new ModelAndView("redirect:/login");
        }

    }


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model) {
        // Implementation of register method
        return "register";
    }
}