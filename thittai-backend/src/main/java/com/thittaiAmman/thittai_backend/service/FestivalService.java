package com.thittaiAmman.thittai_backend.service;

import com.thittaiAmman.thittai_backend.model.Festival;
import com.thittaiAmman.thittai_backend.repo.FestivalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FestivalService {

    @Autowired
    FestivalRepository festivalRepository;

    public boolean addFestvals(List<Festival> festivals) {

        if (festivals == null){
            return false;
        }

        for (Festival festival : festivals) {

            Festival addFes = new Festival();
            addFes.setFestivalName(festival.getFestivalName());
            addFes.setFestivalDate(festival.getFestivalDate());
            addFes.setFestivalDesc(festival.getFestivalDesc());
            festivalRepository.save(addFes);
        }
        return true;
    }

    public List<Festival> getFestivals() {
        return festivalRepository.findAll();
    }

    public Festival getFestById(int id) {
        return festivalRepository.findById(id).orElse(null);
    }

    public boolean updateFestival(int id, Festival festival) {

        Festival festival1 = festivalRepository.findById(id).orElse(null);
        if (festival1 == null){
            return false;
        }
        festival1.setFestivalName(festival.getFestivalName());
        festival1.setFestivalDate(festival.getFestivalDate());
        festival1.setFestivalDesc(festival.getFestivalDesc());
        festivalRepository.save(festival1);
        return true;
    }

    public boolean deleteFestival(int id) {
        if (!festivalRepository.existsById(id))
            return false;
        festivalRepository.deleteById(id);
        return true;
    }
}
