package pl.jgora.aeroklub.airflightslist.pilot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.jgora.aeroklub.airflightslist.model.Pilot;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class PilotService {
    private final PilotRepository pilotRepository;

    public List<Pilot> findAll() {
        return pilotRepository.findPilotsByOrderByName();
    }

    public Pilot findById(Long id) {
        return pilotRepository.findFirstById(id);
    }

    void activationUpdate(Pilot pilot, boolean active) {
        pilot.setActive(active);
        pilotRepository.save(pilot);
    }

    void update(Pilot pilot) {
        if (pilot != null && pilot.getId() != null) {
            Pilot toEdit = findById(pilot.getId());
            if (toEdit != null) {
                toEdit.setFia(pilot.getFia());
                toEdit.setFis(pilot.getFis());
                toEdit.setSepl(pilot.getSepl());
                toEdit.setActive(pilot.getActive());
                toEdit.setEngineInstructor(pilot.getEngineInstructor());
                toEdit.setEnginePilot(pilot.getEnginePilot());
                toEdit.setEnginePractise(pilot.getEnginePractise());
                toEdit.setGliderInstructor(pilot.getGliderInstructor());
                toEdit.setGliderPilot(pilot.getGliderPilot());
                toEdit.setGliderPractise(pilot.getGliderPractise());
                toEdit.setLicence(pilot.getLicence());
                toEdit.setMedicine(pilot.getMedicine());
                toEdit.setName(pilot.getName());
                toEdit.setTheory(pilot.getTheory());
                toEdit.setTow(pilot.getTow());
                toEdit.setNativeMember(pilot.getNativeMember());
                toEdit.setAddress(pilot.getAddress());
                pilotRepository.save(toEdit);
            }
        }
    }

    List<Pilot> filteredPilots(PilotFilter pilotFilter) {
        return pilotRepository.getFilteredPilots(pilotFilter);
    }

    public Set<Pilot> getEnginePilots() {
        return pilotRepository.findByEnginePilotTrueAndActiveTrueOrderByName();
    }

    public Set<Pilot> getEngineInstructors() {
        return pilotRepository.findByEngineInstructorTrueAndActiveTrueOrderByName();
    }

    public Set<Pilot> getTowPilots() {
        return pilotRepository.findByTowTrueAndActiveTrueOrderByName();
    }

    public Set<Pilot> getGliderInstructors() {
        return pilotRepository.findByGliderInstructorTrueAndActiveTrueOrderByName();
    }

    public Set<Pilot> getGliderPilots() {
        return pilotRepository.findByGliderPilotTrueAndActiveTrueOrderByName();
    }

}
