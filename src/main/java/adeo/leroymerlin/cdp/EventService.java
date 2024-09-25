package adeo.leroymerlin.cdp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getEvents() {
        return eventRepository.findAllBy();
    }

    public void delete(Event event) {
        eventRepository.delete(event);
    }

    public void save(Event event) {eventRepository.save(event);}

    public Event findById(Long id){
        return eventRepository.findById(id).orElse(null);}

    public List<Event> getFilteredEvents(String query) {
        List<Event> events = eventRepository.findAllBy();
        // Filter the events list in pure JAVA here

        return events;
    }
}
