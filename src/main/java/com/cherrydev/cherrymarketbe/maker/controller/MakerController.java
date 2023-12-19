package com.cherrydev.cherrymarketbe.maker.controller;

import com.cherrydev.cherrymarketbe.maker.dto.MakerDto;
import com.cherrydev.cherrymarketbe.maker.service.MakerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/maker")
public class MakerController {

    private final MakerService makerService;

    @GetMapping("/list")
    public ResponseEntity<List<MakerDto>> getMakerList(@RequestParam(required = false) String sortBy) {
        return ResponseEntity.ok(makerService.findAll(sortBy));
    }

    @GetMapping("/info/makerName")
    public ResponseEntity<List<MakerDto>> getMakerInfoByName(@RequestParam String makerName) {
        return ResponseEntity.ok(makerService.findByName(makerName));
    }

    @GetMapping("/info/makerId")
    public ResponseEntity<MakerDto> getMakerInfoById(@RequestParam Long makerId) {
        return ResponseEntity.ok(makerService.findById(makerId));
    }

    @GetMapping("/info/businessNumber")
    public ResponseEntity<MakerDto> getMakerInfoByBusinessNumber(@RequestParam String businessNumber) {
        return ResponseEntity.ok(makerService.findByBusinessNumber(businessNumber));
    }

    @PostMapping("/add")
    public ResponseEntity<MakerDto> save(final @Valid @RequestBody MakerDto makerDto) {
        makerService.save(makerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(makerDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<List<MakerDto>> delete(@RequestParam Long makerId, @RequestParam(required = false) String sortBy) {
        makerService.deleteById(makerId);
        return ResponseEntity.ok(makerService.findAll(sortBy));
    }

    @PatchMapping("/update")
    public ResponseEntity<List<MakerDto>> update(final @Valid @RequestBody MakerDto makerDto, @RequestParam(required = false) String sortBy) {
        makerService.updateById(makerDto);
        return ResponseEntity.ok(makerService.findAll(sortBy));
    }
}
