package com.Gibran.Alumnos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Gibran.Alumnos.model.AlumnoModel;

public interface AlumnoRepository extends JpaRepository<AlumnoModel,Long> {

}
