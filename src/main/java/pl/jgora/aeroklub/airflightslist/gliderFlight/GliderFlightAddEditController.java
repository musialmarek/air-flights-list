package pl.jgora.aeroklub.airflightslist.gliderFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.jgora.aeroklub.airflightslist.abstractFlight.AbstractFlightService;
import pl.jgora.aeroklub.airflightslist.aircraft.AircraftService;
import pl.jgora.aeroklub.airflightslist.model.Aircraft;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;
import pl.jgora.aeroklub.airflightslist.model.GliderFlight;
import pl.jgora.aeroklub.airflightslist.model.Pilot;
import pl.jgora.aeroklub.airflightslist.pilot.PilotService;

import java.time.LocalDate;
import java.util.Set;

@Controller
@RequestMapping("admin/glider-flights")
@RequiredArgsConstructor
@Slf4j
public class GliderFlightAddEditController {
    private final PilotService pilotService;
    private final AircraftService aircraftService;
    private final GliderFlightService gliderFlightService;

    @ModelAttribute("type")
    String getType() {
        return "glider";
    }

    @ModelAttribute("pilots")
    Set<Pilot> getPilots() {
        return pilotService.getGliderPilots();
    }

    @ModelAttribute("instructors")
    Set<Pilot> getInstructors() {
        return pilotService.getGliderInstructors();
    }

    @ModelAttribute("aircrafts")
    Set<Aircraft> getAircrafts() {
        return aircraftService.getGliders();
    }

    @ModelAttribute("towaircrafts")
    Set<Aircraft> getTowAircrafts() {
        return aircraftService.getEngineAircrafts();
    }

    @ModelAttribute("towpilots")
    Set<Pilot> getTowPilots() {
        return pilotService.getTowPilots();
    }

    @ModelAttribute("engineinstructors")
    Set<Pilot> getEngineInstructors() {
        return pilotService.getEngineInstructors();
    }

    @GetMapping("/add")
    public String addFlightForm(Model model, @RequestParam("date") String date, @RequestParam(name = "id", required = false) Long id) {
        GliderFlight flight = new GliderFlight();
        flight.setDate(LocalDate.parse(date));
        if (id != null) {
            flight = gliderFlightService.getById(id);
            AbstractFlightService.replacePilots(flight);
            flight.setId(null);
        } else {
            flight.setCharge(true);
            flight.setEngineFlight(new EngineFlight());
            flight.getEngineFlight().setCharge(true);
        }
        log.debug("ADDING FLIGHT TO MODEL");
        model.addAttribute("flight", flight);
        model.addAttribute("action", "add");
        return "flights/add-edit-flight";
    }

    @PostMapping("/add")
    public String addFlightAction(@ModelAttribute("flight") GliderFlight gliderFlight) {
        String date = gliderFlight.getDate().toString();
        log.debug("\n GLIDER-FLIGHT BEFORE SAVE: {}", gliderFlight);
        gliderFlightService.save(gliderFlight);
        log.debug("\n GLIDER-FLIGHT AFTER SAVE: {}", gliderFlight);
        return "redirect:/admin/glider-flights/list?date=" + date;
    }

    @GetMapping("/edit")
    public String gliderFlightEditForm(Model model, @RequestParam Long id) {
        GliderFlight toEdit = gliderFlightService.getById(id);
        log.debug("\nADDING EDITING FLIGHT TO MODEL {}", toEdit);
        AbstractFlightService.replacePilots(toEdit);
        model.addAttribute("flight", toEdit);
        model.addAttribute("action", "edit");
        return "flights/add-edit-flight";
    }

    @PostMapping("/edit")
    public String gliderFlightEditAction(@ModelAttribute("flight") GliderFlight toEdit) {
        log.debug("\nEDITING FLIGHT WITH ID {}", toEdit.getId());
        toEdit.getEngineFlight().setDate(toEdit.getDate());
        toEdit.getEngineFlight().setStart(toEdit.getStart());
        gliderFlightService.update(toEdit);
        String date = toEdit.getDate().toString();
        return "redirect:/admin/glider-flights/list?date=" + date;
    }
}