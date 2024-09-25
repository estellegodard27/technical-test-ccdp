package adeo.leroymerlin.cdp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        List<Event> filteredEvents = new ArrayList<>();
        Set<String> bandNameSet = new HashSet<>();
        for(Event event : events) {
            int countEvent =0;
            for(Band band : event.getBands()) {
                int countMember =0;
                for(Member member : band.getMembers()) {
                    if (member.getName().toLowerCase().contains(query.toLowerCase())) {
                        countMember++;
                        countEvent++;
                    }
                }
                if(!bandNameSet.contains(band.getName())&& countMember !=0) {
                    band.setName(band.getName() + " [" + countMember + "]");
                    bandNameSet.add(band.getName());
                }
            }
            if(countEvent !=0) {
                event.setTitle(event.getTitle() + " ["+countEvent+"]");
                filteredEvents.add(event);
            }
        }
        return filteredEvents;
    }
}
