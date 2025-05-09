package com.Gibran.Alumnos.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Gibran.Alumnos.model.AlumnoModel;
import com.Gibran.Alumnos.repository.AlumnoRepository;

@RestController
@RequestMapping("/alumnos")
public class AlumnoController {

    @Autowired
    private  AlumnoRepository alumnoRepository;
//Metodo GET para obtener todos los alumnos
    @GetMapping("/traer-alumnos")
    public List<AlumnoModel> TraerAlumnos() {
        return alumnoRepository.findAll();
    }

    //Metodo para insertar un alumno a la base de datos
   @PostMapping("/insertar-alumnos")
    public AlumnoModel InsertarAlumno(@RequestBody AlumnoModel alumno) {
        return alumnoRepository.save(alumno);
    }

    //Metodo para Editar un alumno a la base de datos
    @PutMapping("/editar-alumnos/{id}")
    public ResponseEntity<AlumnoModel> actualizarAlumno(@PathVariable Long id, @RequestBody AlumnoModel alumno) {
        return alumnoRepository.findById(id).map(alumnoExistente -> {
            alumnoExistente.setNombre(alumno.getNombre());
            alumnoExistente.setApellido(alumno.getApellido());
            alumnoExistente.setEmail(alumno.getEmail());
            alumnoExistente.setTelefono(alumno.getTelefono());
            alumnoExistente.setNumeroControl(alumno.getNumeroControl());
            alumnoExistente.setCarrera(alumno.getCarrera());
            alumnoExistente.setImagenURL(alumno.getImagenURL());
            AlumnoModel actualizado = alumnoRepository.save(alumnoExistente);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }


}

