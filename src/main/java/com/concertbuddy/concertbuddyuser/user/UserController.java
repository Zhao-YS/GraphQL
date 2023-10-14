package com.concertbuddy.concertbuddyuser.user;

import com.concertbuddy.concertbuddyuser.song.Song;
import com.concertbuddy.concertbuddyuser.song.SongController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(path="{userId}")
    public User getUserById(@PathVariable("userId") UUID userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    public void registerNewUser(@RequestBody User user) {
        userService.addNewUser(user);
    }

    @PutMapping
    public void updateUser(@RequestBody User user) {
        userService.addNewUser(user);
    }

    @DeleteMapping(path="{userId}")
    public void deleteUser(@PathVariable("userId") UUID userId) {
        userService.deleteUser(userId);
    }

    @GetMapping(path="{userId}/songs")
    public ResponseEntity<CollectionModel<EntityModel<Song>>> getUserSongsById(@PathVariable("userId") UUID userId) {
        User user = userService.getUserById(userId);
        List<EntityModel<Song>> songsWithLinks = user.getSongs().stream().map(
                song -> EntityModel.of(
                        song,
                        linkTo(methodOn(SongController.class).getSongById(song.getId())).withSelfRel()
                )
        ).toList();
        return ResponseEntity.ok(CollectionModel.of(songsWithLinks));
    }
}
