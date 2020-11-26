package fr.nextdigital.lab.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import fr.nextdigital.lab.domain.Aggregate;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.Optional;

/**
 * Event service implementation of {@link EventService} for managing {@link Event} entities.
 *
 * @author Kenny Bastani
 * @see Event
 * @see Events
 * @see EventService
 */
@SuppressWarnings("unchecked")
public class BasicEventService<T extends Event, ID extends Serializable> implements EventService<T, ID> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BasicEventService.class);

	@Value("${events.worker:http://localhost:8080/v1/events}")
    private String eventsWorker;

    private final EventRepository<T, ID> eventRepository;
    private final EventSource eventSource;
    private final RestTemplate restTemplate;

    public BasicEventService(EventRepository<T, ID> eventRepository, EventSource eventSource, @LoadBalanced RestTemplate
            restTemplate) {
        this.eventRepository = eventRepository;
        this.eventSource = eventSource;
        this.restTemplate = restTemplate;
    }

    public <E extends Aggregate, S extends T> S send(S event, Link... links) {
        // Assemble request to the event stream processor
        RequestEntity<Resource<T>> requestEntity = RequestEntity.post(URI.create(eventsWorker))
                .contentType(MediaTypes.HAL_JSON)
                .body(new Resource<T>(event), Resource.class);

        try {
            // Send the event to the event stream processor
            ResponseEntity<E> response = restTemplate.exchange(requestEntity, (Class<E>) event.getEntity().getClass());
            E entity = response.getBody();

            // Set the applied entity reference to the event
            event.setEntity(entity);
        } catch (Exception ex) {
            log.error("send ",ex);
            throw new ResourceAccessException(ex.getMessage(), new IOException(ex));
        }

        return event;
    }

    public <S extends T> Boolean sendAsync(S event, Link... links) {
        return eventSource.getChannel()
                .send(MessageBuilder.withPayload(event)
                        .setHeader("contentType", MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .build());
    }

    public <S extends T> S save(S event) {
        event = eventRepository.save(event);
        return event;
    }

    public <S extends T> S save(ID id, S event) {
        event.setEventId(id);
        return save(event);
    }

    public <S extends ID> T findOne(S id) {
        return eventRepository.findById(id).get();
    }

    public <E extends Events> E find(ID entityId) {
        return (E) new Events(entityId, eventRepository.findEventsByEntityId(entityId,
                new PageRequest(0, Integer.MAX_VALUE))
                .getContent());
    }
}
