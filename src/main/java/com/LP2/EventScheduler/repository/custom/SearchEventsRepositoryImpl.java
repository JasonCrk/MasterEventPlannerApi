package com.LP2.EventScheduler.repository.custom;

import com.LP2.EventScheduler.filters.EventSortingOptions;
import com.LP2.EventScheduler.model.Category;
import com.LP2.EventScheduler.model.Event;
import com.LP2.EventScheduler.model.Participation;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.model.enums.Visibility;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
            orderBy = cb.desc(eventRoot.get("createdAt"));
        } else if (sortBy.name().equals(EventSortingOptions.RECENT.name())) {
            orderBy = cb.desc(eventRoot.get("createdAt"));
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

    @Override
    public List<Event> searchEventsCreatedAndParticipating(
            EventSortingOptions sortBy,
            Category category,
            User user
    ) {
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();

        CriteriaQuery<Event> cq = criteriaBuilder.createQuery(Event.class);

        Root<Event> eventRoot = cq.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();

        Predicate getUserCreatedEvents = criteriaBuilder.equal(eventRoot.get("coordinator"), user);

        Subquery<UUID> subquery = criteriaBuilder.createQuery().subquery(UUID.class);
        Root<Participation> participationRoot = subquery.from(Participation.class);
        subquery.select(participationRoot.get("event").get("id"));
        subquery.where(criteriaBuilder.equal(participationRoot.get("user"), user));

        Predicate getEventsTheUserParticipatesIn = criteriaBuilder.in(eventRoot.get("id")).value(subquery);

        predicates.add(criteriaBuilder.or(getUserCreatedEvents, getEventsTheUserParticipatesIn));

        Order orderBy = null;
        if (sortBy == null) {
            orderBy = criteriaBuilder.desc(eventRoot.get("createdAt"));
        } else if (sortBy.name().equals(EventSortingOptions.RECENT.name())) {
            orderBy = criteriaBuilder.desc(eventRoot.get("createdAt"));
        } else if (sortBy.name().equals(EventSortingOptions.UPCOMING.name())) {
            orderBy = criteriaBuilder.desc(eventRoot.get("realizationDate"));
        }

        if (category != null)
            predicates.add(criteriaBuilder.equal(eventRoot.get("category"), category));

        cq.select(eventRoot).where(predicates.toArray(new Predicate[]{})).orderBy(orderBy);

        return entityManager
                .createQuery(cq)
                .getResultList();
    }
}
