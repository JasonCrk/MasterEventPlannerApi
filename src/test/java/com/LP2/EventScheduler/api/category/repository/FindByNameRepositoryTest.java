package com.LP2.EventScheduler.api.category.repository;

import com.LP2.EventScheduler.model.Category;
import com.LP2.EventScheduler.repository.CategoryRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FindByNameRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category partyCategory;

    private final Faker faker = new Faker();

    @BeforeEach
    public void setup() {
        Category partyCategory = new Category();
        partyCategory.setIcon(this.faker.internet().avatar());
        partyCategory.setName("party");

        Category showCategory = new Category();
        showCategory.setIcon(this.faker.internet().avatar());
        showCategory.setName("show");

        this.partyCategory = this.categoryRepository.save(partyCategory);
        this.categoryRepository.save(showCategory);
    }

    @Test
    @DisplayName("Should Return A Event By Name")
    public void ReturnAEventByName() {
        Optional<Category> category = this.categoryRepository.findByName(this.partyCategory.getName());

        Assertions.assertFalse(category.isEmpty());
        Assertions.assertEquals(this.partyCategory.getId(), category.get().getId());
    }

    @Test
    @DisplayName("Should Return A Null If The Category Name Does Not Exist")
    public void ReturnANull() {
        Optional<Category> category = this.categoryRepository.findByName(this.faker.name().name());

        Assertions.assertTrue(category.isEmpty());
    }
}
