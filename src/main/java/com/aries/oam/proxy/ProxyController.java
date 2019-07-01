package com.aries.oam.proxy;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProxyController {
    @RequestMapping(value = { "/login" }, method = RequestMethod.GET)
    public String login(@RequestParam String id,
                        @RequestParam(required = false, defaultValue = "get") String method,
                        Model model) {
        model.addAttribute("id", id);
        model.addAttribute("method", method);
        return "login";
    }
}
