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
    public ResponseEntity<List<MakerDto>> getMakerList() {
        return ResponseEntity.ok(makerService.findAll());
    }

    @GetMapping("/info")
    public ResponseEntity<MakerDto> getMakerInfo(@RequestParam String businessNumber) {
        return ResponseEntity.ok(makerService.findByBusinessNumber(businessNumber));
    }

    @PostMapping("/add")
    public ResponseEntity<MakerDto> addMaker(final @Valid @RequestBody MakerDto makerDto) {
        makerService.save(makerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(makerDto);
    }

    @GetMapping("/delete")
    public ResponseEntity<List<MakerDto>> delMaker(@RequestParam Long makerId) {
        makerService.deleteById(makerId);
        return ResponseEntity.ok(makerService.findAll());
    }

    @PostMapping("/update")
    public ResponseEntity<List<MakerDto>> updateMaker(final  @Valid @RequestBody MakerDto makerDto) {
        makerService.updateById(makerDto);
        return ResponseEntity.ok(makerService.findAll());
    }
}
