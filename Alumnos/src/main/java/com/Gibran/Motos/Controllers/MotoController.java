package com.Gibran.Motos.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Gibran.Motos.model.MotoModel;
import com.Gibran.Motos.repository.MotoRepository;

@RestController
@RequestMapping("/motos")
@CrossOrigin(origins = "*")
public class MotoController {

    @Autowired
    private MotoRepository motoRepository;

    // Método GET para obtener todas las motos
    @GetMapping("/traer-motos")
    public List<MotoModel> traerMotos() {
        return motoRepository.findAll();
    }

    @GetMapping("/traer-moto/{id}")
    public ResponseEntity<MotoModel> traerUnaMoto(@PathVariable Long id) {
        return motoRepository.findById(id)
                .map(moto -> ResponseEntity.ok(moto))
                .orElse(ResponseEntity.notFound().build());
    }

    // Método para insertar una moto a la base de datos
    @PostMapping("/insertar-motos")
    public MotoModel insertarMoto(@RequestBody MotoModel moto) {
        return motoRepository.save(moto);
    }

    // Método para Editar una moto en la base de datos
    @PutMapping("/editar-motos/{id}")
    public ResponseEntity<MotoModel> actualizarMoto(@PathVariable Long id, @RequestBody MotoModel moto) {
        return motoRepository.findById(id).map(motoExistente -> {
            motoExistente.setNumeroSerie(moto.getNumeroSerie());
            motoExistente.setMarca(moto.getMarca());
            motoExistente.setModelo(moto.getModelo());
            motoExistente.setCilindrada(moto.getCilindrada());
            motoExistente.setColor(moto.getColor());
            motoExistente.setAño(moto.getAño());
            motoExistente.setImagenURL(moto.getImagenURL());
            motoExistente.setPrecio(moto.getPrecio());
            MotoModel actualizada = motoRepository.save(motoExistente);
            return ResponseEntity.ok(actualizada);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Método para eliminar una moto de la base de datos
    @DeleteMapping("/eliminar-motos/{id}")
    public void eliminarMoto(@PathVariable Long id) {
        motoRepository.deleteById(id);
    }
}