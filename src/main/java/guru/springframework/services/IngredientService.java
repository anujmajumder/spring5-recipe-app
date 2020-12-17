package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;

public interface IngredientService {

    IngredientCommand findByIdAndIngredientId(Long id,Long ingredientId);

    IngredientCommand saveOrUpdate(IngredientCommand command);
}
