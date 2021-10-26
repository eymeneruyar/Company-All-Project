package companyAll_MVC.dto;

import companyAll_MVC.documents.ElasticAdvertisement;
import companyAll_MVC.entities.Advertisement;
import companyAll_MVC.repositories._elastic.ElasticAdvertisementRepository;
import companyAll_MVC.repositories._jpa.AdvertisementRepository;
import companyAll_MVC.utils.Check;
import companyAll_MVC.utils.Util;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AdvertisementDto {

    final AdvertisementRepository advertisementRepository;
    final ElasticAdvertisementRepository elasticAdvertisementRepository;

    public AdvertisementDto(AdvertisementRepository advertisementRepository, ElasticAdvertisementRepository elasticAdvertisementRepository) {
        this.advertisementRepository = advertisementRepository;
        this.elasticAdvertisementRepository = elasticAdvertisementRepository;
    }

    public Map<Check, Object> get(Integer id) {
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

    public Map<Check, Object> getImage(String image) throws IOException {
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

    public Map<Check, Object> click(Integer id) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        Optional<Advertisement> advertisementOptional = advertisementRepository.findById(id);
        Optional<ElasticAdvertisement> elasticAdvertisementOptional = elasticAdvertisementRepository.findByAid(id);
        if (advertisementOptional.isPresent() && elasticAdvertisementOptional.isPresent()) {
            try{
                Advertisement advertisement = advertisementOptional.get();
                ElasticAdvertisement elasticAdvertisement = elasticAdvertisementOptional.get();
                Long click = advertisement.getClick() + 1;
                advertisement.setClick(click);
                elasticAdvertisement.setClick(click);
                advertisementRepository.saveAndFlush(advertisement);
                elasticAdvertisementRepository.save(elasticAdvertisement);
                resultMap.put(Check.status, true);
                resultMap.put(Check.message, "Advertisement has clicked!");
            }catch (Exception e){
                resultMap.put(Check.status, false);
                resultMap.put(Check.message, "An error occured during the operation!");
            }

        } else {
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No advertisement found with given id!");
        }
        return resultMap;
    }

    public Map<Check, Object> view(Integer id) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        Optional<Advertisement> advertisementOptional = advertisementRepository.findById(id);
        Optional<ElasticAdvertisement> elasticAdvertisementOptional = elasticAdvertisementRepository.findByAid(id);
        if (advertisementOptional.isPresent() && elasticAdvertisementOptional.isPresent()) {
            try{
                Advertisement advertisement = advertisementOptional.get();
                ElasticAdvertisement elasticAdvertisement = elasticAdvertisementOptional.get();
                int view = advertisement.getView() + 1;
                advertisement.setView(view);
                elasticAdvertisement.setView(view);
                advertisementRepository.saveAndFlush(advertisement);
                elasticAdvertisementRepository.save(elasticAdvertisement);
                resultMap.put(Check.status, true);
                resultMap.put(Check.message, "Advertisement has viewed!");
            }catch (Exception e){
                resultMap.put(Check.status, false);
                resultMap.put(Check.message, "An error occured during the operation!");
            }

        } else {
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No advertisement found with given id!");
        }
        return resultMap;
    }

}
