package com.Gibran.Motos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Gibran.Motos.model.MotoModel;

public interface MotoRepository extends JpaRepository<MotoModel, Long> {
    // Puedes añadir métodos personalizados aquí si los necesitas
    // Ejemplo:
    // List<MotoModel> findByMarca(String marca);
    // List<MotoModel> findByCilindradaGreaterThan(String cilindrada);
}