package TockTiMan.factory;

import TockTiMan.entity.message.Message;

import static TockTiMan.factory.UserFactory.createUser;
import static TockTiMan.factory.UserFactory.createUser2;

public class CreateMessageFactory {
    public static Message createMessage() {
        return new Message("title", "content", createUser(), createUser2());
    }
}
