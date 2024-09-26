package adeo.leroymerlin.cdp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EventServiceIT {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private BandRepository bandRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void getEvents() {
        eventRepository.deleteAll();

        Event event1 = new Event();
        event1.setTitle("Event 1");
        eventService.save(event1);

        Event event2 = new Event();
        event2.setTitle("Event 2");
        eventService.save(event2);

        List<Event> events = eventService.getEvents();

        assertEquals(2,events.size());
        assertTrue(events.stream().anyMatch(e -> e.getTitle().equals("Event 1")));
        assertTrue(events.stream().anyMatch(e -> e.getTitle().equals("Event 2")));
    }

    @Test
    public void saveEvent() {
        eventRepository.deleteAll();
        Event event = new Event();
        event.setTitle("Event in save Test");
        eventService.save(event);

        Event savedEvent = eventService.findById(event.getId());
        assertNotNull(savedEvent);
        assertEquals("Event in save Test",savedEvent.getTitle());
    }

    @Test
    public void deleteEvent() {
        eventRepository.deleteAll();
        Event event = new Event();
        event.setTitle("Event in delete Test");
        eventService.save(event);
        eventService.delete(event);

        assertNull(eventService.findById(event.getId()));
    }

    @Test
    public void getFilteredEvents() {
        eventRepository.deleteAll();

        Member member1 = new Member();
        member1.setName("Member1");
        memberRepository.save(member1);

        Member member2 = new Member();
        member2.setName("Member2");
        memberRepository.save(member2);

        Band band1 = new Band();
        band1.setName("Band with mb 1 and 2");
        band1.setMembers(Set.of(member1,member2));
        bandRepository.save(band1);




        Event event1 = new Event();
        event1.setTitle("Event with Band 1");
        event1.setBands(Set.of(band1));
        eventService.save(event1);

        List<Event> filteredEvents = eventService.getFilteredEvents("1");

        assertEquals(1, filteredEvents.size());
        assertTrue(filteredEvents.get(0).getTitle().contains("Event with Band 1"));

    }



}
