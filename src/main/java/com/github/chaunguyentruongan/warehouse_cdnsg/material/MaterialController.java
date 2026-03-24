package com.github.chaunguyentruongan.warehouse_cdnsg.material;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class MaterialController {
    private final MaterialService materialService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MaterialRequestCreate request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(materialService.create(request));
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam Long id) {
        return ResponseEntity.ok(materialService.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(materialService.findAll());
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestParam Long id, @RequestBody MaterialRequestCreate requestCreate) {
        return ResponseEntity.status(200).body(materialService.update(id, requestCreate));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Delete materials id: " + id);
    }
}
