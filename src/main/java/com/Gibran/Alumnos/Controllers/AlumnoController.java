package com.Gibran.Alumnos.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Gibran.Alumnos.model.AlumnoModel;
import com.Gibran.Alumnos.repository.AlumnoRepository;

@RestController
@RequestMapping("/alumnos")
public class AlumnoController {

    @Autowired
    private  AlumnoRepository alumnoRepository;

    @GetMapping("/traer-alumnos")
    public List<AlumnoModel> TraerAlumnos() {
        return alumnoRepository.findAll();
    }
}
