package vttp.ssfassessment.SSFAssessment.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp.ssfassessment.SSFAssessment.services.NewsService;

@Controller
@RequestMapping("Articles")
public class NewsController {

    @Autowired
    private NewsService newsSvc;
    public String Data;
    public Integer id;
    public Integer published_on;

    @GetMapping
    public String getWeather(Model model, @RequestParam String Data) {
        newsSvc.getArticles(Data);
        model.addAttribute("id", id);
        model.addAttribute("published_on", published_on);
        return "articles";
    }
    
}
