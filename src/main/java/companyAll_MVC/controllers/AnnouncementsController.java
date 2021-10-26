package companyAll_MVC.controllers;

import companyAll_MVC.documents.ElasticAnnouncement;
import companyAll_MVC.entities.Announcement;
import companyAll_MVC.repositories._elastic.ElasticAnnouncementRepository;
import companyAll_MVC.repositories._jpa.AnnouncementRepository;
import companyAll_MVC.utils.Check;
import companyAll_MVC.utils.Util;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/announcements")
public class AnnouncementsController {

    final AnnouncementRepository announcementRepository;
    final ElasticAnnouncementRepository elasticAnnouncementRepository;

    public AnnouncementsController(AnnouncementRepository announcementRepository, ElasticAnnouncementRepository elasticAnnouncementRepository) {
        this.announcementRepository = announcementRepository;
        this.elasticAnnouncementRepository = elasticAnnouncementRepository;
    }

    @GetMapping("")
    public String announcements(){
        return "announcements";
    }

    @GetMapping("/get/id={id}")
    @ResponseBody
    public Map<Check, Object> get(@PathVariable Integer id) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        Optional<Announcement> announcement = announcementRepository.findById(id);
        if (announcement.isPresent()) {
            resultMap.put(Check.status, true);
            resultMap.put(Check.result, announcement);
        } else {
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No announcement found with given id!");
            resultMap.put(Check.result, new Object());
        }
        return resultMap;
    }

    @GetMapping("/all/status={status}")
    @ResponseBody
    public Map<Check, Object> all(@PathVariable String status) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        List<ElasticAnnouncement> elasticAnnouncements = elasticAnnouncementRepository.findAllByStatus(status);
        if (elasticAnnouncements.size() != 0) {
            resultMap.put(Check.status, true);
        } else {
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No announcement found!");
        }
        resultMap.put(Check.result, elasticAnnouncements);
        return resultMap;
    }

    @GetMapping("/all/status={status}/page={page}size={size}")
    @ResponseBody
    public Map<Check, Object> all(@PathVariable String status, @PathVariable Integer page, @PathVariable Integer size) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        resultMap.put(Check.status, true);
        Page<ElasticAnnouncement> elasticAnnouncementPage = elasticAnnouncementRepository.findAllByStatus(status, PageRequest.of(page, size));
        if (elasticAnnouncementPage.getTotalElements() != 0) {
            resultMap.put(Check.status, true);
        } else {
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No announcement found!");
        }
        resultMap.put(Check.totalPage, elasticAnnouncementPage.getTotalPages());
        resultMap.put(Check.result, elasticAnnouncementPage.getContent());
        return resultMap;
    }

    @GetMapping("/search/key={key}/status={status}/page={page}size={size}")
    @ResponseBody
    public Map<Check, Object> search(@PathVariable String key, @PathVariable String status, @PathVariable Integer page, @PathVariable Integer size) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        resultMap.put(Check.status, true);
        Page<ElasticAnnouncement> elasticAnnouncementPage = elasticAnnouncementRepository.searchByKeyAndStatus(key, status, PageRequest.of(page, size));
        if (elasticAnnouncementPage.getTotalElements() != 0) {
            resultMap.put(Check.status, true);
        } else {
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No announcement found!");
        }
        resultMap.put(Check.totalPage, elasticAnnouncementPage.getTotalPages());
        resultMap.put(Check.result, elasticAnnouncementPage.getContent());
        return resultMap;
    }

    @PostMapping("/add")
    @ResponseBody
    public Map<Check, Object> add(@RequestBody @Valid Announcement announcement, BindingResult bindingResult) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        if (bindingResult.hasErrors()) {
            resultMap.put(Check.status, false);
            resultMap.put(Check.errors, Util.errors(bindingResult));
        } else {
            try{
                announcement.setDate(Util.getDateFormatter());
                int savedAnnouncementId = announcementRepository.save(announcement).getId();
                ElasticAnnouncement elasticAnnouncement = new ElasticAnnouncement();
                elasticAnnouncement.setAid(announcement.getId());
                elasticAnnouncement.setNo(announcement.getNo());
                elasticAnnouncement.setTitle(announcement.getTitle());
                elasticAnnouncement.setDate(announcement.getDate());
                elasticAnnouncement.setDetail(announcement.getDetail());
                elasticAnnouncement.setStatus(announcement.getStatus());
                elasticAnnouncementRepository.save(elasticAnnouncement);
                resultMap.put(Check.status, true);
                resultMap.put(Check.message, "Announcement successfully saved!");
                resultMap.put(Check.result, savedAnnouncementId);
            }catch (DataIntegrityViolationException e){
                if (announcementRepository.findByTitle(announcement.getTitle()).isPresent()) {
                    resultMap.put(Check.message, "This title has already been used");
                }
            }catch (Exception e){
                resultMap.put(Check.status, false);
                resultMap.put(Check.message, "An error occurred in save operation!");
            }
        }
        return resultMap;
    }

    @PostMapping("/update")
    @ResponseBody
    public Map<Check, Object> update(@RequestBody @Valid Announcement announcement, BindingResult bindingResult) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        if (bindingResult.hasErrors()) {
            resultMap.put(Check.status, false);
            resultMap.put(Check.errors, Util.errors(bindingResult));
        } else {
            try{
                int savedAnnouncementId = announcementRepository.saveAndFlush(announcement).getId();
                Optional<ElasticAnnouncement> elasticAnnouncementOptional = elasticAnnouncementRepository.findByAid(savedAnnouncementId);
                if(elasticAnnouncementOptional.isPresent()){
                    ElasticAnnouncement elasticAnnouncement = elasticAnnouncementOptional.get();
                    elasticAnnouncement.setAid(announcement.getId());
                    elasticAnnouncement.setNo(announcement.getNo());
                    elasticAnnouncement.setTitle(announcement.getTitle());
                    elasticAnnouncement.setDetail(announcement.getDetail());
                    elasticAnnouncement.setStatus(announcement.getStatus());
                    elasticAnnouncementRepository.save(elasticAnnouncement);
                    resultMap.put(Check.status, true);
                    resultMap.put(Check.message, "Announcement successfully saved!");
                    resultMap.put(Check.result, savedAnnouncementId);
                }
            }catch (DataIntegrityViolationException e){
                if (announcementRepository.findByTitle(announcement.getTitle()).isPresent()) {
                    resultMap.put(Check.message, "This title has already been used");
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
        Optional<Announcement> announcementOptional = announcementRepository.findById(id);
        Optional<ElasticAnnouncement> elasticAnnouncementOptional = elasticAnnouncementRepository.findByAid(id);
        if(announcementOptional.isPresent() && elasticAnnouncementOptional.isPresent()){
            try{
                announcementRepository.deleteById(announcementOptional.get().getId());
                elasticAnnouncementRepository.deleteById(elasticAnnouncementOptional.get().getId());
                resultMap.put(Check.status, true);
                resultMap.put(Check.message, "Announcement has been deleted!");
            }catch (Exception e){
                resultMap.put(Check.status, false);
                resultMap.put(Check.message, "An error occurred in delete operation!");
            }
        }else{
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No announcement found with given id!");
        }

        return resultMap;
    }

}
