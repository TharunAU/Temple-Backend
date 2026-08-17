package com.thittaiAmman.thittai_backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thittaiAmman.thittai_backend.model.Gallery;
import com.thittaiAmman.thittai_backend.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class GalleryController {

    @Autowired
    GalleryService galleryService;

    @PostMapping("/addToGallery")
    public ResponseEntity<?> addGallery(@RequestPart String galleriesJson,
                                        @RequestPart List<MultipartFile> galleryImage) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        List<Gallery> galleries =
                mapper.readValue(
                        galleriesJson,
                        new TypeReference<List<Gallery>>() {}
                );

        boolean message = galleryService.saveGallery(galleries,galleryImage);
        if (!message){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Unable to save gallery");
        }
        return ResponseEntity.ok("Added Successfully");
    }

    @GetMapping("/gallery")
    public ResponseEntity<List<Gallery>> getGallery(){
        return ResponseEntity.ok(galleryService.getGallery());
    }

    @GetMapping("/gallery/image/{id}")
    public ResponseEntity<?> getImagesById(@PathVariable Integer id) {
        Gallery gallery = galleryService.getImageById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(gallery.getImageType()))
                .body(gallery.getGalleryImage());
    }

    @DeleteMapping("/gallery/image/{id}")
    public ResponseEntity<String> deleteGalleryById(@PathVariable int id){
        boolean result = galleryService.deleteImageById(id);
        ResponseEntity<String> response;
        if (!result)
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to Perform Operation");
        else
            response = ResponseEntity.ok().body("Gallery Deleted Successfully");
        return response;
    }

    @PutMapping("/gallery/image/{id}")
    public ResponseEntity<String> updateGalleryById(@PathVariable int id,
                                                    @RequestPart Gallery updateGallery,
                                                    @RequestPart MultipartFile updateImageData) throws Exception {
        boolean result = galleryService.updateById(id,updateGallery,updateImageData);
        ResponseEntity<String> response;
        if(!result)
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to Perform Operation");
        else
            response = ResponseEntity.ok().body("Updated Successfully");
        return response;
    }


}
