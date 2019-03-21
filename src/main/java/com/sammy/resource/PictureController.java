package com.sammy.resource;

import com.sammy.entity.api.PictureApiDTO;
import com.sammy.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Controller
public class PictureController {

    private static final String BASE_PATH = "/images";
    private static final String FILENAME = "{filename:.+}";

    private final PictureService pictureService;

    @Autowired
    public PictureController(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @GetMapping(value = "/")
    public String index(Model model, Pageable pageable){
        final Page<PictureApiDTO> apiDTOPage = pictureService.findPage(pageable);
        model.addAttribute("page", apiDTOPage);
        if(apiDTOPage.hasNext())
            model.addAttribute("next", pageable.next());

        if(apiDTOPage.hasPrevious())
            model.addAttribute("prev", pageable.previousOrFirst());
        return "index";
    }

    @ResponseBody
    @GetMapping(value = BASE_PATH + "/" + FILENAME + "/raw")
    public ResponseEntity<?> retrievePicture(@PathVariable String filename) {

        try{

            Resource file = pictureService.findOneImage(filename);
            return ResponseEntity.ok()
                                 .contentLength(file.contentLength())
                                 .contentType(MediaType.IMAGE_JPEG)
                                 .body(new InputStreamResource(file.getInputStream()));
        }catch (IOException ioe){
            return ResponseEntity.badRequest()
                                 .body("Couldn't find " + filename + " => " + ioe.getMessage());
        }
    }

    @PostMapping(value = BASE_PATH)
    public String createFile(@RequestParam ("file") MultipartFile file, RedirectAttributes attributes){
        try {

            pictureService.createPicture(file);
            attributes.addFlashAttribute("flash.message", "Successfully uploaded " + file.getOriginalFilename());
        }catch (IOException ioe){
            attributes.addFlashAttribute("flash.message", "Failed to upload " + file.getOriginalFilename()
                                                          + " => " + ioe.getMessage());
        }
        return "redirect:/";
    }

    @DeleteMapping(value = BASE_PATH + "/" + FILENAME)
    public String deleteFile(@PathVariable String filename, RedirectAttributes attributes){
        try{
            pictureService.deletePicture(filename);
            attributes.addFlashAttribute("flash.message", "Successfully deleted " + filename);
        }catch (IOException | RuntimeException ioe){
            attributes.addFlashAttribute("flash.message", "Failed to delete " + filename + " => " + ioe.getMessage());
        }
        return "redirect:/";
    }
}