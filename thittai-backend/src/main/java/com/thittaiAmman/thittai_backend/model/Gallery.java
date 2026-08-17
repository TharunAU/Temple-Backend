package com.thittaiAmman.thittai_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String galleryName;
    private String galleryCategory;
    private String imageName;
    private String imageType;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] galleryImage;

}
