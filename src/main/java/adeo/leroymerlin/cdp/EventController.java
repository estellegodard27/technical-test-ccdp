package adeo.leroymerlin.cdp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Event> findEvents() {
        return eventService.getEvents();
    }

    @RequestMapping(value = "/search/{query}", method = RequestMethod.GET)
    public List<Event> findEvents(@PathVariable String query) {
        return eventService.getFilteredEvents(query);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteEvent(@PathVariable Long id) {
        Event existingEvent = eventService.findById(id);
        if (existingEvent != null) {
            eventService.delete(existingEvent);
        }

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updateEvent(@PathVariable Long id, @RequestBody Event event) {
        Event existingEvent =  eventService.findById(id);
        if (existingEvent != null) {
            existingEvent.setNbStars(event.getNbStars());
            existingEvent.setComment(event.getComment());

            eventService.save(existingEvent);
        }
    }
}
