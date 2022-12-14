package TockTiMan.controller.user;

import TockTiMan.dto.user.UserDto;
import TockTiMan.entity.user.User;
import TockTiMan.exception.MemberNotFoundException;
import TockTiMan.repository.user.UserRepository;
import TockTiMan.response.Response;
import TockTiMan.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Api(value = "User Controller", tags = "User")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @ApiOperation(value = "전체 회원 조회", notes = "전체 회원을 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    public Response findAllUsers() {
        return Response.success(userService.findAllUsers());
    }

    @ApiOperation(value = "개별 회원 조회", notes = "개별 회원을 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{id}")
    public Response findUser(@ApiParam(value = "User ID", required = true) @PathVariable int id) {
        return Response.success(userService.findUser(id));
    }

    @ApiOperation(value = "회원 정보 수정", notes = "회원의 정보를 수정")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/users/{id}")
    public Response editUserInfo(@ApiParam(value = "User ID", required = true) @PathVariable int id, @RequestBody UserDto userDto) {
        return Response.success(userService.editUserInfo(id, userDto));
    }

    @ApiOperation(value = "회원 탈퇴", notes = "회원을 탈퇴 시킴")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/users/{id}")
    public Response deleteUserInfo(@ApiParam(value = "User ID", required = true) @PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);

        userService.deleteUserInfo(user, id);
        return Response.success();
    }

    @ApiOperation(value = "즐겨찾기 한 글 조회", notes = "유저가 즐겨찾기 한 게시글들 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/favorites")
    public Response findFavorites() {
//        @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);

        return Response.success(userService.findFavorites(user));
    }
}
