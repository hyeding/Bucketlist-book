package TockTiMan.service.message;

import TockTiMan.dto.message.MessageCreateRequest;
import TockTiMan.dto.message.MessageDto;
import TockTiMan.entity.message.Message;
import TockTiMan.entity.user.User;
import TockTiMan.exception.MemberNotEqualsException;
import TockTiMan.exception.MemberNotFoundException;
import TockTiMan.exception.MessageNotFoundException;
import TockTiMan.repository.message.MessageRepository;
import TockTiMan.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    public MessageDto createMessage(User sender, MessageCreateRequest req) {
        User receiver = userRepository.findByNickname(req.getReceiverNickname()).orElseThrow(MemberNotFoundException::new);
        Message message = new Message(req.getTitle(), req.getContent(), sender, receiver);
        return MessageDto.toDto(messageRepository.save(message));
    }

    @Transactional(readOnly = true)
    public List<MessageDto> receiveMessages(User user) {

        List<MessageDto> messageDtoList = new ArrayList<>();
        List<Message> messageList = messageRepository.findAllByReceiverAndDeletedByReceiverFalseOrderByIdDesc(user);

        for (Message message : messageList) {
            messageDtoList.add(MessageDto.toDto(message));
        }
        return messageDtoList;
    }


    @Transactional(readOnly = true)
    public MessageDto receiveMessage(int id, User user) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);

        if (message.getReceiver() != user) {
            throw new MemberNotEqualsException();
        }
        if (message.isDeletedByReceiver()) {
            throw new MessageNotFoundException();
        }
        return MessageDto.toDto(message);
    }

    @Transactional(readOnly = true)
    public List<MessageDto> sendMessages(User user) {
        List<MessageDto> messageDtoList = new ArrayList<>();
        List<Message> messageList = messageRepository.findAllBySenderAndDeletedBySenderFalseOrderByIdDesc(user);

        for (Message message : messageList) {
            messageDtoList.add(MessageDto.toDto(message));
        }
        return messageDtoList;
    }

    @Transactional(readOnly = true)
    public MessageDto sendMessage(int id, User user) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);

        if (message.getSender() != user) {
            throw new MemberNotEqualsException();
        }

        if (message.isDeletedByReceiver()) {
            throw new MessageNotFoundException();
        }
        return MessageDto.toDto(message);
    }

    @Transactional
    public void deleteMessageByReceiver(int id, User user) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);

        if (message.getReceiver() == user) {
            message.deleteByReceiver();
        } else {
            throw new MemberNotEqualsException();
        }

        if (message.isDeletedMessage()) {
            // ??????, ????????? ?????? ????????? ??????
            messageRepository.delete(message);
        }
    }

    @Transactional
    public void deleteMessageBySender(int id, User user) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);

        if (message.getSender() == user) {
            message.deleteBySender();
        } else {
            throw new MemberNotEqualsException();
        }

        if (message.isDeletedMessage()) {
            messageRepository.delete(message);
        }
    }
}
