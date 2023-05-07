package com.financecrm.webportal.controller;

import com.financecrm.webportal.input.team.*;
import com.financecrm.webportal.payload.team.CreateTeamPayload;
import com.financecrm.webportal.payload.team.DeleteTeamPayload;
import com.financecrm.webportal.payload.team.TeamPayload;
import com.financecrm.webportal.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
@CrossOrigin
public class TeamController {


    private final TeamService teamService;

    @PostMapping("/createTeam")
    public ResponseEntity<CreateTeamPayload> createTeam(@RequestBody CreateTeamInput createTeamInput){
        return ResponseEntity.ok(teamService.createTeam(createTeamInput));
    }

    @PostMapping("/deleteTeam")
    public ResponseEntity<DeleteTeamPayload> deleteTeam(@RequestBody DeleteTeamInput deleteTeamInput){
        return ResponseEntity.ok(teamService.deleteTeam(deleteTeamInput));
    }

    @PostMapping("/getTeamById")
    public ResponseEntity<TeamPayload> getTeamById(@RequestBody GetTeamByIdInput getTeamByIdInput){
        return ResponseEntity.ok(teamService.getTeamById(getTeamByIdInput));
    }

    @PostMapping("/getAllTeams")
    public Page<TeamPayload> getAllTeams(@RequestBody GetAllTeamsInput getAllTeamsInput){
        return teamService.getAllTeams(getAllTeamsInput);
    }

    @PostMapping("/updateTeamName")
    public ResponseEntity<TeamPayload> updateTeamName(@RequestBody UpdateTeamNameInput updateTeamNameInput){
        return ResponseEntity.ok(teamService.updateTeamName(updateTeamNameInput));
    }

    @PostMapping("/updateTeamManager")
    public ResponseEntity<TeamPayload> updateTeamManager(@RequestBody UpdateTeamManagerInput updateTeamManagerInput){
        return ResponseEntity.ok(teamService.updateTeamManager(updateTeamManagerInput));
    }


}
