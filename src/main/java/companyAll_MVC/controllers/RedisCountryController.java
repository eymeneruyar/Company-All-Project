package companyAll_MVC.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import companyAll_MVC.model.RedisCountry;
import companyAll_MVC.properties.Country;
import companyAll_MVC.properties.CountryHolder;
import companyAll_MVC.repositories._redis.RedisCountryRepository;
import companyAll_MVC.utils.Check;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller
@RequestMapping("/redis")
public class RedisCountryController {

    final RedisCountryRepository redisCountryRepository;

    public RedisCountryController(RedisCountryRepository redisCountryRepository) {
        this.redisCountryRepository = redisCountryRepository;
    }

    @GetMapping("")
    public String set() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<RedisCountry> redisCountryList = new ArrayList<>();
        InputStream is = CountryHolder.class.getResourceAsStream("/countries.json");
        CountryHolder countryHolder = mapper.readValue(is, CountryHolder.class);
        for(Country country : countryHolder.getCountries()){
            RedisCountry redisCountry = new RedisCountry();
            redisCountry.setName(country.getCountry());
            redisCountry.setCities(country.getStates());
            redisCountryList.add(redisCountry);
        }
        redisCountryRepository.saveAll(redisCountryList);

        return "dashboard";
    }

    @GetMapping("/get_countries")
    @ResponseBody
    public Map<Check, Object> getCountries() {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        Iterable<RedisCountry> redisCountries = redisCountryRepository.findAll();
        resultMap.put(Check.status, true);
        resultMap.put(Check.result, redisCountries);
        return resultMap;
    }

    @GetMapping("/get_cities/country_id={id}")
    @ResponseBody
    public Map<Check, Object> getCities(@PathVariable String id) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        Optional<RedisCountry> redisCountryOptional = redisCountryRepository.findById(id);
        if(redisCountryOptional.isPresent()){
            resultMap.put(Check.status, true);
            resultMap.put(Check.result, redisCountryOptional.get().getCities());
        }else{
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "Country not found!");
        }
        return resultMap;
    }


}
