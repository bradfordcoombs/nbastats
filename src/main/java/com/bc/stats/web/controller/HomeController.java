package com.bc.stats.web.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bc.stats.service.ScraperService;

@RestController
@RequestMapping("/hello")
public class HomeController {

   @Resource
   ScraperService scraperService;

   @RequestMapping("/{name}")
   public String hello(@PathVariable String name) {
      scraperService.scrapeNbaDotComPlayerProfiles();
      return "Hello World";
   }
}
