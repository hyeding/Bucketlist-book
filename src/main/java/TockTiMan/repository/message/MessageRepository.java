package TockTiMan.repository.message;

import TockTiMan.entity.message.Message;
import TockTiMan.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findAllByReceiverAndDeletedByReceiverFalseOrderByIdDesc(User user);
    List<Message> findAllBySenderAndDeletedBySenderFalseOrderByIdDesc(User user);
}
