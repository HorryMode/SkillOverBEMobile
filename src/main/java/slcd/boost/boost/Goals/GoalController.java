package slcd.boost.boost.Goals;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import slcd.boost.boost.General.DTOs.UUIDResponse;
import slcd.boost.boost.Goals.DTOs.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/goals")
public class GoalController {

    @Autowired
    private GoalService goalService;

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public GoalResponse getGoalByUuid(@PathVariable(name = "uuid") String uuid) throws AccessDeniedException {
        return goalService.getGoalByUuid(uuid);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GoalShortResponse> getCurrentGoalByOwnerId(@RequestParam Long ownerId) throws AccessDeniedException {
        return goalService.getUserCurrentGoals(ownerId);
    }

    @GetMapping("/statistics/{id}/user")
    @ResponseStatus(HttpStatus.OK)
    public GoalsStatisticsDTO getUserGoalStatistics(@PathVariable(name = "id") Long id) throws AccessDeniedException {
        return goalService.getUserGoalStatistics(id);
    }

    @GetMapping("/statistics/{id}/team")
    @ResponseStatus(HttpStatus.OK)
    public List<GoalsStatisticsDTO> getTeamGoalStatistics(@PathVariable(name = "id") Long id) throws AccessDeniedException {
        return goalService.getTeamGoalsStatistics(id);
    }
}
