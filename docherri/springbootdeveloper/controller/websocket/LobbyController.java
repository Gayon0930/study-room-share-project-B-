package me.docherri.springbootdeveloper.controller.websocket;

import me.docherri.springbootdeveloper.domain.Room;
import me.docherri.springbootdeveloper.repository.RoomRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api")
public class LobbyController {

    private final RoomRepository roomRepository;

    public LobbyController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }


    @GetMapping("/rooms")
    public ResponseEntity<List<Room>> getRooms() {
        List<Room> rooms = roomRepository.findAll();
        return ResponseEntity.ok(rooms);
    }

    @PostMapping("/rooms")
    public ResponseEntity<String> createRoom(@RequestBody RoomRequest request) {
        if (request.getName() == null || request.getName().isEmpty()) {
            return ResponseEntity.badRequest().body("Room name cannot be empty");
        }
        if (roomRepository.findByName(request.getName()).isPresent()) {
            return ResponseEntity.badRequest().body("Room already exists");
        }

        Room newRoom = new Room();
        newRoom.setName(request.getName());
        roomRepository.save(newRoom);

        return ResponseEntity.ok("Room created successfully");
    }

    // 방 입장 메서드 추가
    @PostMapping("/enter-room")
    public ResponseEntity<String> enterRoom(@RequestBody RoomRequest request) {
        try {
            if (request.getName() == null || request.getName().isEmpty()) {
                return ResponseEntity.badRequest().body("Room name cannot be empty");
            }

            List<Room> rooms = roomRepository.findAllByName(request.getName());
            if (rooms.isEmpty()) {
                return ResponseEntity.status(404).body("Room not found: " + request.getName());
            } else if (rooms.size() > 1) {
                return ResponseEntity.status(409).body("Multiple rooms found with the same name: " + request.getName());
            } else {
                return ResponseEntity.ok("Successfully entered the room: " + request.getName());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("An unexpected error occurred: " + ex.getMessage());
        }
    }


}