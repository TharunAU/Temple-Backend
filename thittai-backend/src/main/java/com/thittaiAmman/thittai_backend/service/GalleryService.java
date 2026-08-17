package com.thittaiAmman.thittai_backend.service;

import com.thittaiAmman.thittai_backend.model.Gallery;
import com.thittaiAmman.thittai_backend.repo.GalleryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class GalleryService {

    @Autowired
    GalleryRepository galleryRepo;

    @Transactional
    public boolean saveGallery(List<Gallery> galleries, List<MultipartFile> galleryImage) throws IOException {

        if (galleries == null || galleryImage == null){
            return false;
        }
        if (galleries.size() != galleryImage.size()) {
            return false;
        }
        for (int i=0;i < galleries.size();i++){

            Gallery request = galleries.get(i);
            MultipartFile image = galleryImage.get(i);

            Gallery gallery = new Gallery();

            gallery.setGalleryName(request.getGalleryName());
            gallery.setGalleryCategory(request.getGalleryCategory());

            gallery.setGalleryImage(image.getBytes());
            gallery.setImageName(image.getOriginalFilename());
            gallery.setImageType(image.getContentType());

            galleryRepo.save(gallery);

        }
        return true;
    }

    public List<Gallery> getGallery() {
        return galleryRepo.findAll();
    }

    public Gallery getImageById(Integer id) {

        return galleryRepo.findById(id).orElseThrow();
    }

    public boolean deleteImageById(int id) {
        if (!galleryRepo.existsById(id)){
            return false;
        }
        galleryRepo.deleteById(id);
        return true;
    }

    public boolean updateById(int id, Gallery updateGallery, MultipartFile updateImageData) throws Exception{
        Gallery exist = galleryRepo.findById(id).orElse(null);
        if(exist!=null){
            exist.setGalleryCategory(updateGallery.getGalleryCategory());
            exist.setGalleryName(updateGallery.getGalleryName());
            exist.setGalleryImage(updateImageData.getBytes());
            exist.setImageType(updateImageData.getContentType());
            exist.setImageName(updateImageData.getOriginalFilename());
            galleryRepo.save(exist);
            return true;
        }
        return false;
    }
}
