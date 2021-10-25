package companyAll_MVC.controllers;

import companyAll_MVC.documents.ElasticGallery;
import companyAll_MVC.documents.ElasticGalleryCategory;
import companyAll_MVC.entities.Gallery;
import companyAll_MVC.entities.GalleryCategory;
import companyAll_MVC.repositories._elastic.ElasticGalleryCategoryRepository;
import companyAll_MVC.repositories._elastic.ElasticGalleryRepository;
import companyAll_MVC.repositories._jpa.GalleryCategoryRepository;
import companyAll_MVC.repositories._jpa.GalleryRepository;
import companyAll_MVC.utils.Check;
import companyAll_MVC.utils.Util;
import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/gallery")
public class GalleryController {


    int chosenId = 0;
    int sendCount = 0;
    int sendSuccessCount = 0;
    String errorMessage = "";

    final ElasticGalleryRepository elasticGalleryRepository;
    final GalleryRepository galleryRepository;
    final ElasticGalleryCategoryRepository elasticGalleryCategoryRepository;
    final GalleryCategoryRepository galleryCategoryRepository;

    public GalleryController(ElasticGalleryRepository elasticGalleryRepository, GalleryRepository galleryRepository, ElasticGalleryCategoryRepository elasticGalleryCategoryRepository, GalleryCategoryRepository galleryCategoryRepository) {
        this.elasticGalleryRepository = elasticGalleryRepository;
        this.galleryRepository = galleryRepository;
        this.elasticGalleryCategoryRepository = elasticGalleryCategoryRepository;
        this.galleryCategoryRepository = galleryCategoryRepository;
    }


    @GetMapping("")
    public String gallery() {
        return "galleries";
    }

    // Add Category
    @PostMapping("/categoryAdd")
    @ResponseBody
    public Map<Check, Object> categoryAdd(@RequestBody @Valid GalleryCategory galleryCategory, BindingResult bindingResult) {
        Map<Check, Object> map = new LinkedHashMap<>();
        if (!bindingResult.hasErrors()) {
            if (galleryCategory.getId() != null) {
                Optional<GalleryCategory> optionalGalleryCategory = galleryCategoryRepository.findById(galleryCategory.getId());
                if (optionalGalleryCategory.isPresent()) {
                    try {
                        GalleryCategory galleryCategory1 = galleryCategoryRepository.saveAndFlush(galleryCategory);
                        ElasticGalleryCategory elasticGalleryCategory = new ElasticGalleryCategory();
                        elasticGalleryCategory.setId(Integer.toString(galleryCategory1.getId()));
                        elasticGalleryCategory.setCategoryId(galleryCategory1.getId());
                        elasticGalleryCategory.setNo(Long.toString(galleryCategory1.getNo()));
                        elasticGalleryCategory.setName(galleryCategory1.getName());
                        elasticGalleryCategory.setStatus(galleryCategory1.getStatus());
                        elasticGalleryCategoryRepository.save(elasticGalleryCategory);

                        map.put(Check.status, true);
                        map.put(Check.message, "Updated operations success!");
                        map.put(Check.result, galleryCategory);
                    } catch (Exception ex) {
                        System.err.println("Elastic gallery category update" + ex);
                    }
                }
            } else {
                try {
                    GalleryCategory galleryCategory1 = galleryCategoryRepository.saveAndFlush(galleryCategory);
                    map.put(Check.status, true);
                    map.put(Check.message, "Adding of Gallery Category Operations Successful!");
                    map.put(Check.result, galleryCategory);

                    ElasticGalleryCategory elasticGalleryCategory = new ElasticGalleryCategory();
                    elasticGalleryCategory.setId(Integer.toString(galleryCategory1.getId()));
                    elasticGalleryCategory.setCategoryId(galleryCategory.getId());
                    elasticGalleryCategory.setNo(Long.toString(galleryCategory.getNo()));
                    elasticGalleryCategory.setName(galleryCategory.getName());
                    elasticGalleryCategory.setStatus(galleryCategory.getStatus());
                    elasticGalleryCategoryRepository.save(elasticGalleryCategory);
                } catch (Exception ex) {
                    map.put(Check.status, false);
                    if (ex.toString().contains("constraint")) {
                        String error = "This gallery category name has alredy been registered!";
                        Util.logger(error, GalleryCategory.class);
                        map.put(Check.message, error);
                    }
                }
            }
        } else {
            map.put(Check.status, false);
            map.put(Check.errors, Util.errors(bindingResult));
        }
        return map;
    }

    //List of Gallery Category with pagination
    @ResponseBody
    @GetMapping("/categoryList/{stShowNumber}/{stPageNo}")
    public Map<Check, Object> categoryList(@PathVariable String stShowNumber, @PathVariable String stPageNo) {
        Map<Check, Object> map = new LinkedHashMap<>();
        try {
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo, showNumber);
            Page<ElasticGalleryCategory> categoryPage = elasticGalleryCategoryRepository.findByOrderByIdAsc(pageable);
            map.put(Check.status, true);
            map.put(Check.totalPage, categoryPage.getTotalPages());
            map.put(Check.message, "Content listing on page " + (pageNo + 1) + " is successful");
            map.put(Check.result, categoryPage.getContent());
        } catch (Exception e) {
            map.put(Check.status, false);
            String error = "An error occurred during the operation!";
            System.err.println(e);
            Util.logger(error, GalleryCategory.class);
            map.put(Check.message, error);
        }
        return map;
    }

    // Delete Category
    @ResponseBody
    @DeleteMapping("/categoryDelete/{stId}")
    public Map<Check, Object> delete(@PathVariable String stId) {
        Map<Check, Object> map = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            Optional<GalleryCategory> optionalGalleryCategory = galleryCategoryRepository.findById(id);
            if (optionalGalleryCategory.isPresent()) {
                ElasticGalleryCategory elasticGalleryCategory = elasticGalleryCategoryRepository.findById(id).get();
                galleryCategoryRepository.deleteById(id);
                elasticGalleryCategoryRepository.deleteById(elasticGalleryCategory.getId());
                map.put(Check.status, true);
                map.put(Check.message, "Data has been deleted!");
                map.put(Check.result, optionalGalleryCategory.get());
            } else {
                String error = "Gallery Category not found";
                map.put(Check.status, false);
                map.put(Check.message, error);
                Util.logger(error, GalleryCategory.class);
            }
        } catch (Exception e) {
            String error = "An error occurred during the delete operation";
            map.put(Check.status, false);
            map.put(Check.message, error);
            Util.logger(error, GalleryCategory.class);
            System.err.println(e);
        }
        return map;
    }

    // ElasticSearch Category
    @ResponseBody
    @GetMapping("/searchGalleryCategory/{data}/{stPageNo}/{stShowNumber}")
    public Map<Check, Object> searchGalleryCategory(@PathVariable String data, @PathVariable String stPageNo, @PathVariable String stShowNumber) {
        Map<Check, Object> map = new LinkedHashMap<>();
        try {
            System.out.println("burada :");
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo, showNumber);
            System.out.println("burada pageable:" + pageable);
            System.out.println("burada data:" + data);
            Page<ElasticGalleryCategory> searchPage = elasticGalleryCategoryRepository.findBySearchData(data, pageable);
            System.out.println("burada searchPage:" + searchPage);

            List<ElasticGalleryCategory> elasticGalleryCategoryList = searchPage.getContent();
            System.out.println("burada elasticGalleryCategoryList:" + elasticGalleryCategoryList);

            int totalData = elasticGalleryCategoryList.size(); //for total data in table
            System.out.println("burada totalData:" + totalData);
            if (totalData > 0) {
                map.put(Check.status, true);
                map.put(Check.totalPage, searchPage.getTotalPages());
                map.put(Check.message, "Search operation success!");
                map.put(Check.result, elasticGalleryCategoryList);
            } else {
                map.put(Check.status, false);
                map.put(Check.message, "Could not find a result for your search!");
            }
        } catch (NumberFormatException e) {
            map.put(Check.status, false);
            String error = "An error occurred during the search operation!";
            System.err.println(e);
            Util.logger(error, GalleryCategory.class);
            map.put(Check.message, error);
        }
        return map;
    }

    // Category List
    @ResponseBody
    @GetMapping("/categoryList")
    public Map<Check, Object> categoryList() {
        Map<Check, Object> map = new LinkedHashMap<>();
        map.put(Check.status, true);
        map.put(Check.message, "Gallery category list operations success!");
        map.put(Check.result, galleryCategoryRepository.findAll());
        return map;
    }

    // Gallery Add - Update
    @PostMapping("/add")
    @ResponseBody
    public Map<Check, Object> galleryAdd(@RequestBody @Valid Gallery gallery, BindingResult bindingResult) {
        System.out.println("okey");
        Map<Check, Object> map = new LinkedHashMap<>();
        if (!bindingResult.hasErrors()) {
            System.out.println("hata yok");
            if (gallery.getId() != null) {
                System.out.println(gallery.getId());
                Optional<Gallery> optionalGallery = galleryRepository.findById(gallery.getId());
                if (optionalGallery.isPresent()) {
                    try {
                        Gallery gallery1 = galleryRepository.saveAndFlush(gallery);
                        System.out.println("gallery1: " + gallery1);
                        ElasticGallery elasticGallery = new ElasticGallery();
                        elasticGallery.setId(Integer.toString(gallery1.getId()));
                        elasticGallery.setNo(Long.toString(gallery1.getNo()));
                        elasticGallery.setTitle(gallery1.getTitle());
                        elasticGallery.setDescription(gallery1.getDescription());
                        elasticGallery.setStatus(gallery1.getStatus());
                        elasticGallery.setDate(gallery1.getDate());
                        elasticGallery.setCategoryName(gallery1.getGalleryCategory().getName());
                        elasticGallery.setCategoryId(Integer.toString(gallery1.getGalleryCategory().getId()));
                        elasticGalleryRepository.save(elasticGallery);

                        map.put(Check.status, true);
                        map.put(Check.message, "Updated operations success!");
                        map.put(Check.result, gallery);
                    } catch (Exception ex) {
                        System.err.println("Elastic Gallery Update: " + ex);
                    }
                }
            } else {
                try {
                    System.out.println("galleryAddController");
                    Gallery gallery1 = galleryRepository.saveAndFlush(gallery);
                    map.put(Check.status, true);
                    map.put(Check.message, "Adding of Gallery Operations Successful!");
                    map.put(Check.result, gallery);
                    ElasticGallery elasticGallery = new ElasticGallery();
                    elasticGallery.setId(Integer.toString(gallery1.getId()));
                    elasticGallery.setNo(Long.toString(gallery1.getNo()));
                    elasticGallery.setTitle(gallery1.getTitle());
                    elasticGallery.setDescription(gallery1.getDescription());
                    elasticGallery.setStatus(gallery1.getStatus());
                    elasticGallery.setDate(gallery1.getDate());
                    elasticGallery.setCategoryName(gallery1.getGalleryCategory().getName());
                    elasticGallery.setCategoryId(Integer.toString(gallery1.getGalleryCategory().getId()));
                    elasticGalleryRepository.save(elasticGallery);
                } catch (Exception ex) {
                    System.out.println("galleryAddControllerError");

                    map.put(Check.status, false);
                    if (ex.toString().contains("constraint")) {
                        String error = "This gallery name has already been registered!";
                        Util.logger(error, Gallery.class);
                        map.put(Check.message, error);
                    }
                }
            }

        } else {
            map.put(Check.status, false);
            map.put(Check.errors, Util.errors(bindingResult));
        }
        return map;
    }

    //List of Gallery with pagination
    @ResponseBody
    @GetMapping("/gallerylist/{stShowNumber}/{stPageNo}")
    public Map<Check, Object> listGallery(@PathVariable String stShowNumber, @PathVariable String stPageNo) {
        Map<Check, Object> map = new LinkedHashMap<>();
        //System.out.println(stShowNumber + " " + stPageNo);
        try {
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo, showNumber);
            Page<ElasticGallery> galleryPage = elasticGalleryRepository.findByOrderByIdAsc(pageable);
            map.put(Check.status, true);
            map.put(Check.totalPage, galleryPage.getTotalPages());
            map.put(Check.message, "Gallery listing on page " + (pageNo + 1) + " is successful");
            map.put(Check.result, galleryPage.getContent());
        } catch (Exception e) {
            map.put(Check.status, false);
            String error = "An error occurred during the operation!";
            System.err.println(e);
            Util.logger(error, Gallery.class);
            map.put(Check.message, error);
        }
        return map;
    }


    // Get id data from Choosen Gallery
    @ResponseBody
    @GetMapping("/chosenId/{stId}")
    public int chosenId(@PathVariable String stId) {
        try {
            int id = Integer.parseInt(stId);
            Optional<Gallery> optionalGallery = galleryRepository.findById(id);
            if (optionalGallery.isPresent()) {
                chosenId = id;
            }
        } catch (Exception e) {
            System.err.println("Chosen Id Error: " + e);
        }
        System.out.println("Resim eklemek için seçilen ürünün ıd numarası: " + chosenId);
        return chosenId;
    }

    //  Add Gallery Images
    @PostMapping("/imageUpload")
    public String imageUpload(@RequestParam("imageName") MultipartFile[] files) {
        System.out.println(chosenId);
        Optional<Gallery> optionalGallery = galleryRepository.findById(chosenId);
        List<String> imageNameList = new ArrayList<>();
        File f = new File(Util.UPLOAD_DIR_GALLERY + ("" + chosenId));
        boolean isDeleted = f.delete();
        if (optionalGallery.isPresent()) {
            Gallery gallery = optionalGallery.get();
            if (files != null && files.length != 0) {
                sendCount = files.length;
                String idFolder = "" + chosenId + "/";
                File folderGallery = new File(Util.UPLOAD_DIR_GALLERY + idFolder);
                boolean status = folderGallery.mkdir();
                for (MultipartFile file : files) {
                    Map<Check, Object> imgResult = Util.imageUpload(file, Util.UPLOAD_DIR_GALLERY + idFolder);
                    imageNameList.add(imgResult.get(Check.message).toString());
                }
                gallery.setFileName(imageNameList);
                galleryRepository.saveAndFlush(gallery);
                Optional<ElasticGallery> elasticGalleryOptional = elasticGalleryRepository.findById(Integer.toString(gallery.getId()));
                if (elasticGalleryOptional.isPresent()) {
                    ElasticGallery elasticGallery = elasticGalleryOptional.get();
                    elasticGallery.setFileName(imageNameList);
                    elasticGalleryRepository.save(elasticGallery);
                } else {
                    errorMessage = "Elastic Gallery is not found";
                }
            } else {
                errorMessage = "Please choose an image";
                System.err.println(errorMessage);
            }
        } else {
            errorMessage = "Gallery is not found";
            System.err.println(errorMessage);
        }
        return "redirect:/gallery";
    }

    //Detail of Gallery
    @ResponseBody
    @GetMapping("/detail/{stId}")
    public Map<Check, Object> detailGallery(@PathVariable String stId) {
        Map<Check, Object> map = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            map.put(Check.status, true);
            map.put(Check.message, "Gallery detail operation is successful!");
            map.put(Check.result, galleryRepository.findById(id).get());
        } catch (Exception e) {
            map.put(Check.status, false);
            String error = "An error occurred during the operation!";
            System.err.println(e);
            Util.logger(error, Gallery.class);
            map.put(Check.message, error);
        }
        return map;
    }

    //Gallery delete
    @ResponseBody
    @DeleteMapping("/delete/{stId}")
    public Map<Check, Object> deleteGallery(@PathVariable String stId) {
        Map<Check, Object> map = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            Optional<Gallery> optionalGallery = galleryRepository.findById(id);
            if (optionalGallery.isPresent()) {
                //Images Delete
                Gallery gallery = optionalGallery.get();
                gallery.getFileName().forEach(item -> {
                    File file = new File(Util.UPLOAD_DIR_GALLERY + stId + "/" + item);
                    file.delete();
                });
                File file = new File(Util.UPLOAD_DIR_GALLERY + stId);
                if (file.exists()) {
                    file.delete();
                }
                //Images Delete
                ElasticGallery elasticGallery = elasticGalleryRepository.findById(Integer.toString(id)).get();
                galleryRepository.deleteById(id);
                elasticGalleryRepository.deleteById(elasticGallery.getId());
                map.put(Check.status, true);
                map.put(Check.message, "Data has been deleted!");
                map.put(Check.result, optionalGallery.get());
            } else {
                String error = "Gallery is not found";
                map.put(Check.status, false);
                map.put(Check.message, error);
                Util.logger(error, Gallery.class);
            }
        } catch (Exception e) {
            String error = "An error occurred during the delete operation";
            map.put(Check.status, false);
            map.put(Check.message, error);
            Util.logger(error, Gallery.class);
            System.err.println(e);
        }
        return map;
    }

    //Elasticsearch for GAllery
    @ResponseBody
    @GetMapping("/search/{data}/{stPageNo}/{stShowNumber}")
    public Map<Check, Object> searchGallery(@PathVariable String data, @PathVariable String
            stPageNo, @PathVariable String stShowNumber) {
        Map<Check, Object> map = new LinkedHashMap<>();
        try {
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo, showNumber);
            Page<ElasticGallery> searchPage = elasticGalleryRepository.findBySearchData(data, pageable);
            List<ElasticGallery> elasticGalleryList = searchPage.getContent();
            int totalData = elasticGalleryList.size(); //for total data in table
            if (totalData > 0) {
                map.put(Check.status, true);
                map.put(Check.totalPage, searchPage.getTotalPages());
                map.put(Check.message, "Search operation success!");
                map.put(Check.result, elasticGalleryList);
            } else {
                map.put(Check.status, false);
                map.put(Check.message, "Could not find a result for your search!");
            }
        } catch (NumberFormatException e) {
            map.put(Check.status, false);
            String error = "An error occurred during the search operation!";
            System.err.println(e);
            Util.logger(error, Gallery.class);
            map.put(Check.message, error);
        }
        return map;
    }

    //Chosen image delete
    @ResponseBody
    @DeleteMapping("/chosenImages/delete/{images}")
    public Map<Check, Object> deleteChosenImage(@PathVariable List<String> images) {
        Map<Check, Object> map = new LinkedHashMap<>();
        try {
            if (images.size() > 0) {
                for (int i = 0; i < images.size(); i++) {
                    System.out.println("Silinmek istenen dosya: " + images.get(i));
                    galleryRepository.deleteImageByFileName(images.get(i));
                    File file = new File(Util.UPLOAD_DIR_GALLERY + chosenId + "/" + images.get(i));
                    file.delete();
                }
                //Elasticsearch update images - Start
                Gallery gallery = galleryRepository.findById(chosenId).get();
                System.out.println("Id ye ait resimler " + gallery.getFileName());
                ElasticGallery elasticGallery = elasticGalleryRepository.findById(Integer.toString(chosenId)).get();
                elasticGallery.setFileName(gallery.getFileName());
                elasticGalleryRepository.save(elasticGallery);
                //Elasticsearch update images - End
                map.put(Check.status, true);
                map.put(Check.message, "The selected pictures have been deleted!");
            } else {
                map.put(Check.status, false);
                map.put(Check.message, "Please select a picture!");
            }
        } catch (Exception e) {
            String error = "An error occurred during the image delete operation";
            map.put(Check.status, false);
            map.put(Check.message, error);
            Util.logger(error, Gallery.class);
            System.err.println(e);
        }
        return map;
    }

    @GetMapping("/galleryDetail/get_image/id={id}name={name}")
    public void getImage(@PathVariable Integer id, @PathVariable String name, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("image/jpeg; image/jpg; image/png");
        File file = new File(Util.UPLOAD_DIR_GALLERY + id + "/" + name);
        if (file.exists()) {
            System.out.println(file.exists());
            byte[] fileContent = FileUtils.readFileToByteArray(file);
            response.getOutputStream().write(fileContent);
        } else {
            File defaultFile = new File(Util.UPLOAD_DIR_GALLERY + "default_image.jpg");
            byte[] defaultFileContent = FileUtils.readFileToByteArray(defaultFile);
            response.getOutputStream().write(defaultFileContent);
        }
        response.getOutputStream().close();
    }
}
