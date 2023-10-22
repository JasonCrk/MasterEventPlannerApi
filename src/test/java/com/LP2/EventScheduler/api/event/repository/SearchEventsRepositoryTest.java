package com.LP2.EventScheduler.api.event.repository;

import com.LP2.EventScheduler.filters.EventSortingOptions;
import com.LP2.EventScheduler.model.Account;
import com.LP2.EventScheduler.model.Category;
import com.LP2.EventScheduler.model.Event;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.model.enums.Visibility;
import com.LP2.EventScheduler.repository.AccountRepository;
import com.LP2.EventScheduler.repository.CategoryRepository;
import com.LP2.EventScheduler.repository.EventRepository;
import com.LP2.EventScheduler.repository.UserRepository;

import com.github.javafaker.Faker;

import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SearchEventsRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private final Faker faker = new Faker();
    private final String testQuery = "test";

    private Category secondCategory;
    private Event event1;
    private Event event2;
    private Event privateEvent3;

    @BeforeEach
    public void setup() {
        Category firstTestCategory = new Category();
        firstTestCategory.setIcon(this.faker.internet().avatar());
        firstTestCategory.setName("Test category 1");

        Category secondTestCategory = new Category();
        secondTestCategory.setIcon(this.faker.internet().avatar());
        secondTestCategory.setName("Test category 2");

        Category firstSavedCategory = this.categoryRepository.save(firstTestCategory);
        this.secondCategory = this.categoryRepository.save(secondTestCategory);

        Account testAccount = new Account();
        Account savedAccount = this.accountRepository.save(testAccount);

        User testUser = User.builder()
                .account(savedAccount)
                .userName(this.faker.name().username())
                .email(this.faker.internet().safeEmailAddress())
                .password(this.faker.internet().password())
                .build();
        User savedUser = this.userRepository.save(testUser);

        Event testEvent1 = Event.builder()
                .name("Test event 1")
                .coordinator(savedUser)
                .category(firstSavedCategory)
                .visibility(Visibility.PUBLIC)
                .local(this.faker.address().streetAddress())
                .realizationDate(LocalDateTime.now().plusDays(1))
                .createdAt(LocalDateTime.now().plusMinutes(1))
                .finishDate(LocalDateTime.now().plusDays(2))
                .build();

        Event testEvent2 = Event.builder()
                .name("Test event 2")
                .coordinator(savedUser)
                .category(this.secondCategory)
                .visibility(Visibility.PUBLIC)
                .local(this.faker.address().streetAddress())
                .realizationDate(LocalDateTime.now().plusDays(2))
                .createdAt(LocalDateTime.now().plusMinutes(2))
                .finishDate(LocalDateTime.now().plusDays(3))
                .build();

        Event testEvent3 = Event.builder()
                .name("Test event 3")
                .coordinator(savedUser)
                .category(firstSavedCategory)
                .visibility(Visibility.ONLY_CONNECTIONS)
                .local(this.faker.address().streetAddress())
                .realizationDate(LocalDateTime.now().plusDays(3))
                .createdAt(LocalDateTime.now().plusMinutes(3))
                .finishDate(LocalDateTime.now().plusDays(4))
                .build();

        this.event1 = this.eventRepository.save(testEvent1);
        this.event2 = this.eventRepository.save(testEvent2);
        this.privateEvent3 = this.eventRepository.save(testEvent3);
    }

    @AfterEach
    public void after() {
        this.categoryRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should Return A IllegalArgumentException If The Query Value Is Null")
    public void shouldReturnIllegalArgumentExceptionIfTheQueryIsNull() {
        IllegalArgumentException thrown = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> this.eventRepository.searchEvents(null, null, null, null)
        );

        Assertions.assertEquals("The query cannot be null", thrown.getMessage());
    }

    @Test
    @DisplayName("Should Return A List Of Events Filtered By Event Name")
    public void shouldReturnAListOfEventsFilteredByQuery() {
        List<Event> filteredEvents = this.eventRepository.searchEvents(
                this.event1.getName(),
                null,
                null,
                null
        );

        Assertions.assertTrue(filteredEvents.stream().anyMatch(
                event -> event.getId().equals(this.event1.getId())
        ));
        Assertions.assertFalse(filteredEvents.stream().anyMatch(
                event -> event.getId().equals(this.event2.getId())
        ));
        Assertions.assertFalse(filteredEvents.stream().anyMatch(
                event -> event.getId().equals(this.privateEvent3.getId())
        ));
    }

    @Test
    @DisplayName("Should Return A List Of Events Filtered By Event Name Regardless Of Capitalization")
    public void shouldReturnAListOfEventsRegardlessOfCapitalization() {
        List<Event> filteredEvents = this.eventRepository.searchEvents(
                this.testQuery.toUpperCase(),
                null,
                null,
                null
        );

        Assertions.assertEquals(3, filteredEvents.size());
        Assertions.assertTrue(filteredEvents.stream().anyMatch(
                event -> event.getId().equals(this.event1.getId())
        ));
        Assertions.assertTrue(filteredEvents.stream().anyMatch(
                event -> event.getId().equals(this.event2.getId())
        ));
        Assertions.assertTrue(filteredEvents.stream().anyMatch(
                event -> event.getId().equals(this.privateEvent3.getId())
        ));
    }

    @Test
    @DisplayName("Should Return A List Of Public Events")
    public void shouldReturnAListOfPublicEvents() {
        List<Event> filteredEvents = this.eventRepository.searchEvents(
                this.testQuery,
                null,
                Visibility.PUBLIC,
                null
        );

        Assertions.assertTrue(filteredEvents.stream().anyMatch(
                event -> event.getId().equals(this.event1.getId())
        ));
        Assertions.assertTrue(filteredEvents.stream().anyMatch(
                event -> event.getId().equals(this.event2.getId())
        ));
        Assertions.assertFalse(filteredEvents.stream().anyMatch(
                event -> event.getId().equals(this.privateEvent3.getId())
        ));
    }

    @Test
    @DisplayName("Should Return A List Of Only Connection Events")
    public void shouldReturnAListOfOnlyConnectionEvents() {
        List<Event> filteredEvents = this.eventRepository.searchEvents(
                this.testQuery,
                null,
                Visibility.ONLY_CONNECTIONS,
                null
        );

        Assertions.assertTrue(filteredEvents.stream().anyMatch(
                event -> event.getId().equals(this.privateEvent3.getId())
        ));
        Assertions.assertFalse(filteredEvents.stream().anyMatch(
                event -> event.getId().equals(this.event1.getId())
        ));
        Assertions.assertFalse(filteredEvents.stream().anyMatch(
                event -> event.getId().equals(this.event2.getId())
        ));
    }

    @Test
    @DisplayName("Should Return A List Of Events Sorted By Recent By Default")
    public void shouldReturnAListOfEventsSortedByRecentByDefault() {
        List<Event> filteredEvents = this.eventRepository.searchEvents(
                this.testQuery,
                null,
                null,
                null
        );
        filteredEvents.forEach(event -> {
            System.out.println(event.getCreatedAt() + " " + event.getName());
        });

        Assertions.assertEquals(this.privateEvent3.getId(), filteredEvents.get(0).getId());
        Assertions.assertEquals(this.event2.getId(), filteredEvents.get(1).getId());
        Assertions.assertEquals(this.event1.getId(), filteredEvents.get(2).getId());
    }

    @Test
    @DisplayName("Should Return A List Of Events Sorted By Recent")
    public void shouldReturnAListOfEventsSortedByRecent() {
        List<Event> filteredEvents = this.eventRepository.searchEvents(
                this.testQuery,
                EventSortingOptions.RECENT,
                null,
                null
        );

        Assertions.assertEquals(this.privateEvent3.getId(), filteredEvents.get(0).getId());
        Assertions.assertEquals(this.event2.getId(), filteredEvents.get(1).getId());
        Assertions.assertEquals(this.event1.getId(), filteredEvents.get(2).getId());
    }

    @Test
    @DisplayName("Should Return A List Of Events Sorted By Upcoming")
    public void shouldReturnAListOfEventsSortedByUpcoming() {
        List<Event> filteredEvents = this.eventRepository.searchEvents(
                this.testQuery,
                EventSortingOptions.UPCOMING,
                null,
                null
        );

        Assertions.assertEquals(this.event1.getId(), filteredEvents.get(2).getId());
        Assertions.assertEquals(this.event2.getId(), filteredEvents.get(1).getId());
        Assertions.assertEquals(this.privateEvent3.getId(), filteredEvents.get(0).getId());
    }

    @Test
    @DisplayName("Should Return A List Of Events From A Category")
    public void shouldReturnAListOfEventsFromACategory() {
        List<Event> filteredEvents = this.eventRepository.searchEvents(
                this.testQuery,
                null,
                null,
                this.secondCategory
        );

        Assertions.assertTrue(filteredEvents.stream().anyMatch(
                event -> event.getId().equals(this.event2.getId())
        ));
        Assertions.assertFalse(filteredEvents.stream().anyMatch(
                event -> event.getId().equals(this.event1.getId())
        ));
        Assertions.assertFalse(filteredEvents.stream().anyMatch(
                event -> event.getId().equals(this.privateEvent3.getId())
        ));
    }
}
