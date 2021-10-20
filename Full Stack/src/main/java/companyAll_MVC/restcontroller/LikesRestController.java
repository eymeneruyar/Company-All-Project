package companyAll_MVC.restcontroller;

import companyAll_MVC.dto.LikesDto;
import companyAll_MVC.entities.Indent;
import companyAll_MVC.entities.Likes;
import companyAll_MVC.entities.LikesProduct;
import companyAll_MVC.entities.Product;
import companyAll_MVC.repositories._jpa.IndentRepository;
import companyAll_MVC.repositories._jpa.LikesProductRepository;
import companyAll_MVC.utils.Check;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/likes")
public class LikesRestController {

    final LikesDto likesDto;
    final LikesProductRepository likesProductRepository;
    final IndentRepository indentRepository;

    public LikesRestController(LikesDto likesDto, LikesProductRepository likesProductRepository, IndentRepository indentRepository) {
        this.likesDto = likesDto;
        this.likesProductRepository = likesProductRepository;
        this.indentRepository = indentRepository;
    }


    @GetMapping("/list/ratingByIndentId/{id}")
    public Map<Check, Object> list(@PathVariable Integer id) {
        return likesDto.list(id);
    }

    // likes to customer id
    @GetMapping("/list/raitngByCustomerId/{id}")
    public Map<Check, Object> listCustomerById(@PathVariable Integer id) {
        return likesDto.listCustomerById(id);
    }

    @PostMapping("/addlike")
    public Map<Check,Object> add(@RequestBody Likes likes) {
        return likesDto.add(likes);
    }



}
