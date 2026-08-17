package com.thittaiAmman.thittai_backend.controller;

import com.thittaiAmman.thittai_backend.model.Festival;
import com.thittaiAmman.thittai_backend.repo.FestivalRepository;
import com.thittaiAmman.thittai_backend.service.FestivalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FestivalController {

    @Autowired
    FestivalService festivalService;

    @PostMapping("/addFestivals")
    public ResponseEntity<String> addFestivals(@RequestBody List<Festival> festivals){
        boolean result = festivalService.addFestvals(festivals);
        if(!result){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to perform add Operation");
        }
        return ResponseEntity.ok().body("Added Successfully");
    }

    @GetMapping("getFestivals")
    public ResponseEntity<List<Festival>> getFestives(){
        List<Festival> festivals = festivalService.getFestivals();
        if (festivals!=null)
            return ResponseEntity.ok(festivals);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/getFestivals/{id}")
    public ResponseEntity<Festival> getFesById(@PathVariable int id){
        Festival festival = festivalService.getFestById(id);
        if (festival!=null){
            return ResponseEntity.ok(festival);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PutMapping("/updateFest/{id}")
    public ResponseEntity<String> updateFestival(@PathVariable int id,@RequestBody Festival festival){
        boolean res = festivalService.updateFestival(id,festival);
        if (!res){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to perform update operation");
        }
        return ResponseEntity.ok().body("Updated Successfully");
    }

    @DeleteMapping("/deleteFestival/{id}")
    public ResponseEntity<String> deleteFestival(@PathVariable int id){
        boolean res = festivalService.deleteFestival(id);
        if (!res)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to Perform delete Operation");
        return ResponseEntity.ok("Festival Deleted Successfully");
    }

}
