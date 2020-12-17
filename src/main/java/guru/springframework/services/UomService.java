package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;

import java.util.Set;

public interface UomService {

    Set<UnitOfMeasureCommand> findAll();
}
