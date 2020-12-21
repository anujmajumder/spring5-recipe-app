package guru.springframework.controllers;

import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class imageController {

    private RecipeService recipeService;

    private ImageService imageService;


    public imageController(RecipeService recipeService, ImageService imageService) {
        this.recipeService = recipeService;
        this.imageService = imageService;
    }

    @GetMapping("/recipe/{id}/image")
    public String showUploadForm(@PathVariable String id, Model model)
    {
        model.addAttribute("recipe",recipeService.findCommandById(Long.valueOf(id)));

        return "recipe/imageuploadform";

    }

    @PostMapping("/recipe/{id}/image")
    public String handleImagePost(@PathVariable String id, @RequestParam("imagefile")MultipartFile multipartFile)
    {

        imageService.saveImageFile(Long.valueOf(id),multipartFile);

        return "redirect:/recipe/"+id+"/show";

    }

}
