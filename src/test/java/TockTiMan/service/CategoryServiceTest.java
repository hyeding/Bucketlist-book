package TockTiMan.service;

import TockTiMan.dto.category.CategoryCreateRequest;
import TockTiMan.dto.category.CategoryDto;
import TockTiMan.entity.category.Category;
import TockTiMan.repository.category.CategoryRepository;
import TockTiMan.repository.user.UserRepository;
import TockTiMan.service.category.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static TockTiMan.factory.CategoryFactory.createCategory;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    CategoryService categoryService;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    UserRepository userRepository;


    @Test
    @DisplayName("findAll 서비스 테스트")
    void findAllTest() {
        // given
        List<Category> categories = new ArrayList<>();
        categories.add(createCategory());
        given(categoryRepository.findAllOrderByParentIdAscNullsFirstCategoryIdAsc()).willReturn(categories);

        // when
        List<CategoryDto> result = categoryService.findAll();

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("create 서비스 테스트")
    void createTest() {
        // given
        CategoryCreateRequest req = new CategoryCreateRequest("name", 1);
        given(categoryRepository.findById(anyInt())).willReturn(Optional.of(createCategory()));

        // when
        categoryService.create(req);

        // then
        verify(categoryRepository).save(any());
    }

    @Test
    @DisplayName("delete 서비스 테스트")
    void deleteTest() {
        // given
        given(categoryRepository.findById(anyInt())).willReturn(Optional.of(createCategory()));

        // when
        categoryService.delete(anyInt());

        // then
        verify(categoryRepository).delete(any());
    }
}
