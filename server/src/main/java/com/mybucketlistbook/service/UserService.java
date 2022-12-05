package com.mybucketlistbook.service;

import com.mybucketlistbook.dto.ChangeNicknameDto;
import com.mybucketlistbook.dto.ChangePasswordDto;
import com.mybucketlistbook.dto.PasswordDto;
import com.mybucketlistbook.dto.SignUpDto;
import com.mybucketlistbook.entity.User;
import com.mybucketlistbook.error.exception.custom.EmailDuplicateException;
import com.mybucketlistbook.error.exception.custom.NicknameDuplicateException;
import com.mybucketlistbook.error.exception.custom.UserNotFoundException;
import com.mybucketlistbook.repository.EntryRepositry;
import com.mybucketlistbook.repository.UserRepository;
import com.mybucketlistbook.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.net.UnknownHostException;
import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EntryRepositry entryRepositry;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Value("${custom.imgcnt}")
    private Integer imgCnt;

    @Autowired
    public UserService(UserRepository userRepository, EntryRepositry entryRepositry, PasswordEncoder passwordEncoder, MailService mailService) {
        this.userRepository = userRepository;
        this.entryRepositry = entryRepositry;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @Transactional
    public User signup(final SignUpDto signUpDto) throws MessagingException, UnknownHostException {
        if (userRepository.findOneWithRolesByEmail(signUpDto.getEmail()).orElse(null) != null) {
            throw new EmailDuplicateException(signUpDto);
        }

        Role role = Role.builder()
                .roleId(1)
                .roleName("ROLE_USER")
                .build();

        String authKey = mailService.sendAuthMail(signUpDto.getEmail(), signUpDto.getNickname());
        int imgNum = (int) (Math.random()*25 + 1);

        User user = User.builder()
                .email(signUpDto.getEmail())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .nickname(signUpDto.getNickname())
                .roles(Collections.singleton(role))
                .isTutorialFinished(false)
                .img(Integer.toString(imgNum))
                .authKey(authKey)
                .authStatus(false)
                .build();

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithRoles(final String email) {
        return userRepository.findOneWithRolesByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<User> getMyUserWithRoles() {
        return SecurityUtil.getCurrentEmail().flatMap(userRepository::findOneWithRolesByEmail);
    }

    @Transactional(readOnly = true)
    public List<User> getAllUserWithRoles() {
        return userRepository.findByRoles_RoleNameNot("ROLE_ADMIN");
    }

    @Transactional
    public User updateAuthStatus(Map<String, String> map) {
        String email = map.get("email");
        String authKey = map.get("authKey");
        User updateUser = userRepository.findOneWithRolesByEmailAndAuthKey(email, authKey).orElse(null);

        if (updateUser == null) {
            throw new UserNotFoundException(email);
        }

        updateUser.setAuthStatus(true);
        return userRepository.save(updateUser);
    }

    @Transactional(readOnly = true)
    public void checkDuplicateNickname(String nickname) {
        if(userRepository.findAllByNickname(nickname).size() > 0) {
            throw new NicknameDuplicateException(nickname);
        }
    }

    @Transactional
    public void deleteUser() {
        User user = SecurityUtil.getCurrentEmail().flatMap(userRepository::findOneWithRolesByEmail).orElse(null);

        if(user == null) {
            throw new UserNotFoundException(SecurityUtil.getCurrentEmail().orElse(""));
        }

        entryRepositry.deleteByUserId(user.getUserId());

        if(userRepository.deleteByEmail(SecurityUtil.getCurrentEmail().orElse("")) == 0) {
            throw new UserNotFoundException(SecurityUtil.getCurrentEmail().orElse(""));
        }
    }

    @Transactional
    public void deleteUser(final String email) {
        User user = SecurityUtil.getCurrentEmail().flatMap(userRepository::findOneWithRolesByEmail).orElse(null);

        if(user == null) {
            throw new UserNotFoundException(SecurityUtil.getCurrentEmail().orElse(""));
        }

        User deleteUser = userRepository.findOneWithRolesByEmail(email).orElse(null);

        if(deleteUser == null) {
            throw new UserNotFoundException(SecurityUtil.getCurrentEmail().orElse(""));
        }

        entryRepositry.deleteByUserId(deleteUser.getUserId());

        if(userRepository.deleteByEmail(email) == 0) {
            throw new UserNotFoundException(SecurityUtil.getCurrentEmail().orElse(""));
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Boolean> checkPassword(PasswordDto passwordDto) {
        User user = userRepository.findOneWithRolesByEmail(SecurityUtil.getCurrentEmail().orElse("")).orElse(null);

        if(user == null) {
            throw new UserNotFoundException(SecurityUtil.getCurrentEmail().orElse(null));
        }

        Map<String, Boolean> check = new HashMap<>();

        if(!passwordEncoder.matches(passwordDto.getPassword(), user.getPassword())) {
            check.put("check", false);
        } else {
            check.put("check", true);
        }

        return check;
    }

    @Transactional
    public void updatePassword(ChangePasswordDto changePasswordDto) {
        User updateUser = userRepository.findOneWithRolesByEmail(SecurityUtil.getCurrentEmail().orElse("")).orElse(null);

        if(updateUser == null) {
            throw new UserNotFoundException(SecurityUtil.getCurrentEmail().orElse(null));
        }

        updateUser.setPassword(passwordEncoder.encode(changePasswordDto.getChangePassword()));

        userRepository.save(updateUser);
    }

    @Transactional
    public void updateNickname(ChangeNicknameDto changeNicknameDto) {
        User updateUser = userRepository.findOneWithRolesByEmail(SecurityUtil.getCurrentEmail().orElse(null)).orElse(null);

        if(updateUser == null) {
            throw new UserNotFoundException(SecurityUtil.getCurrentEmail().orElse(null));
        }

        updateUser.setNickname(changeNicknameDto.getChangeNickname());

        userRepository.save(updateUser);
    }

    @Transactional
    public void updateProfileImage(String imgNum) {
        User updateUser = userRepository.findOneWithRolesByEmail(SecurityUtil.getCurrentEmail().orElse(null)).orElse(null);

        if(updateUser == null) {
            throw new UserNotFoundException(SecurityUtil.getCurrentEmail().orElse(null));
        }

        updateUser.setImg(imgNum);

        userRepository.save(updateUser);
    }

}
