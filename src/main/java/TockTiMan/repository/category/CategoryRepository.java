package TockTiMan.repository.category;

import TockTiMan.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("SELECT c FROM Category c LEFT JOIN c.parent p ORDER BY p.id ASC NULLS FIRST, c.id ASC")
    List<Category> findAllOrderByParentIdAscNullsFirstCategoryIdAsc();
}
