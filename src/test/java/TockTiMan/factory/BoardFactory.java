package TockTiMan.factory;

import TockTiMan.entity.board.Board;
import TockTiMan.entity.board.Image;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.List;

import static TockTiMan.factory.CategoryFactory.createCategory;
import static TockTiMan.factory.ImageFactory.createImage;
import static TockTiMan.factory.UserFactory.createUser;
import static TockTiMan.factory.UserFactory.createUserWithAdminRole;

public class BoardFactory {
    public static Board createBoardWithImages(List<Image> images) {
        return new Board("title", "content", createUserWithAdminRole(), createCategory(), images);
    }

    public static Board createBoard() {
        List<Image> images = new ArrayList<>();
        images.add(createImage());
        return new Board("title", "content", createUser(), createCategory(), images);
    }
}
