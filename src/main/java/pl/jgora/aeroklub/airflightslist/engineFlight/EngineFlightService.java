package pl.jgora.aeroklub.airflightslist.engineFlight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.jgora.aeroklub.airflightslist.abstractFlight.AbstractFlightService;
import pl.jgora.aeroklub.airflightslist.abstractFlight.FlightsFilter;
import pl.jgora.aeroklub.airflightslist.model.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class EngineFlightService {
    private final EngineFlightRepository engineFlightRepository;
    private final AbstractFlightService abstractFlightService;

    private Set<LocalDate> getAllFlyingDays(int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate finish = LocalDate.of(year, 12, 31);
        log.debug("\n START: {} FINISH: {}", start, finish);
        return engineFlightRepository.getFlyingEngineDays(start, finish);
    }

    public Set<EngineFlight> getByDate(LocalDate date) {
        return engineFlightRepository.getDistinctByDateOrderByStart(date);
    }

    public EngineFlight save(EngineFlight flight) {
        AbstractFlightService.replacePilots(flight);
        if (flight.getCharge() && flight.getCost() == null) {
            flight.setCost(abstractFlightService.calculateCost(flight));
        }
        log.debug("\n SAVING ENGINE FLIGHT {}", flight);
        return engineFlightRepository.save(flight);
    }

    public EngineFlight getById(Long id) {
        return engineFlightRepository.findFirstById(id);
    }

    public EngineFlight update(EngineFlight flight) {
        log.debug("\nCHECKING ENGINE FLIGHT {}", flight);
        if (flight != null && flight.getId() != null) {
            log.debug("\nGETTING ENGINE-FLIGHT FROM DATABASE");
            EngineFlight toEdit = engineFlightRepository.findFirstById(flight.getId());
            log.debug("\n SETTING ALL FIELDS IN ENGINE-FLIGHT TO EDIT \n OLD DATA {} \n NEW DATA{}", toEdit, flight);
            abstractFlightService.updateFlight(flight, toEdit);
            toEdit.setCrew(flight.getCrew());
            toEdit.setTow(flight.getTow());
            log.debug("SAVING ENGINE-FLIGHT WITH NEW DATA");
            return save(toEdit);
        }
        return flight;
    }

    private boolean isEveryFlightActive(LocalDate date) {
        boolean anyInactive = true;
        Set<EngineFlight> flights = getByDate(date);
        for (EngineFlight flight : flights) {
            if (!flight.getActive()) {
                anyInactive = false;
                break;
            }
        }
        return anyInactive;
    }

    public void activateDeactivate(EngineFlight flight, boolean activate) {
        EngineFlight toChange = engineFlightRepository.findFirstById(flight.getId());
        toChange.setActive(activate);
        engineFlightRepository.save(toChange);
    }

    public void setNote(Note note, AbstractFlight flight) {
        EngineFlight toEdit = engineFlightRepository.findFirstById(flight.getId());
        toEdit.setNote(note);
        log.debug("EF SETTING NOTE {} IN FLIGHT {}", note, toEdit);
        engineFlightRepository.save(toEdit);
    }

    Map<LocalDate, Boolean> getDatesAndActives(int year) {
        Map<LocalDate, Boolean> datesAndActives = new LinkedHashMap<>();
        Set<LocalDate> allFlyingDays = getAllFlyingDays(year);
        allFlyingDays.forEach(date -> datesAndActives.put(date, isEveryFlightActive(date)));
        return datesAndActives;
    }

    List<EngineFlight> getByPilot(Pilot pilot) {
        String name = pilot.getName();
        log.debug("SEARCHING ALL ENGINE FLIGHTS BY PILOT {}", name);
        return engineFlightRepository.findByPicOrCopilotOrPicNameOrCopilotNameOrderByDateAscStart(pilot, pilot, name, name);
    }


    public List<EngineFlight> getFilteredEngineFlightsByPilot(FlightsFilter flightsFilter) {
        return engineFlightRepository.getFilteredEngineFlights(flightsFilter);
    }

    public List<EngineFlight> getFilteredFlightsByAircraft(FlightsFilter flightsFilter) {
        return engineFlightRepository.getFilteredEngineFlights(flightsFilter);
    }


    public List<EngineFlight> getAllByAircraft(Aircraft aircraft) {
        String type = aircraft.getType();
        String registrationNumber = aircraft.getRegistrationNumber();
        log.debug("SEARCHING ALL ENGINE FLIGHTS BY AIRCRAFT: {} {}", type, registrationNumber);
        return engineFlightRepository.findByAircraftOrAircraftTypeAndAircraftRegistrationNumberOrderByDateAscStartAsc(aircraft, type, registrationNumber);
    }

    public List<EngineFlight> getAllToChargeByPayer(Pilot payer) {
        return engineFlightRepository.findByPayerAndActiveAndChargeAndNoteIsNullAndTowIsFalseOrTowIsNull(payer, true, true);
    }
}
