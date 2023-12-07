package com.cherrydev.cherrymarketbe.discount.controller;

import com.cherrydev.cherrymarketbe.discount.dto.DiscountDto;
import com.cherrydev.cherrymarketbe.discount.service.DiscountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/discount")
public class DiscountController {

    private final DiscountService discountService;

    @GetMapping("/list")
    public ResponseEntity<List<DiscountDto>> getListAll() {
        return ResponseEntity.ok(discountService.findAll());
    }

    @GetMapping("infoById")
    public ResponseEntity<DiscountDto> getInfoById(@RequestParam Long discountId) {
        return ResponseEntity.ok(discountService.findById(discountId));
    }

    @GetMapping("infoByCode")
    public ResponseEntity<DiscountDto> getInfoByCode(@RequestParam String discountCode) {
        return ResponseEntity.ok(discountService.findByCode(discountCode));
    }

    @PostMapping("/add")
    public ResponseEntity<DiscountDto> addDiscount(final @Valid @RequestBody DiscountDto discountDto) {
        discountService.save(discountDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(discountDto);
    }

    @PostMapping("/update")
    public ResponseEntity<List<DiscountDto>> updateDiscount(final @Valid @RequestBody DiscountDto discountDto) {
        discountService.updateById(discountDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(discountService.findAll());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<List<DiscountDto>> deleteDiscount(@RequestParam Long discountId) {
        discountService.deleteById(discountId);
        return ResponseEntity.status(HttpStatus.CREATED).body(discountService.findAll());
    }
}
