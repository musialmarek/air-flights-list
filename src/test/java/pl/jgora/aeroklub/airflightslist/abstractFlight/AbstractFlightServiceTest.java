package pl.jgora.aeroklub.airflightslist.abstractFlight;

import org.junit.jupiter.api.Test;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AbstractFlightServiceTest {

    @Test
    void shouldUpdateFlightDataToDataFromNewFlight() {
//TODO create parametrized test with more data
        //given
        EngineFlight engineFlight = (EngineFlight) FlightTestBase.builder("engine")
                .withActive(true)
                .withPicName("Żwirko Franciszek")
                .withCopilotName("Wigura Stanisław")
                .withDate(LocalDate.of(1932, 8, 15))
                .withStart(LocalTime.of(12, 55))
                .withTouchdown(LocalTime.of(15, 33))
                .withRegistrationNumber("SP-AHN")
                .withAircraftType("RWD-6")
                .withTask("Chalange")
                .withInstructor(false)
                .build();
        EngineFlight newEngineFlight = (EngineFlight) FlightTestBase.builder("engine").withActive(false)
                .withPicName("Wigura stanisław")
                .withCopilotName("Żwirko Franciszak")
                .withDate(LocalDate.of(1932, 8, 12))
                .withStart(LocalTime.of(10, 33))
                .withTouchdown(LocalTime.of(16, 17))
                .withRegistrationNumber("SP-AHL")
                .withAircraftType("RWD6")
                .withTask("CHAL")
                .withInstructor(true)
                .build();
        //when
        AbstractFlightService.updateFlight(engineFlight, newEngineFlight);
        //then
        assertThat(engineFlight.getActive()).isEqualTo(newEngineFlight.getActive());
        assertThat(engineFlight.getDate()).isEqualTo(newEngineFlight.getDate());
        assertThat(engineFlight.getStart()).isEqualTo(newEngineFlight.getStart());
        assertThat(engineFlight.getTouchdown()).isEqualTo(newEngineFlight.getTouchdown());
        assertThat(engineFlight.getAircraftRegistrationNumber()).isEqualTo(newEngineFlight.getAircraftRegistrationNumber());
        assertThat(engineFlight.getAircraftType()).isEqualTo(newEngineFlight.getAircraftType());
        assertThat(engineFlight.getPicName()).isEqualTo(newEngineFlight.getPicName());
        assertThat(engineFlight.getCopilotName()).isEqualTo(newEngineFlight.getCopilotName());
        assertThat(engineFlight.getTask()).isEqualTo(newEngineFlight.getTask());
    }

    @Test
    void replacePilots() {
        //TODO
    }

}