package com.cherrydev.cherrymarketbe.server.application.discount.controller;

import com.cherrydev.cherrymarketbe.server.application.discount.service.DiscountService;
import com.cherrydev.cherrymarketbe.server.domain.discount.dto.request.RequestDiscount;
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
    public ResponseEntity<List<RequestDiscount>> getListAll() {
        return ResponseEntity.ok(discountService.findAll());
    }

    @GetMapping("/infoById")
    public ResponseEntity<RequestDiscount> getInfoById(@RequestParam Long discountId) {
        return ResponseEntity.ok(discountService.findById(discountId));
    }

    @GetMapping("/infoByCode")
    public ResponseEntity<List<RequestDiscount>> getInfoByCode(@RequestParam String discountCode) {
        return ResponseEntity.ok(discountService.findByCode(discountCode));
    }

    @PostMapping("/add")
    public ResponseEntity<RequestDiscount> addDiscount(final @Valid @RequestBody RequestDiscount requestDiscount) {
        discountService.save(requestDiscount);
        return ResponseEntity.status(HttpStatus.CREATED).body(requestDiscount);
    }

    @PatchMapping("/update")
    public ResponseEntity<List<RequestDiscount>> updateDiscount(final @Valid @RequestBody RequestDiscount requestDiscount) {
        discountService.updateById(requestDiscount);
        return ResponseEntity.status(HttpStatus.CREATED).body(discountService.findAll());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<List<RequestDiscount>> deleteDiscount(@RequestParam Long discountId) {
        discountService.deleteById(discountId);
        return ResponseEntity.status(HttpStatus.CREATED).body(discountService.findAll());
    }
}
