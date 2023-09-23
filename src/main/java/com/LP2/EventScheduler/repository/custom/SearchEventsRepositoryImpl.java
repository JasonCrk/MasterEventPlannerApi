package com.LP2.EventScheduler.repository.custom;

import com.LP2.EventScheduler.filters.EventSortingOptions;
import com.LP2.EventScheduler.model.Category;
import com.LP2.EventScheduler.model.Event;
import com.LP2.EventScheduler.model.enums.Visibility;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class SearchEventsRepositoryImpl implements SearchEventsRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Event> searchEvents(
            String query,
            EventSortingOptions sortBy,
            Visibility visibility,
            Category category
    ) throws IllegalArgumentException {
        if (query == null)
            throw new IllegalArgumentException("The query cannot be null");

        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();

        CriteriaQuery<Event> cq = cb.createQuery(Event.class);

        Root<Event> eventRoot = cq.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.like(cb.lower(eventRoot.get("name")), "%" + query.toLowerCase() + "%"));

        Order orderBy = null;
        if (sortBy == null) {
            orderBy = cb.asc(eventRoot.get("createdAt"));
        } else if (sortBy.name().equals(EventSortingOptions.RECENT.name())) {
            orderBy = cb.asc(eventRoot.get("createdAt"));
        } else if (sortBy.name().equals(EventSortingOptions.UPCOMING.name())) {
            orderBy = cb.desc(eventRoot.get("realizationDate"));
        }

        if (visibility != null)
            predicates.add(cb.equal(eventRoot.get("visibility"), visibility));

        if (category != null)
            predicates.add(cb.equal(eventRoot.get("category"), category));

        cq.select(eventRoot).where(predicates.toArray(new Predicate[]{})).orderBy(orderBy);

        return entityManager
                .createQuery(cq)
                .getResultList();
    }
}
