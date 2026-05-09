package com.example.MayaFisioLumiere.Services;

import com.example.MayaFisioLumiere.Domain.Exercises.ExerciseRequestDTO;
import com.example.MayaFisioLumiere.Domain.Exercises.ExerciseResponseDTO;
import com.example.MayaFisioLumiere.Entity.ExerciseEntity;
import com.example.MayaFisioLumiere.Repository.ExercisesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseService {

    // Lógica de criar exercicios contendo titulo, link do youtube, tags e descrição do exercicio
    @Autowired
    private ExercisesRepository exercisesRepository;

    public ExerciseEntity createExercise(ExerciseRequestDTO data) {

        ExerciseEntity newExercise = new ExerciseEntity();
        newExercise.setTitle(data.title());
        newExercise.setTags(data.tags());
        newExercise.setMidiaURL(data.midiaURL());
        newExercise.setDescription(data.description());

        return exercisesRepository.save(newExercise);
    }

    // Atualizar um atributo do exercicio
    public ExerciseResponseDTO updateExercise(Long id, ExerciseRequestDTO data) {
        // Procura o id do exercicio e edita no banco somente o que não é nulo
        ExerciseEntity exercise = exercisesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exercício não encontrado"));

        if (data.title() != null) {
            exercise.setTitle(data.title());
        }

        if (data.tags() != null) {
            exercise.setTags(data.tags());
        }

        if (data.description() != null) {
            exercise.setDescription(data.description());
        }

        if (data.midiaURL() != null) {
            exercise.setMidiaURL(data.midiaURL());
        }
        exercisesRepository.save(exercise);
        return new ExerciseResponseDTO(
                exercise.getExercise_ID(),
                exercise.getTitle(),
                exercise.getMidiaURL(),
                exercise.getTags(),
                exercise.getDescription()
        );

    }

    // Para buscar todos os exercicios
    public List<ExerciseResponseDTO> getAllExercises(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ExerciseEntity> exercisesPage = this.exercisesRepository.findAll(pageable);
        return exercisesPage.map(exercise -> new ExerciseResponseDTO(
                exercise.getExercise_ID(),
                exercise.getTitle(),
                exercise.getMidiaURL(),
                exercise.getTags(),
                exercise.getDescription()
        )
        ).stream().toList();
    }

    // Buscar exercicios por ID
    public ExerciseResponseDTO findById(Long id) {

        ExerciseEntity exercise = exercisesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exercício não encontrado"));

        return new ExerciseResponseDTO(
                exercise.getExercise_ID(),
                exercise.getTitle(),
                exercise.getMidiaURL(),
                exercise.getTags(),
                exercise.getDescription()
        );
    }

    // Buscar exercicios por nome/titulo do exercicio
    public List<ExerciseResponseDTO> findByTitleContaining(String title) {

        List<ExerciseEntity> exercises = exercisesRepository.findByTitleContainingIgnoreCase(title);

        return exercises.stream().map(exercise
                -> new ExerciseResponseDTO(
                        exercise.getExercise_ID(),
                        exercise.getTitle(),
                        exercise.getMidiaURL(),
                        exercise.getTags(),
                        exercise.getDescription()
                )
        ).toList();
    }

    // Buscar exercicios por tags
    public List<ExerciseResponseDTO> findByTags(String tag) {

        List<ExerciseEntity> exercises = exercisesRepository.findByTagsContainingIgnoreCase(tag);

        return exercises.stream().map(exercise
                -> new ExerciseResponseDTO(
                        exercise.getExercise_ID(),
                        exercise.getTitle(),
                        exercise.getMidiaURL(),
                        exercise.getTags(),
                        exercise.getDescription()
                )
        ).toList();
    }

    // Deletar um exercicio por id
    public void deleteExerciseById(Long id) {

        if (!exercisesRepository.existsById(id)) {
            throw new RuntimeException("Exercise not found");
        }
        exercisesRepository.deleteById(id);
    }
}
