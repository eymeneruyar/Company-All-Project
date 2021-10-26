package companyAll_MVC.dto;

import companyAll_MVC.entities.Gallery;
import companyAll_MVC.entities.GalleryCategory;
import companyAll_MVC.entities.Likes;
import companyAll_MVC.repositories._jpa.GalleryCategoryRepository;
import companyAll_MVC.repositories._jpa.GalleryRepository;
import companyAll_MVC.utils.Check;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class GalleryDto {

    final GalleryRepository galleryRepository;
    final GalleryCategoryRepository galleryCategoryRepository;

    public GalleryDto(GalleryRepository galleryRepository, GalleryCategoryRepository galleryCategoryRepository) {
        this.galleryRepository = galleryRepository;
        this.galleryCategoryRepository = galleryCategoryRepository;
    }

    // gallery-category list
    public Map<Check, Object> list() {
        Map<Check, Object> hm = new LinkedHashMap<>();
        hm.put(Check.status, true);
        List<GalleryCategory> ls = galleryCategoryRepository.findAll();
        hm.put(Check.result, ls);
        return hm;
    }

    //galleryListByCategoryId
    public Map<Check, Object> listByCategoryId(Integer id) {
        Map<Check, Object> hm = new LinkedHashMap<>();
        hm.put(Check.status, true);
        List<Gallery> ls = galleryRepository.findByGalleryCategory_IdEquals(id);
        hm.put(Check.result, ls);
        return hm;
    }

}
