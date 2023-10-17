package com.LP2.EventScheduler.api.event.service;

import com.LP2.EventScheduler.exception.CategoryNotFoundException;
import com.LP2.EventScheduler.filters.EventSortingOptions;
import com.LP2.EventScheduler.model.Category;
import com.LP2.EventScheduler.model.Event;
import com.LP2.EventScheduler.model.enums.Visibility;
import com.LP2.EventScheduler.repository.CategoryRepository;
import com.LP2.EventScheduler.repository.EventRepository;
import com.LP2.EventScheduler.response.ListResponse;
import com.LP2.EventScheduler.response.event.EventItem;
import com.LP2.EventScheduler.service.event.EventServiceImpl;

import com.github.javafaker.Faker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class SearchPublicEventsServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    private final String searchQuery = "test";

    private Category showCategory;
    private Category concertCategory;
    private Event showEvent;
    private Event concertEvent;

    @BeforeEach
    public void setUp() {
        this.showCategory = Category.builder()
                .id(UUID.randomUUID())
                .name("show")
                .build();

        this.showEvent = Event.builder()
                .id(UUID.randomUUID())
                .name("Show test event")
                .category(showCategory)
                .realizationDate(LocalDateTime.now().plusDays(1))
                .createdAt(LocalDateTime.now())
                .visibility(Visibility.PUBLIC)
                .finishDate(LocalDateTime.now().plusDays(2))
                .build();

        this.concertCategory = Category.builder()
                .id(UUID.randomUUID())
                .name("concert")
                .build();

        this.concertEvent = Event.builder()
                .id(UUID.randomUUID())
                .name("Concert test event")
                .category(concertCategory)
                .realizationDate(LocalDateTime.now().plusDays(2))
                .createdAt(LocalDateTime.now())
                .visibility(Visibility.PUBLIC)
                .finishDate(LocalDateTime.now().plusDays(3))
                .build();
    }

    @Test
    @DisplayName("Should Return A List Of Public Events")
    public void ReturnAListOfPublicEvents() {
        Event privateEvent = Event.builder()
                .id(UUID.randomUUID())
                .name("Show test event")
                .category(showCategory)
                .visibility(Visibility.ONLY_CONNECTIONS)
                .build();

        Mockito.when(
                this.eventRepository.searchEvents(
                        this.searchQuery,
                        null,
                        Visibility.PUBLIC,
                        null)
        ).thenReturn(List.of(
                this.showEvent,
                this.concertEvent
        ));

        ListResponse<EventItem> response = this.eventService.searchPublicEvents(
                this.searchQuery,
                null,
                null
        );

        Assertions.assertTrue(response.getData().stream().anyMatch(
                event -> event.getId().equals(this.showEvent.getId())
        ));
        Assertions.assertTrue(response.getData().stream().anyMatch(
                event -> event.getId().equals(this.concertEvent.getId())
        ));
        Assertions.assertFalse(response.getData().stream().anyMatch(
                event -> event.getId().equals(privateEvent.getId())
        ));
    }

    @Test
    @DisplayName("Should Return A List Of Events Filtered By Name Using SearchQuery")
    public void ReturnAListOfEventsFilteredByName() {
        Faker faker = new Faker();

        Event expectedEvent = Event.builder()
                .id(UUID.randomUUID())
                .name(faker.name().title())
                .category(showCategory)
                .visibility(Visibility.PUBLIC)
                .build();

        Mockito.when(
                this.eventRepository.searchEvents(
                        expectedEvent.getName(),
                        null,
                        Visibility.PUBLIC,
                        null)
        ).thenReturn(List.of(expectedEvent));

        ListResponse<EventItem> response = this.eventService.searchPublicEvents(
                expectedEvent.getName(),
                null,
                null
        );

        Assertions.assertTrue(response.getData().stream().anyMatch(
                event -> event.getId().equals(expectedEvent.getId())
        ));
        Assertions.assertFalse(response.getData().stream().anyMatch(
                event -> event.getId().equals(this.showEvent.getId())
        ));
        Assertions.assertFalse(response.getData().stream().anyMatch(
                event -> event.getId().equals(this.concertEvent.getId())
        ));
    }

    @Test
    @DisplayName("Should Return A List Of Events Filtered By Category Name")
    public void ReturnAListOfEventsFilteredByCategoryName() {
        Mockito.when(
                this.categoryRepository.findByName(this.showCategory.getName())
        ).thenReturn(Optional.ofNullable(this.showCategory));

        Mockito.when(
                this.eventRepository.searchEvents(
                        this.searchQuery,
                        null,
                        Visibility.PUBLIC,
                        this.showCategory)
        ).thenReturn(List.of(
                this.showEvent
        ));

        ListResponse<EventItem> response = this.eventService.searchPublicEvents(
                this.searchQuery,
                null,
                this.showCategory.getName()
        );

        Assertions.assertTrue(response.getData().stream().anyMatch(
                event -> event.getId().equals(this.showEvent.getId())
        ));
        Assertions.assertFalse(response.getData().stream().anyMatch(
                event -> event.getId().equals(this.concertEvent.getId())
        ));
    }

    @Test
    @DisplayName("Should Return A Exception If There Is No Category With The Sent Name")
    public void ReturnAExceptionIfThereIsNoCategoryWithTheSentName() {
        Faker faker = new Faker();

        Mockito.when(
                this.categoryRepository.findByName(Mockito.any())
        ).thenThrow(new CategoryNotFoundException());

        CategoryNotFoundException thrown = Assertions.assertThrows(
                CategoryNotFoundException.class,
                () -> this.eventService.searchPublicEvents(this.searchQuery, null, faker.name().name())
        );

        Assertions.assertEquals("The category does not exist", thrown.getMessage());
    }

    @Test
    @DisplayName("Should Return A List Of Events Sorted By Recent By Default")
    public void ReturnAListOfEventsSortedByRecentByDefault() {
        Event lastCreatedEvent = Event.builder()
                .id(UUID.randomUUID())
                .name("Show test event")
                .category(showCategory)
                .createdAt(LocalDateTime.now())
                .visibility(Visibility.PUBLIC)
                .build();

        Mockito.when(
                this.eventRepository.searchEvents(
                        this.searchQuery,
                        null,
                        Visibility.PUBLIC,
                        null)
        ).thenReturn(List.of(
                this.showEvent,
                this.concertEvent,
                lastCreatedEvent
        ));

        ListResponse<EventItem> response = this.eventService.searchPublicEvents(
                this.searchQuery,
                null,
                null
        );

        Assertions.assertEquals(this.showEvent.getId(), response.getData().get(0).getId());
        Assertions.assertEquals(this.concertEvent.getId(), response.getData().get(1).getId());
        Assertions.assertEquals(lastCreatedEvent.getId(), response.getData().get(2).getId());
    }

    @Test
    @DisplayName("Should Return A List Of Events Sorted By Recent")
    public void ReturnAListOfEventsSortedByRecent() {
        Event lastCreatedEvent = Event.builder()
                .id(UUID.randomUUID())
                .name("Show test event")
                .category(showCategory)
                .createdAt(LocalDateTime.now())
                .visibility(Visibility.PUBLIC)
                .build();

        Mockito.when(
                this.eventRepository.searchEvents(
                        this.searchQuery,
                        EventSortingOptions.RECENT,
                        Visibility.PUBLIC,
                        null)
        ).thenReturn(List.of(
                this.showEvent,
                this.concertEvent,
                lastCreatedEvent
        ));

        ListResponse<EventItem> response = this.eventService.searchPublicEvents(
                this.searchQuery,
                EventSortingOptions.RECENT,
                null
        );

        Assertions.assertEquals(this.showEvent.getId(), response.getData().get(0).getId());
        Assertions.assertEquals(this.concertEvent.getId(), response.getData().get(1).getId());
        Assertions.assertEquals(lastCreatedEvent.getId(), response.getData().get(2).getId());
    }

    @Test
    @DisplayName("Should Return A List Of Events Sorted By Upcoming")
    public void ReturnAListOfEventsSortedByUpcoming() {
        Event mostUpcomingEvent = Event.builder()
                .id(UUID.randomUUID())
                .name("Show test event")
                .category(showCategory)
                .realizationDate(LocalDateTime.now().plusHours(1))
                .visibility(Visibility.PUBLIC)
                .build();

        Mockito.when(
                this.eventRepository.searchEvents(
                        this.searchQuery,
                        EventSortingOptions.UPCOMING,
                        Visibility.PUBLIC,
                        null)
        ).thenReturn(List.of(
                mostUpcomingEvent,
                this.showEvent,
                this.concertEvent
        ));

        ListResponse<EventItem> response = this.eventService.searchPublicEvents(
                this.searchQuery,
                EventSortingOptions.UPCOMING,
                null
        );

        Assertions.assertEquals(mostUpcomingEvent.getId(), response.getData().get(0).getId());
        Assertions.assertEquals(this.showEvent.getId(), response.getData().get(1).getId());
        Assertions.assertEquals(this.concertEvent.getId(), response.getData().get(2).getId());
    }
}
