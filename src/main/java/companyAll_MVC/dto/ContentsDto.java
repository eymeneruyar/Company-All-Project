package companyAll_MVC.dto;

import companyAll_MVC.documents.ElasticContents;
import companyAll_MVC.entities.Contents;
import companyAll_MVC.repositories._elastic.ElasticContentsRepository;
import companyAll_MVC.repositories._jpa.ContentsRepository;
import companyAll_MVC.utils.Check;
import companyAll_MVC.utils.Util;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContentsDto {

    final ContentsRepository contentsRepository;
    final ElasticContentsRepository elasticContentsRepository;
    final ElasticsearchOperations elasticsearchOperations;

    public ContentsDto(ContentsRepository contentsRepository, ElasticContentsRepository elasticContentsRepository, ElasticsearchOperations elasticsearchOperations) {
        this.contentsRepository = contentsRepository;
        this.elasticContentsRepository = elasticContentsRepository;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    //Contents list with pagination
    public Map<Check,Object> list(String stShowNumber,String stPageNo){
        Map<Check,Object> map = new LinkedHashMap<>();
        //System.out.println(stShowNumber + " " + stPageNo);
        try {
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo,showNumber);
            int totalData =  elasticContentsRepository.findAllByOrderByIdAsc().size(); //for total data in table
            List<ElasticContents> elasticContentsList = elasticContentsRepository.findByOrderByIdAsc(pageable);
            map.put(Check.status,true);
            map.put(Check.totalPage, Util.getTotalPage(totalData,showNumber));
            map.put(Check.message, "Content listing on page " + (pageNo + 1) + " is successful");
            map.put(Check.result,elasticContentsList);
        } catch (Exception e) {
            map.put(Check.status,false);
            String error = "An error occurred during the operation!";
            System.err.println(e);
            Util.logger(error, Contents.class);
            map.put(Check.message,error);
        }
        //System.out.println(map);
        return map;
    }

}
