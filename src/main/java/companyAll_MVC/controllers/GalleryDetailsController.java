package companyAll_MVC.controllers;

import companyAll_MVC.entities.Gallery;
import companyAll_MVC.repositories._jpa.GalleryRepository;
import companyAll_MVC.utils.Util;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/galleryDetails")
public class GalleryDetailsController {

    final GalleryRepository galleryRepository;
    public GalleryDetailsController(GalleryRepository galleryRepository) {
        this.galleryRepository = galleryRepository;
    }

    @GetMapping("/{stId}")
    public String galleryDetails(@PathVariable String stId, Model model){

        try {
            int id = Integer.parseInt(stId);
            Optional<Gallery> galleryOptional = galleryRepository.findById(id);
            if (galleryOptional.isPresent()){
                Gallery gallery = galleryOptional.get();
                model.addAttribute("gallery",gallery);
            }
        } catch (Exception e) {
            System.err.println(e);
            return "redirect:/galleryList";
        }
        return "galleryDetails";
    }

    @GetMapping("/get_image/id={id}name={name}")
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
