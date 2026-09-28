package com.emmaong.rostermanager.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emmaong.rostermanager.backend.dto.response.RosterResponse;
import com.emmaong.rostermanager.backend.service.RosterService;
import com.emmaong.rostermanager.models.Roster;

@RestController
@RequestMapping("/api/rosters")
public class RosterController {

    private final RosterService rosterService;
    
    public RosterController(RosterService rosterService) {
    	this.rosterService = rosterService;
    }

    @PostMapping("/generate")
    public RosterResponse generateRoster() {
        return rosterService.generateRoster();
    }
}
