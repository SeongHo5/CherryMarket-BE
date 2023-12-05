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

    @GetMapping("/makerlist")
    public ResponseEntity<List<MakerDto>> getMakerList() {
        List<MakerDto> makerList = makerService.findAllMaker();
        return ResponseEntity.ok(makerList);
    }

    @GetMapping("/makerinfo")
    public ResponseEntity<MakerDto> getMakerInfo(@RequestParam String businessNumber) {
        MakerDto makerDto = makerService.findMakerByBusinessNumber(businessNumber);
        return ResponseEntity.ok(makerDto);
    }

    @PostMapping("/addmaker")
    public ResponseEntity<MakerDto> addMaker(final @Valid @RequestBody MakerDto makerDto) {
        makerService.saveMaker(makerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(makerDto);
    }

    @GetMapping("/delmaker")
    public ResponseEntity<List<MakerDto>> delMaker(@RequestParam Long makerId) {
        makerService.deleteMakerById(makerId);
        List<MakerDto> makerList = makerService.findAllMaker();
        return ResponseEntity.ok(makerList);
    }

    @PostMapping("/updatemaker")
    public ResponseEntity<List<MakerDto>> updateMaker(final  @Valid @RequestBody MakerDto makerDto) {
        makerService.updateMakerById(makerDto);
        List<MakerDto> makerList = makerService.findAllMaker();
        return ResponseEntity.ok(makerList);
    }
}
