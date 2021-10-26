package companyAll_MVC.restcontroller;

import companyAll_MVC.dto.GalleryDto;
import companyAll_MVC.repositories._jpa.GalleryCategoryRepository;
import companyAll_MVC.repositories._jpa.GalleryRepository;
import companyAll_MVC.utils.Check;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/gallery")
//@Api(value = "GalleryRestController",authorizations = {@Authorization(value = "basicAuth")})
public class GalleryRestController {

    final GalleryDto galleryDto;
    final GalleryRepository galleryRepository;
    final GalleryCategoryRepository galleryCategoryRepository;

    public GalleryRestController(GalleryDto galleryDto, GalleryRepository galleryRepository, GalleryCategoryRepository galleryCategoryRepository) {
        this.galleryDto = galleryDto;
        this.galleryRepository = galleryRepository;
        this.galleryCategoryRepository = galleryCategoryRepository;
    }

    //gallery-category list
    @GetMapping("/listCategory")
    public Map<Check, Object> list() {
        return galleryDto.list();
    }

    //galleryListByCategoryId
    @GetMapping("/listGallery/{id}")
    public Map<Check, Object> listByCategoryId(@PathVariable Integer id) {
        return galleryDto.listByCategoryId(id);
    }

}
