package com.cherrydev.cherrymarketbe.server.application.maker.controller;

import com.cherrydev.cherrymarketbe.server.application.maker.service.MakerService;
import com.cherrydev.cherrymarketbe.server.domain.maker.dto.response.MakerInfo;
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
    public ResponseEntity<List<MakerInfo>> getMakerList(@RequestParam(required = false) String sortBy) {
        return ResponseEntity.ok(makerService.findAll(sortBy));
    }

    @GetMapping("/info/makerName")
    public ResponseEntity<List<MakerInfo>> getMakerInfoByName(@RequestParam String makerName) {
        return ResponseEntity.ok(makerService.findByName(makerName));
    }

    @GetMapping("/info/makerId")
    public ResponseEntity<MakerInfo> getMakerInfoById(@RequestParam Long makerId) {
        return ResponseEntity.ok(makerService.findById(makerId));
    }

    @GetMapping("/info/businessNumber")
    public ResponseEntity<MakerInfo> getMakerInfoByBusinessNumber(@RequestParam String businessNumber) {
        return ResponseEntity.ok(makerService.findByBusinessNumber(businessNumber));
    }

    @PostMapping("/add")
    public ResponseEntity<MakerInfo> save(final @Valid @RequestBody MakerInfo makerInfo) {
        makerService.save(makerInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(makerInfo);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<List<MakerInfo>> delete(@RequestParam Long makerId, @RequestParam(required = false) String sortBy) {
        makerService.deleteById(makerId);
        return ResponseEntity.ok(makerService.findAll(sortBy));
    }

    @PatchMapping("/update")
    public ResponseEntity<List<MakerInfo>> update(final @Valid @RequestBody MakerInfo makerInfo, @RequestParam(required = false) String sortBy) {
        makerService.updateById(makerInfo);
        return ResponseEntity.ok(makerService.findAll(sortBy));
    }
}
