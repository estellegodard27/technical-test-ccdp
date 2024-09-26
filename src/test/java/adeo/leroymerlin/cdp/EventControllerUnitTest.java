package adeo.leroymerlin.cdp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Set;

@WebMvcTest(controllers = EventController.class)
public class EventControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @MockBean
    private EventRepository eventRepository;


    @Test
    public void findEvents() throws Exception {

        Event event1 = new Event();
        event1.setTitle("Event 1");

        Event event2 = new Event();
        event2.setTitle("Event 2");

        List<Event> events = List.of(event1, event2);
        Mockito.when(eventService.getEvents()).thenReturn(events);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/events/"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Event 1"))
                .andExpect(jsonPath("$[1].title").value("Event 2"));
    }

    @Test
    public void findEventByQuery() throws Exception {

        Member member1 = new Member();
        member1.setName("Member1");


        Member member2 = new Member();
        member2.setName("Member2");


        Band band1 = new Band();
        band1.setName("Band with mb 1 and 2");
        band1.setMembers(Set.of(member1,member2));


        Event event1 = new Event();
        event1.setTitle("Event with Band 1");
        event1.setBands(Set.of(band1));


        List<Event> events = List.of(event1);
        Mockito.when(eventService.getFilteredEvents("1")).thenReturn(events);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/events/search/1"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Event with Band 1"));

    }

    @Test
    public void deleteEvent() throws Exception {
        Event event = new Event();
        event.setId(1L);
        Mockito.when(eventService.findById(1L)).thenReturn(event);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/events/1")).andExpect(status().isOk());

        assertFalse(eventRepository.existsById(1L));

    }

    @Test
    public void updateEvent() throws Exception {
        Event event = new Event();
        event.setId(1L);
        Mockito.when(eventService.findById(1L)).thenReturn(event);

        Event updatedEvent = new Event();
        updatedEvent.setNbStars(5);
        updatedEvent.setComment("Great event !");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nbStars\":5,\"comment\":\"Great event !\"}"))
                .andExpect(status().isOk());

        assertEquals(event.getNbStars(), updatedEvent.getNbStars());
        assertEquals(event.getComment(), updatedEvent.getComment());
    }

}
