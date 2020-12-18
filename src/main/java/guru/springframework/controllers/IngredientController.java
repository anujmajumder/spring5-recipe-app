package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class IngredientController {

    private final RecipeService recipeService;

    private final IngredientService ingredientService;

    private final UomService uomService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UomService uomService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.uomService = uomService;
    }

    @GetMapping("/recipe/{id}/ingredients")
    public String showIngredients(@PathVariable String id , Model model)
    {
        RecipeCommand recipeCommand = recipeService.findCommandById(Long.valueOf(id));

        model.addAttribute("recipe",recipeCommand);

        return "recipe/ingredient/list";
    }

    @GetMapping("/recipe/{id}/ingredient/{ingredientId}/show")
    public String viewIngredient(@PathVariable String id, @PathVariable String ingredientId , Model model)
    {
        model.addAttribute("ingredient", ingredientService.findByIdAndIngredientId(Long.valueOf(id),Long.valueOf(ingredientId)));

        return "recipe/ingredient/show";
    }

    @GetMapping("/recipe/{id}/ingredient/{ingredientId}/update")
    public String updateIngredient(@PathVariable String id, @PathVariable String ingredientId,Model model)
    {
        model.addAttribute("ingredient", ingredientService.findByIdAndIngredientId(Long.valueOf(id),Long.valueOf(ingredientId)));

        model.addAttribute("uomList",uomService.findAll());

        return "recipe/ingredient/ingredientform";
    }
    @GetMapping("/recipe/{recipeId}/ingredient/new")
    public String newIngredient(@PathVariable String recipeId,Model model)
    {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(Long.valueOf(recipeId));
        ingredientCommand.setUnitOfMeasure(new UnitOfMeasureCommand());
        model.addAttribute("ingredient",ingredientCommand);



        model.addAttribute("uomList",uomService.findAll());

        return "recipe/ingredient/ingredientform";
    }

    @PostMapping("/recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand ingredientCommand)
    {
        IngredientCommand command = ingredientService.saveOrUpdate(ingredientCommand);

        return "redirect:/recipe/"+command.getRecipeId()+"/ingredient/"+command.getId()+"/show";

    }
}
