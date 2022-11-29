package TockTiMan.factory;

import TockTiMan.entity.category.Category;

public class CategoryFactory {
    public static Category createCategory() {
        return new Category("name", null);
    }
}
