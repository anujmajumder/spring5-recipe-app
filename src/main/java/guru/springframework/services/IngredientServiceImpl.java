package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.converters.UnitOfMeasureCommandToUnitOfMeasure;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class IngredientServiceImpl implements IngredientService {

     private RecipeRepository recipeRepository;

     private IngredientToIngredientCommand ingredientToIngredientCommand;

     private IngredientCommandToIngredient ingredientCommandToIngredient;

     private UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure;

     private UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(RecipeRepository recipeRepository, IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient, UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;

        this.unitOfMeasureCommandToUnitOfMeasure = unitOfMeasureCommandToUnitOfMeasure;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public IngredientCommand findByIdAndIngredientId(Long id, Long ingredientId) {

        Optional<Recipe> recipe = recipeRepository.findById(id);

        Stream<Ingredient> ingredientStream = recipe.get().getIngredients().stream().filter(ingredient -> {
            return ingredient.getId().equals(ingredientId);
        });
        return ingredientStream.map(ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst().get();


    }

    @Override
    public IngredientCommand saveOrUpdate(IngredientCommand command) {



        Optional<Recipe> recipe = recipeRepository.findById(command.getRecipeId());

        Optional<Ingredient> ingredients = recipe.get().getIngredients().stream().filter(ingredient -> {
           return ingredient.getId().equals(command.getId());
        }).findFirst();

        if(ingredients.isPresent()) {

            Ingredient ingredient = ingredients.get();

            ingredient.setAmount(command.getAmount());
            ingredient.setDescription(command.getDescription());
            ingredient.setUom(unitOfMeasureRepository.findById(command.getUnitOfMeasure().getId()).get());

        }
        else recipe.get().addIngredient( ingredientCommandToIngredient.convert(command));

       Recipe savedRecipe = recipeRepository.save(recipe.get());


        Optional<Ingredient> savedIngredient = savedRecipe.getIngredients().stream().filter(ingredient1 ->
                ingredient1.getId().equals(command.getId())).findFirst();

        if(!savedIngredient.isPresent())
        {
             savedIngredient = savedRecipe.getIngredients().stream().filter(ingredient1 -> ingredient1.getDescription().equals(command.getDescription())).
                    filter(ingredient1 -> ingredient1.getAmount().equals(command.getAmount())).
                    filter(ingredient1 -> ingredient1.getUom().getId().equals(command.getUnitOfMeasure().getId())).findFirst();
        }


        return ingredientToIngredientCommand.convert(savedIngredient.get());
    }
}
