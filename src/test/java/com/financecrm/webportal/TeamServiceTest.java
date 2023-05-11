package com.financecrm.webportal;

import com.financecrm.webportal.entities.Department;
import com.financecrm.webportal.entities.Team;
import com.financecrm.webportal.entities.User;
import com.financecrm.webportal.enums.SortBy;
import com.financecrm.webportal.enums.UserStatus;
import com.financecrm.webportal.input.PaginationInput;
import com.financecrm.webportal.input.team.*;
import com.financecrm.webportal.payload.team.CreateTeamPayload;
import com.financecrm.webportal.payload.team.DeleteTeamPayload;
import com.financecrm.webportal.payload.team.TeamPayload;
import com.financecrm.webportal.repositories.DepartmentRepository;
import com.financecrm.webportal.repositories.TeamRepository;
import com.financecrm.webportal.services.CustomUserService;
import com.financecrm.webportal.services.MapperService;
import com.financecrm.webportal.services.TeamService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private CustomUserService customUserService;
    @Mock
    private MapperService mapperService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("should Return CreateTeamPayload when Team Is Null And Department IsExist And IsDeleted False From CreateTeamInput")
    @Tag("createTeam")
    @Test
    void shouldReturnCreateTeamPayload_whenTeamIsNullAndDepartmentIsExistAndIsDeletedFalseFromCreateTeamInput(){
        Team savedTeam = new Team("test_id","test_name","test_departmentId","test_managerId",false);
        Department department = new Department("test_departmentId","test_name","test_managerId",false);
        CreateTeamPayload result = new CreateTeamPayload("test_id","test_name","test_departmentId","test_managerId");
        CreateTeamInput createTeamInput = new CreateTeamInput("test_name","test_departmentId","test_managerId");

        Mockito.when(teamRepository.findByName(createTeamInput.getName())).thenReturn(null);
        Mockito.when(departmentRepository.findById(createTeamInput.getDepartmentId())).thenReturn(Optional.of(department));
        Mockito.when(teamRepository.save(ArgumentMatchers.any(Team.class))).thenReturn(savedTeam);
        Mockito.when(mapperService.convertToCreateTeamPayload(savedTeam)).thenReturn(result);

        CreateTeamPayload expectedResult = teamService.createTeam(createTeamInput);

        assertEquals(expectedResult.getDepartmentId(),result.getDepartmentId());
        assertEquals(expectedResult.getName(),result.getName());

        Mockito.verify(teamRepository).findByName(createTeamInput.getName());
        Mockito.verify(departmentRepository).findById(createTeamInput.getDepartmentId());
        Mockito.verify(teamRepository).save(ArgumentMatchers.any(Team.class));
    }

    @DisplayName("should Return Null when Team Is Exist From CreateTeamInput")
    @Tag("createTeam")
    @Test
    void shouldReturnNull_whenTeamIsExistFromCreateTeamInput(){
        Team savedTeam = new Team("test_id","test_name","test_departmentId","test_managerId",false);
        Department department = new Department("test_departmentId","test_name","test_managerId",false);
        CreateTeamInput createTeamInput = new CreateTeamInput("test_name","test_departmentId","test_managerId");

        Mockito.when(teamRepository.findByName(createTeamInput.getName())).thenReturn(savedTeam);
        Mockito.when(departmentRepository.findById(createTeamInput.getDepartmentId())).thenReturn(Optional.of(department));

        CreateTeamPayload expectedResult = teamService.createTeam(createTeamInput);

        assertNull(expectedResult);

        Mockito.verify(teamRepository).findByName(createTeamInput.getName());
        Mockito.verify(departmentRepository).findById(createTeamInput.getDepartmentId());
    }

    @DisplayName("should Return Null when Team Is Null And Department Is Null From CreateTeamInput")
    @Tag("createTeam")
    @Test
    void shouldReturnNull_whenTeamIsNullAndDepartmentIsNullFromCreateTeamInput(){
        CreateTeamInput createTeamInput = new CreateTeamInput("test_name","test_departmentId","test_managerId");

        Mockito.when(teamRepository.findByName(createTeamInput.getName())).thenReturn(null);
        Mockito.when(departmentRepository.findById(createTeamInput.getDepartmentId())).thenReturn(Optional.empty());

        CreateTeamPayload expectedResult = teamService.createTeam(createTeamInput);

        assertNull(expectedResult);

        Mockito.verify(teamRepository).findByName(createTeamInput.getName());
        Mockito.verify(departmentRepository).findById(createTeamInput.getDepartmentId());
    }

    @DisplayName("should Return Null when Team Is Null And Department Is Exist And Is Deleted True From CreateTeamInput")
    @Tag("createTeam")
    @Test
    void shouldReturnNull_whenTeamIsNullAndDepartmentIsExistAndIsDeletedTrueFromCreateTeamInput(){
        CreateTeamInput createTeamInput = new CreateTeamInput("test_name","test_departmentId","test_managerId");
        Department department = new Department("test_departmentId","test_name","test_managerId",true);

        Mockito.when(teamRepository.findByName(createTeamInput.getName())).thenReturn(null);
        Mockito.when(departmentRepository.findById(createTeamInput.getDepartmentId())).thenReturn(Optional.of(department));

        CreateTeamPayload expectedResult = teamService.createTeam(createTeamInput);

        assertNull(expectedResult);

        Mockito.verify(teamRepository).findByName(createTeamInput.getName());
        Mockito.verify(departmentRepository).findById(createTeamInput.getDepartmentId());
    }

    @DisplayName("should Return DeleteTeamPayload Status True when Team Is Exist And IsDeleted False From DeleteTeamInput")
    @Tag("deleteTeam")
    @Test
    void shouldReturnDeleteTeamPayloadStatusTrue_whenTeamIsExistAndIsDeletedFalseFromDeleteTeamInput(){
        DeleteTeamInput deleteTeamInput = new DeleteTeamInput("test_id");
        Team savedTeam = new Team("test_id","test_name","test_departmentId","test_managerId",false);

        Mockito.when(teamRepository.findById(deleteTeamInput.getId())).thenReturn(Optional.of(savedTeam));
        Mockito.when(teamRepository.save(savedTeam)).thenReturn(null);

        DeleteTeamPayload expectedResult = teamService.deleteTeam(deleteTeamInput);

        assertTrue(expectedResult.isStatus());

        Mockito.verify(teamRepository).findById(deleteTeamInput.getId());
        Mockito.verify(teamRepository).save(savedTeam);
    }

    @DisplayName("should Return DeleteTeamPayload Status False when Team Does Not Exist From DeleteTeamInput")
    @Tag("deleteTeam")
    @Test
    void shouldReturnDeleteTeamPayloadStatusFalse_whenTeamDoesNotExistFromDeleteTeamInput(){
        DeleteTeamInput deleteTeamInput = new DeleteTeamInput("test_id");

        Mockito.when(teamRepository.findById(deleteTeamInput.getId())).thenReturn(Optional.empty());

        DeleteTeamPayload expectedResult = teamService.deleteTeam(deleteTeamInput);

        assertFalse(expectedResult.isStatus());

        Mockito.verify(teamRepository).findById(deleteTeamInput.getId());
    }

    @DisplayName("should Return DeleteTeamPayload Status False when Team IsExist And IsDeleted True From DeleteTeamInput")
    @Tag("deleteTeam")
    @Test
    void shouldReturnDeleteTeamPayloadStatusFalse_whenTeamIsExistAndIsDeletedTrueFromDeleteTeamInput(){
        DeleteTeamInput deleteTeamInput = new DeleteTeamInput("test_id");
        Team savedTeam = new Team("test_id","test_name","test_departmentId","test_managerId",true);
        Mockito.when(teamRepository.findById(deleteTeamInput.getId())).thenReturn(Optional.of(savedTeam));

        DeleteTeamPayload expectedResult = teamService.deleteTeam(deleteTeamInput);

        assertFalse(expectedResult.isStatus());

        Mockito.verify(teamRepository).findById(deleteTeamInput.getId());
    }

    @DisplayName("should Return TeamPayload when Team Is Exist From GetTeamByIdInput")
    @Tag("getTeamById")
    @Test
    void shouldReturnTeamPayload_whenTeamIsExistFromGetTeamByIdInput(){
        GetTeamByIdInput getTeamByIdInput = new GetTeamByIdInput("test_id");
        Team team = new Team("test_id","test_name","test_departmentId","test_managerId",false);
        TeamPayload result = new TeamPayload("test_id","test_name","test_departmentId","test_managerId",false);


        Mockito.when(teamRepository.findById(getTeamByIdInput.getId())).thenReturn(Optional.of(team));
        Mockito.when(mapperService.convertToTeamPayload(team)).thenReturn(result);

        TeamPayload expectedResult = teamService.getTeamById(getTeamByIdInput);

        assertEquals(result.getId(),expectedResult.getId());

        Mockito.verify(teamRepository).findById(getTeamByIdInput.getId());
        Mockito.verify(mapperService).convertToTeamPayload(team);
    }
    @DisplayName("should Return Null when Team Does Not Exist From GetTeamByIdInput")
    @Tag("getTeamById")
    @Test
    void shouldReturnNull_whenTeamDoesNotExistFromGetTeamByIdInput(){
        GetTeamByIdInput getTeamByIdInput = new GetTeamByIdInput("test_id");

        Mockito.when(teamRepository.findById(getTeamByIdInput.getId())).thenReturn(Optional.empty());
        Mockito.when(mapperService.convertToTeamPayload(null)).thenReturn(null);

        TeamPayload expectedResult = teamService.getTeamById(getTeamByIdInput);

        assertNull(expectedResult);

        Mockito.verify(teamRepository).findById(getTeamByIdInput.getId());
        Mockito.verify(mapperService).convertToTeamPayload(null);
    }

    @DisplayName("should Return Page TeamPayload when GetAllTeamsInput Is Exist")
    @Tag("getAllTeams")
    @Test
    void shouldReturnPageTeamPayload_whenGetAllTeamsInputIsExist(){
        GetAllTeamsInput getAllTeamsInput = new GetAllTeamsInput(new PaginationInput(0,10,"name", SortBy.ASC));
        Team team = new Team("test_id","test_name","test_departmentId","test_managerId",false);
        TeamPayload result = new TeamPayload("test_id","test_name","test_departmentId","test_managerId",false);
        List<Team> teamList = new ArrayList<>();
        teamList.add(team);
        Page<Team> teamPayloadPage = new PageImpl<>(teamList);
        Pageable pageable = PageRequest.of(getAllTeamsInput.getPagination().getPage(),
                getAllTeamsInput.getPagination().getSize(),
                Sort.by(Sort.Direction.valueOf(getAllTeamsInput.getPagination().getSortBy().toString()), getAllTeamsInput.getPagination().getFieldName()));

        Mockito.when(teamRepository.findByIsDeletedFalse(pageable)).thenReturn(teamPayloadPage);
        Mockito.when(mapperService.convertToTeamPayload(team)).thenReturn(result);

        Page<TeamPayload> expectedResult = teamService.getAllTeams(getAllTeamsInput);

        assertEquals(1,expectedResult.getContent().size());

        Mockito.verify(teamRepository).findByIsDeletedFalse(pageable);
        Mockito.verify(mapperService).convertToTeamPayload(team);
    }

    @DisplayName("should Return TeamPayload when Team Is Exist And TeamName Does Not Exist And Team IsDeleted False From UpdateTeamNameInput")
    @Tag("updateTeamName")
    @Test
    void shouldReturnTeamPayload_whenTeamIsExistAndTeamNameDoesNotExistAndTeamIsDeletedFalseFromUpdateTeamNameInput(){
        UpdateTeamNameInput updateTeamNameInput = new UpdateTeamNameInput("test_id","test_updatename");
        Team db_team = new Team("test_id","test_name","test_departmentId","test_managerId",false);
        Team updateTeam = new Team("test_id","test_updatename","test_departmentId","test_managerId",false);
        TeamPayload result = new TeamPayload("test_id","test_updatename","test_departmentId","test_managerId",false);

        Mockito.when(teamRepository.findById(updateTeamNameInput.getId())).thenReturn(Optional.of(db_team));
        Mockito.when(teamRepository.findByName(updateTeamNameInput.getName())).thenReturn(null);
        Mockito.when(teamRepository.save(db_team)).thenReturn(updateTeam);
        Mockito.when(mapperService.convertToTeamPayload(updateTeam)).thenReturn(result);

        TeamPayload expectedResult = teamService.updateTeamName(updateTeamNameInput);

        assertEquals(expectedResult,result);

        Mockito.verify(teamRepository).findById(updateTeam.getId());
        Mockito.verify(teamRepository).findByName(updateTeamNameInput.getName());
        Mockito.verify(teamRepository).save(db_team);
        Mockito.verify(mapperService).convertToTeamPayload(updateTeam);
    }

    @DisplayName("should Return Null when Team Does Not Exist From UpdateTeamNameInput")
    @Tag("updateTeamName")
    @Test
    void shouldReturnNull_whenTeamDoesNotExistFromUpdateTeamNameInput(){
        UpdateTeamNameInput updateTeamNameInput = new UpdateTeamNameInput("test_id","test_updatename");

        Mockito.when(teamRepository.findById(updateTeamNameInput.getId())).thenReturn(Optional.empty());
        Mockito.when(teamRepository.findByName(updateTeamNameInput.getName())).thenReturn(ArgumentMatchers.any(Team.class));

        TeamPayload expectedResult = teamService.updateTeamName(updateTeamNameInput);

        assertNull(expectedResult);

        Mockito.verify(teamRepository).findById(updateTeamNameInput.getId());
        Mockito.verify(teamRepository).findByName(updateTeamNameInput.getName());
    }

    @DisplayName("should Return Null when Team IsExist And IsDeleted True FromUpdateTeamNameInput")
    @Tag("updateTeamName")
    @Test
    void shouldReturnNull_whenTeamIsExistAndIsDeletedTrueFromUpdateTeamNameInput(){
        UpdateTeamNameInput updateTeamNameInput = new UpdateTeamNameInput("test_id","test_updatename");
        Team db_team = new Team("test_id","test_name","test_departmentId","test_managerId",true);

        Mockito.when(teamRepository.findById(updateTeamNameInput.getId())).thenReturn(Optional.of(db_team));
        Mockito.when(teamRepository.findByName(updateTeamNameInput.getName())).thenReturn(null);

        TeamPayload expectedResult = teamService.updateTeamName(updateTeamNameInput);

        assertNull(expectedResult);

        Mockito.verify(teamRepository).findById(updateTeamNameInput.getId());
        Mockito.verify(teamRepository).findByName(updateTeamNameInput.getName());
    }

    @DisplayName("should Return Null when Team IsExist And IsDeleted False And Team Name IsExist From UpdateTeamNameInput")
    @Tag("updateTeamName")
    @Test
    void shouldReturnNull_whenTeamIsExistAndIsDeletedFalseAndTeamNameIsExistFromUpdateTeamNameInput(){
        UpdateTeamNameInput updateTeamNameInput = new UpdateTeamNameInput("test_id","test_updatename");
        Team db_team = new Team("test_id","test_name","test_departmentId","test_managerId",false);
        Team isExistTeam = new Team("test_id","test_updatename","test_departmentId","test_managerId",false);

        Mockito.when(teamRepository.findById(updateTeamNameInput.getId())).thenReturn(Optional.of(db_team));
        Mockito.when(teamRepository.findByName(updateTeamNameInput.getName())).thenReturn(isExistTeam);

        TeamPayload expectedResult = teamService.updateTeamName(updateTeamNameInput);

        assertNull(expectedResult);

        Mockito.verify(teamRepository).findById(updateTeamNameInput.getId());
        Mockito.verify(teamRepository).findByName(updateTeamNameInput.getName());
    }

    @DisplayName("should Return TeamPayload when Team IsExist And User IsExist And Team IsDeleted False And User IsDeleted False From UpdateTeamManagerInput")
    @Tag("updateTeamManager")
    @Test
    void shouldReturnTeamPayload_whenTeamIsExistAndUserIsExistAndTeamIsDeletedFalseAndUserIsDeletedFalseFromUpdateTeamManagerInput(){
        UpdateTeamManagerInput updateTeamManagerInput = new UpdateTeamManagerInput("test_new_managerId","test_teamId");
        Team db_team = new Team("test_teamId","test_name","test_departmentId","test_managerId",false);
        Team updateTeam = new Team("test_teamId","test_name","test_departmentId","test_new_managerId",false);
        User user = new User("test_userId","test_email","test_password","test_name","test_surname","test_phone","test_representativeEmployeeId", UserStatus.TEST,false,new Date(),new Date());
        TeamPayload result = new TeamPayload("test_teamId","test_name","test_departmentId","test_new_managerId",false);

        Mockito.when(teamRepository.findById(updateTeamManagerInput.getTeamId())).thenReturn(Optional.of(db_team));
        Mockito.when(customUserService.findByUserId(updateTeamManagerInput.getManagerId())).thenReturn(user);
        Mockito.when(teamRepository.save(db_team)).thenReturn(null);
        Mockito.when(mapperService.convertToTeamPayload(updateTeam)).thenReturn(result);

        TeamPayload expectedResult = teamService.updateTeamManager(updateTeamManagerInput);

        assertEquals(expectedResult,result);

        Mockito.verify(teamRepository).findById(updateTeamManagerInput.getTeamId());
        Mockito.verify(customUserService).findByUserId(updateTeamManagerInput.getManagerId());
        Mockito.verify(teamRepository).save(db_team);
        Mockito.verify(mapperService).convertToTeamPayload(updateTeam);
    }

    @DisplayName("should Return Null when Team Does Not Exist And User IsExist And IsDeleted False From UpdateTeamManagerInput")
    @Tag("updateTeamManager")
    @Test
    void shouldReturnNull_whenTeamDoesNotExistAndUserIsExistAndIsDeletedFalseFromUpdateTeamManagerInput(){
        UpdateTeamManagerInput updateTeamManagerInput = new UpdateTeamManagerInput("test_new_managerId","test_teamId");
        User user = new User("test_userId","test_email","test_password","test_name","test_surname","test_phone","test_representativeEmployeeId", UserStatus.TEST,false,new Date(),new Date());

        Mockito.when(teamRepository.findById(updateTeamManagerInput.getTeamId())).thenReturn(Optional.empty());
        Mockito.when(customUserService.findByUserId(updateTeamManagerInput.getManagerId())).thenReturn(user);

        TeamPayload expectedResult = teamService.updateTeamManager(updateTeamManagerInput);

        assertNull(expectedResult);

        Mockito.verify(teamRepository).findById(updateTeamManagerInput.getTeamId());
        Mockito.verify(customUserService).findByUserId(updateTeamManagerInput.getManagerId());

    }

    @DisplayName("should Return Null when Team IsExist And IsDeleted False And User Does Not Exist From UpdateTeamManagerInput")
    @Tag("updateTeamManager")
    @Test
    void shouldReturnNull_whenTeamIsExistAndIsDeletedFalseAndUserDoesNotExistFromUpdateTeamManagerInput(){
        UpdateTeamManagerInput updateTeamManagerInput = new UpdateTeamManagerInput("test_new_managerId","test_teamId");
        Team db_team = new Team("test_teamId","test_name","test_departmentId","test_managerId",false);

        Mockito.when(teamRepository.findById(updateTeamManagerInput.getTeamId())).thenReturn(Optional.of(db_team));
        Mockito.when(customUserService.findByUserId(updateTeamManagerInput.getManagerId())).thenReturn(null);

        TeamPayload expectedResult = teamService.updateTeamManager(updateTeamManagerInput);

        assertNull(expectedResult);

        Mockito.verify(teamRepository).findById(updateTeamManagerInput.getTeamId());
        Mockito.verify(customUserService).findByUserId(updateTeamManagerInput.getManagerId());

    }

    @DisplayName("should Return Null when Team IsExist And IsDeleted True And User IsExist And IsDeleted False From UpdateTeamManagerInput")
    @Tag("updateTeamManager")
    @Test
    void shouldReturnNull_whenTeamIsExistAndIsDeletedTrueAndUserIsExistAndIsDeletedFalseTeamFromUpdateTeamManagerInput(){
        UpdateTeamManagerInput updateTeamManagerInput = new UpdateTeamManagerInput("test_new_managerId","test_teamId");
        Team db_team = new Team("test_teamId","test_name","test_departmentId","test_managerId",true);
        User user = new User("test_userId","test_email","test_password","test_name","test_surname","test_phone","test_representativeEmployeeId", UserStatus.TEST,false,new Date(),new Date());

        Mockito.when(teamRepository.findById(updateTeamManagerInput.getTeamId())).thenReturn(Optional.of(db_team));
        Mockito.when(customUserService.findByUserId(updateTeamManagerInput.getManagerId())).thenReturn(user);

        TeamPayload expectedResult = teamService.updateTeamManager(updateTeamManagerInput);

        assertNull(expectedResult);

        Mockito.verify(teamRepository).findById(updateTeamManagerInput.getTeamId());
        Mockito.verify(customUserService).findByUserId(updateTeamManagerInput.getManagerId());

    }

    @DisplayName("should Return Null when Team IsExist And IsDeleted False And User IsExist And IsDeleted True From UpdateTeamManagerInput")
    @Tag("updateTeamManager")
    @Test
    void shouldReturnNull_whenTeamIsExistAndIsDeletedFalseAndUserIsExistAndIsDeletedTrueFromUpdateTeamManagerInput(){
        UpdateTeamManagerInput updateTeamManagerInput = new UpdateTeamManagerInput("test_new_managerId","test_teamId");
        Team db_team = new Team("test_teamId","test_name","test_departmentId","test_managerId",false);
        User user = new User("test_userId","test_email","test_password","test_name","test_surname","test_phone","test_representativeEmployeeId", UserStatus.TEST,true,new Date(),new Date());

        Mockito.when(teamRepository.findById(updateTeamManagerInput.getTeamId())).thenReturn(Optional.of(db_team));
        Mockito.when(customUserService.findByUserId(updateTeamManagerInput.getManagerId())).thenReturn(user);

        TeamPayload expectedResult = teamService.updateTeamManager(updateTeamManagerInput);

        assertNull(expectedResult);

        Mockito.verify(teamRepository).findById(updateTeamManagerInput.getTeamId());
        Mockito.verify(customUserService).findByUserId(updateTeamManagerInput.getManagerId());
    }

    @AfterEach
    void tearDown() {
    }
}
