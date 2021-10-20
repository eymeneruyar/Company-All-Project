package companyAll_MVC.controllers;

import companyAll_MVC.documents.ElasticAdvertisement;
import companyAll_MVC.entities.Advertisement;
import companyAll_MVC.repositories._elastic.ElasticAdvertisementRepository;
import companyAll_MVC.repositories._jpa.AdvertisementRepository;
import companyAll_MVC.utils.Check;
import companyAll_MVC.utils.Util;
import org.apache.commons.io.FileUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/advertisement")
public class AdvertisementController {

    final AdvertisementRepository advertisementRepository;
    final ElasticAdvertisementRepository elasticAdvertisementRepository;

    public AdvertisementController(AdvertisementRepository advertisementRepository, ElasticAdvertisementRepository elasticAdvertisementRepository) {
        this.advertisementRepository = advertisementRepository;
        this.elasticAdvertisementRepository = elasticAdvertisementRepository;
    }

    @GetMapping("")
    public String advertisement(){
        return "advertisement";
    }

    @GetMapping("/get/id={id}")
    @ResponseBody
    public Map<Check, Object> get(@PathVariable Integer id) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        Optional<Advertisement> advertisement = advertisementRepository.findById(id);
        if (advertisement.isPresent()) {
            resultMap.put(Check.status, true);
            resultMap.put(Check.result, advertisement);
        } else {
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No advertisement found with given id!");
        }
        return resultMap;
    }

    @GetMapping(value = "/get/image/name={image}")
    @ResponseBody
    public Map<Check, Object> getImage(@PathVariable String image) throws IOException {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        String path = Util.UPLOAD_DIR_ADVERTISEMENT + image;
        byte[] fileContent = FileUtils.readFileToByteArray(new File(path));
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        if(encodedString != null){
            resultMap.put(Check.status, true);
            resultMap.put(Check.result, encodedString);
        }else{
            resultMap.put(Check.status, false);
        }
        return resultMap;
    }

    @GetMapping("/all/status={status}")
    @ResponseBody
    public Map<Check, Object> all(@PathVariable String status) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        List<ElasticAdvertisement> elasticAdvertisements = elasticAdvertisementRepository.findAllByStatus(status);
        if (elasticAdvertisements.size() != 0) {
            resultMap.put(Check.status, true);
        } else {
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No advertisement found!");
        }
        resultMap.put(Check.result, elasticAdvertisements);
        return resultMap;
    }

    @GetMapping("/all/status={status}/page={page}size={size}")
    @ResponseBody
    public Map<Check, Object> all(@PathVariable String status, @PathVariable Integer page, @PathVariable Integer size) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        resultMap.put(Check.status, true);
        Page<ElasticAdvertisement> elasticAdvertisementPage = elasticAdvertisementRepository.findAllByStatus(status, PageRequest.of(page, size));
        if (elasticAdvertisementPage.getTotalElements() != 0) {
            resultMap.put(Check.status, true);
        } else {
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No advertisement found!");
        }
        resultMap.put(Check.totalPage, elasticAdvertisementPage.getTotalPages());
        resultMap.put(Check.result, elasticAdvertisementPage.getContent());
        return resultMap;
    }

    @GetMapping("/search/key={key}/status={status}/page={page}size={size}")
    @ResponseBody
    public Map<Check, Object> search(@PathVariable String key, @PathVariable String status, @PathVariable Integer page, @PathVariable Integer size) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        resultMap.put(Check.status, true);
        Page<ElasticAdvertisement> elasticAdvertisementPage = elasticAdvertisementRepository.searchByKeyAndStatus(key, status, PageRequest.of(page, size));
        if (elasticAdvertisementPage.getTotalElements() != 0) {
            resultMap.put(Check.status, true);
        } else {
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No advertisement found!");
        }
        resultMap.put(Check.totalPage, elasticAdvertisementPage.getTotalPages());
        resultMap.put(Check.result, elasticAdvertisementPage.getContent());
        return resultMap;
    }

    @PostMapping("/add")
    @ResponseBody
    public Map<Check, Object> add(@RequestBody @Valid Advertisement advertisement, BindingResult bindingResult) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        if (bindingResult.hasErrors()) {
            resultMap.put(Check.status, false);
            resultMap.put(Check.errors, Util.errors(bindingResult));
        } else {
            try{
                int savedAdvertisementId = advertisementRepository.save(advertisement).getId();
                ElasticAdvertisement elasticAdvertisement = new ElasticAdvertisement();
                elasticAdvertisement.setAid(advertisement.getId());
                elasticAdvertisement.setNo(advertisement.getNo());
                elasticAdvertisement.setName(advertisement.getName());
                elasticAdvertisement.setView(advertisement.getView());
                elasticAdvertisement.setWidth(advertisement.getWidth());
                elasticAdvertisement.setHeight(advertisement.getHeight());
                elasticAdvertisement.setClick(advertisement.getClick());
                elasticAdvertisement.setStatus(advertisement.getStatus());
                elasticAdvertisementRepository.save(elasticAdvertisement);
                resultMap.put(Check.status, true);
                resultMap.put(Check.result, savedAdvertisementId);
            }catch (DataIntegrityViolationException e){
                if (advertisementRepository.findByName(advertisement.getName()).isPresent()) {
                    resultMap.put(Check.message, "This name has already been used");
                }
            }catch (Exception e){
                resultMap.put(Check.status, false);
                resultMap.put(Check.message, "An error occurred in save operation!");
                System.err.println(e);
            }
        }
        return resultMap;
    }

    @PostMapping("/update")
    @ResponseBody
    public Map<Check, Object> update(@RequestBody @Valid Advertisement advertisement, BindingResult bindingResult) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        if (bindingResult.hasErrors()) {
            resultMap.put(Check.status, false);
            resultMap.put(Check.errors, Util.errors(bindingResult));
        } else {
            try{
                int savedAdvertisementId = advertisementRepository.saveAndFlush(advertisement).getId();
                Optional<ElasticAdvertisement> elasticAdvertisementOptional = elasticAdvertisementRepository.findByAid(savedAdvertisementId);
                if(elasticAdvertisementOptional.isPresent()){
                    ElasticAdvertisement elasticAdvertisement = elasticAdvertisementOptional.get();
                    elasticAdvertisement.setAid(advertisement.getId());
                    elasticAdvertisement.setNo(advertisement.getNo());
                    elasticAdvertisement.setName(advertisement.getName());
                    elasticAdvertisement.setView(advertisement.getView());
                    elasticAdvertisement.setWidth(advertisement.getWidth());
                    elasticAdvertisement.setHeight(advertisement.getHeight());
                    elasticAdvertisement.setClick(advertisement.getClick());
                    elasticAdvertisement.setStatus(advertisement.getStatus());
                    elasticAdvertisementRepository.save(elasticAdvertisement);
                    resultMap.put(Check.status, true);
                    resultMap.put(Check.result, savedAdvertisementId);
                }
            }catch (DataIntegrityViolationException e){
                if (advertisementRepository.findByName(advertisement.getName()).isPresent()) {
                    resultMap.put(Check.message, "This name has already been used");
                }
            }catch (Exception e){
                resultMap.put(Check.status, false);
                resultMap.put(Check.message, "An error occurred in save operation!");
            }
        }
        return resultMap;
    }

    @DeleteMapping("/delete/id={id}")
    @ResponseBody
    public Map<Check, Object> delete(@PathVariable Integer id) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        Optional<Advertisement> advertisementOptional = advertisementRepository.findById(id);
        Optional<ElasticAdvertisement> elasticAdvertisementOptional = elasticAdvertisementRepository.findByAid(id);
        if(advertisementOptional.isPresent() && elasticAdvertisementOptional.isPresent()){
            try{
                advertisementRepository.deleteById(advertisementOptional.get().getId());
                elasticAdvertisementRepository.deleteById(elasticAdvertisementOptional.get().getId());
                resultMap.put(Check.status, true);
                resultMap.put(Check.message, "Advertisement has been deleted!");
            }catch (Exception e){
                resultMap.put(Check.status, false);
                resultMap.put(Check.message, "An error occurred in delete operation!");
            }
        }else{
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No advertisement found with given id!");
        }

        return resultMap;
    }

    @PostMapping("/image_upload/advertisement_id={id}")
    @ResponseBody
    public Map<Check, Object> uploadImage(@PathVariable Integer id, @RequestParam("image") MultipartFile file) {
        Map<Check, Object> resultMap = Util.imageUpload(file, Util.UPLOAD_DIR_ADVERTISEMENT);
        if((Boolean) resultMap.get(Check.status)){
            Advertisement advertisement = advertisementRepository.getById(id);
            try{
                advertisement.setImage(resultMap.get(Check.result).toString());
                advertisementRepository.save(advertisement);
                resultMap.put(Check.status, true);
                resultMap.put(Check.message, "Advertisement successfully saved!");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return resultMap;
    }

    @PostMapping("/image_update/advertisement_id={id}")
    @ResponseBody
    public Map<Check, Object> updateImage(@PathVariable Integer id, @RequestParam("image") MultipartFile file) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        Advertisement advertisement = advertisementRepository.getById(id);
        File fileOld = new File(Util.UPLOAD_DIR_ADVERTISEMENT + advertisement.getImage());
        if(fileOld.delete()){
            resultMap = Util.imageUpload(file, Util.UPLOAD_DIR_ADVERTISEMENT);
            if((Boolean) resultMap.get(Check.status)){
                try{
                    advertisement.setImage(resultMap.get(Check.result).toString());
                    advertisementRepository.save(advertisement);
                    resultMap.put(Check.status, true);
                    resultMap.put(Check.message, "Advertisement successfully saved!");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        return resultMap;
    }

}
