package pl.jgora.aeroklub.airflightslist.gliderFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.jgora.aeroklub.airflightslist.model.AbstractFlight;
import pl.jgora.aeroklub.airflightslist.model.GliderFlight;
import pl.jgora.aeroklub.airflightslist.model.ListSummary;
import pl.jgora.aeroklub.airflightslist.model.StartMethod;
import pl.jgora.aeroklub.airflightslist.pdfExporter.PdfExporter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("admin/glider-flights")
public class GliderFlightController {
    private final GliderFlightService gliderFlightService;

    @ModelAttribute("type")
    String getType() {
        return "glider";
    }

    @GetMapping
    public String gliderFlightsDates(Model model, @RequestParam(name = "year", required = false) Integer year) {
        if (year == null) {
            log.debug("\n NOT YEAR PARAMETER - GETTING THIS YEAR ");
            year = LocalDate.now().getYear();
        }
        Map<LocalDate, Boolean> datesAndActives = gliderFlightService.getDatesAndActives(year);
        for (Map.Entry<LocalDate, Boolean> entry : datesAndActives.entrySet()) {
            log.debug("\ndate {} active {}", entry.getKey().toString(), entry.getValue());
        }
        model.addAttribute("previousYear", year - 1);
        model.addAttribute("nextYear", year + 1);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("dates", datesAndActives);
        return "flights/dates";

    }

    @GetMapping("/list")
    public String gliderDailyFlights(Model model, @RequestParam("date") String date) {
        log.debug("\ndate {}", date);
        Set<GliderFlight> flightsInDay = gliderFlightService.getByDate(LocalDate.parse(date));
        Set<AbstractFlight> flights = flightsInDay.stream().map(flight -> (AbstractFlight) flight).collect(Collectors.toSet());
        Set<AbstractFlight> towFlights = flightsInDay
                .stream()
                .filter(flight -> StartMethod.ATTO.equals(flight.getStartMethod()))
                .map(flight -> (AbstractFlight) flight.getEngineFlight())
                .collect(Collectors.toSet());
        log.debug("\n flying-list size {}", flightsInDay.size());
        model.addAttribute("date", date);
        model.addAttribute("flights", flightsInDay);
        model.addAttribute("summary", new ListSummary(flights));
        model.addAttribute("towSummary", new ListSummary(towFlights));
        List<GliderFlight> gliderFlights = new ArrayList<>(flightsInDay);
        PdfExporter.addPdfExporterToModel("pdf", model, PdfExporter.ListType.DAILY, gliderFlights);
        model.addAttribute("category", "daily");
        model.addAttribute("type", "glider");
        return "flights/list";
    }

    @GetMapping("/details")
    public String showFlightDetails(Model model, @RequestParam("id") Long id, @RequestParam("date") String date) {
        log.debug("\n SHOWING DETAILS OF FLIGHT WITH ID: {}", id);
        if (gliderFlightService.getById(id) != null) {
            model.addAttribute("flight", gliderFlightService.getById(id));
            return "flights/flight-details";
        }
        return gliderDailyFlights(model, date);
    }

    @PostMapping("/activate")
    public String activateList(@RequestParam("date") String date) {
        log.debug("\nACTIVATING LIST FROM {}", date);
        Set<GliderFlight> flights = gliderFlightService.getByDate(LocalDate.parse(date));
        log.debug("FLIGHTS TO ACTIVATE {}", flights.size());
        for (GliderFlight flight : flights) {
            gliderFlightService.activateDeactivate(flight, true);

        }
        String year = date.substring(0, 4);
        return "redirect:/admin/glider-flights?year=" + year;
    }

    @PostMapping("/deactivate")
    public String deactivateList(@RequestParam("date") String date) {
        log.debug("\nDEACTIVATING LIST FROM {}", date);
        Set<GliderFlight> flights = gliderFlightService.getByDate(LocalDate.parse(date));
        for (GliderFlight flight : flights) {
            gliderFlightService.activateDeactivate(flight, false);
        }
        String year = date.substring(0, 4);
        return "redirect:/admin/glider-flights?year=" + year;
    }
}
