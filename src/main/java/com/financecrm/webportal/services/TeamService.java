package com.financecrm.webportal.services;

import com.financecrm.webportal.entities.Employee;
import com.financecrm.webportal.entities.Team;
import com.financecrm.webportal.enums.JobTitle;
import com.financecrm.webportal.input.department.GetDepartmentByIdInput;
import com.financecrm.webportal.input.team.*;
import com.financecrm.webportal.payload.department.DepartmentPayload;
import com.financecrm.webportal.payload.team.CreateTeamPayload;
import com.financecrm.webportal.payload.team.DeleteTeamPayload;
import com.financecrm.webportal.payload.team.TeamPayload;
import com.financecrm.webportal.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamService {


    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private MapperService mapperService;

    @Transactional
    public CreateTeamPayload createTeam(CreateTeamInput createTeamInput) {

        Team db_team = teamRepository.findByName(createTeamInput.getName().toLowerCase());

        GetDepartmentByIdInput getDepartmentByIdInput = new GetDepartmentByIdInput(createTeamInput.getDepartmentId());
        DepartmentPayload db_department = departmentService.getDepartmentById(getDepartmentByIdInput);
        Employee employee = employeeService.findById(createTeamInput.getManagerId());

        if(db_team == null && db_department != null && employee != null && employee.getJobTitle().equals(JobTitle.MANAGER)){
            Team team = new Team();
            team.setName(createTeamInput.getName().toLowerCase());
            team.setDepartmentId(createTeamInput.getDepartmentId());
            team.setManagerId(createTeamInput.getManagerId());
            team.setDeleted(false);
            teamRepository.save(team);
            log.info("Team : "+ team.getId() + " created");

            return mapperService.convertToCreateTeamPayload(team);
        }else {
            return null;
        }
    }

    @Transactional
    public DeleteTeamPayload deleteTeam(DeleteTeamInput deleteTeamInput) {

        Team team = teamRepository.findById(deleteTeamInput.getId()).orElse(null);

        if(team != null && !team.isDeleted()){
            team.setDeleted(true);
            teamRepository.save(team);
            log.info("team deleted");
            return new DeleteTeamPayload(true);
        }else{
            log.info("team is null or is already deleted");
            return new DeleteTeamPayload(false);
        }
    }

    public TeamPayload getTeamById(GetTeamByIdInput getTeamByIdInput) {

        return mapperService.convertToTeamPayload(teamRepository.findById(getTeamByIdInput.getId()).orElse(null));
    }

    public Page<TeamPayload> getAllTeams(GetAllTeamsInput getAllTeamsInput) {
        Pageable pageable = PageRequest.of(getAllTeamsInput.getPagination().getPage(),
                getAllTeamsInput.getPagination().getSize(),
                Sort.by(Sort.Direction.valueOf(getAllTeamsInput.getPagination().getSortBy().toString()),getAllTeamsInput.getPagination().getFieldName()));

        Page<Team> teamPage = teamRepository.findByIsDeletedFalse(pageable);

        return teamPage.map(team -> mapperService.convertToTeamPayload(team));
    }

    @Transactional
    public TeamPayload updateTeamName(UpdateTeamNameInput updateTeamNameInput){

        Team db_team = teamRepository.findById(updateTeamNameInput.getId()).orElse(null);
        Team isExistTeam = teamRepository.findByName(updateTeamNameInput.getName().toLowerCase());

        if(db_team != null && isExistTeam == null && !db_team.isDeleted()){
            db_team.setName(updateTeamNameInput.getName().toLowerCase());
            teamRepository.save(db_team);
            log.info("team name changed");
            return mapperService.convertToTeamPayload(db_team);
        } else if (db_team == null || db_team.isDeleted()) {
            log.info(" team not found or deleted");
        }
        return null;
    }

    @Transactional
    public TeamPayload updateTeamManager(UpdateTeamManagerInput updateTeamManagerInput) {

        Team db_team = teamRepository.findById(updateTeamManagerInput.getTeamId()).orElse(null);
        Employee db_employee = employeeService.findById(updateTeamManagerInput.getManagerId());

        if(db_team != null && db_employee != null && !db_team.isDeleted() && !db_employee.isDeleted() && db_employee.getJobTitle().equals(JobTitle.MANAGER) ){
            db_team.setManagerId(updateTeamManagerInput.getManagerId());
            db_employee.setTeamId(updateTeamManagerInput.getTeamId());
            teamRepository.save(db_team);
            employeeService.save(db_employee);
            log.info("team manager is update");
            log.info("employee team id is update");

            return mapperService.convertToTeamPayload(db_team);
        } else if (db_team == null || db_employee == null || db_team.isDeleted() || db_employee.isDeleted() || !db_employee.getJobTitle().equals(JobTitle.MANAGER)) {
            log.info("team or employee not found");
        }
        return null;

    }
}
