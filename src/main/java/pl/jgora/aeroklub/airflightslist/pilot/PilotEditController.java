package pl.jgora.aeroklub.airflightslist.pilot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jgora.aeroklub.airflightslist.model.Pilot;

@Controller
@RequestMapping("/admin/pilot")
@RequiredArgsConstructor
@Slf4j
public class PilotEditController {
    private final PilotService pilotService;

    @GetMapping("/edit")
    public String editForm(Model model, @RequestParam("id") Long id) {
        Pilot toEdit = pilotService.findById(id);
        if (toEdit != null) {
            log.debug("\n ADDING PILOT: {} TO MODEL", toEdit.getName());
            model.addAttribute("pilot", toEdit);
            model.addAttribute("action", "edit");
            return "pilots/add-edit-pilot";
        }
        log.debug("\nTHERE IS NO PILOT WITH ID: {}", id);
        return "redirect:/admin/pilots";
    }

    @PostMapping("/edit")
    public String editAction(Pilot pilot) {
        log.debug("\n{}", pilot);
        pilotService.update(pilot);
        log.debug("\n SUCCESSFUL EDITING OF PILOT: {}", pilot.getName());
        return "redirect:/admin/pilots";
    }

}
