<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import Swal from 'sweetalert2';

const alumnos = ref([]);

const nuevoAlumno = ref({
  id: null,
  nombre: '',
  apellido: '',
  carrera: '',
  telefono: '',
  imagenURL: ''
});

const editado = ref(false);

const cargarAlumnos = async () => {
  try {
    const response = await axios.get('http://localhost:8080/alumnos/traer-alumnos');
    alumnos.value = response.data;
  } catch (error) {
    console.error('Error cargando alumnos:', error);
    Swal.fire({
      icon: 'error',
      title: 'Error al cargar alumnos',
      text: 'No se pudieron cargar los datos de los alumnos'
    });
  }
}

const agregarAlumno = async () => {
  try {
    if (editado.value) {
      await axios.put(`http://localhost:8080/alumnos/editar-alumnos/${nuevoAlumno.value.id}`, nuevoAlumno.value);
      Swal.fire({
        icon: 'success',
        title: 'Alumno actualizado correctamente',
        showConfirmButton: false,
        timer: 1500
      });
      editado.value = false;
    } else {
      await axios.post('http://localhost:8080/alumnos/insertar-alumnos', nuevoAlumno.value);
      Swal.fire({
        icon: 'success',
        title: 'Alumno agregado correctamente',
        showConfirmButton: false,
        timer: 1500
      });
    }

    await cargarAlumnos();
    nuevoAlumno.value = {
      id: null,
      nombre: '',
      apellido: '',
      carrera: '',
      telefono: '',
      imagenURL: ''
    };
  } catch (error) {
    console.error('Error al guardar alumno:', error);
    Swal.fire({
      icon: 'error',
      title: 'Error al guardar alumno',
      text: 'Por favor, inténtalo de nuevo'
    });
  }
}

const editarAlumno = (alumno) => {
  Object.assign(nuevoAlumno.value, alumno);
  editado.value = true;
}

const eliminarAlumno = async (id) => {
  const result = await Swal.fire({
    title: '¿Estás seguro de eliminar?',
    text: "¡No podrás revertir esto!",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Sí, eliminarlo'
  });

  if (result.isConfirmed) {
    try {
      await axios.delete(`http://localhost:8080/alumnos/eliminar-alumnos/${id}`);
      await cargarAlumnos();
      Swal.fire(
        '¡Eliminado!',
        'El alumno ha sido eliminado.',
        'success'
      );
    } catch (error) {
      console.error('Error al eliminar:', error);
      Swal.fire(
        'Error',
        'No se pudo eliminar el alumno',
        'error'
      );
    }
  }
}

onMounted(cargarAlumnos);
</script>

<template>
  <div class="container">
    <div class="row">
      <div class="col-md-12 mt-4">
        <div class="card shadow p-4 mb-4">
          <h2 class="text-center">Formulario de Motos</h2>
          <form @submit.prevent="agregarAlumno">
            <div class="col-md-6 mb-3">
              <label for="nombre" class="form-label">Nombre</label>
              <input type="text" class="form-control" id="nombre" v-model="nuevoAlumno.nombre" required>
            </div>
            <div class="row">
              <div class="col-md-6 mb-3">
                <label for="apellido" class="form-label">Apellidos</label>
                <input type="text" class="form-control" id="apellido" v-model="nuevoAlumno.apellido" required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="carrera" class="form-label">Carrera</label>
                <input type="text" class="form-control" id="carrera" v-model="nuevoAlumno.carrera" required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="telefono" class="form-label">Telefono</label>
                <input type="number" class="form-control" id="telefono" v-model="nuevoAlumno.telefono" required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="imagenURL" class="form-label">Imagen URL</label>
                <input type="text" class="form-control" id="imagenURL" v-model="nuevoAlumno.imagenURL">
              </div>
            </div>
            <button type="submit" class="btn btn-primary">
              {{ editado ? 'Actualizar Alumno' : 'Agregar Alumno' }}
            </button>
          </form>
        </div>
      </div>

      <div class="col-md-12">
        <div class="card shadow">
          <div class="card-body">
            <h2 class="card-title mb-3">Tabla de Motos</h2>
            <table class="table table-bordered table-striped">
              <thead class="table-dark">
                <tr>
                  <th scope="col">Id</th>
                  <th scope="col">Nombre</th>
                  <th scope="col">Apellidos</th>
                  <th scope="col">Carrera</th>
                  <th scope="col">telefono</th>
                  <th scope="col">Imagen</th>
                  <th scope="col">Acciones</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="alumno in alumnos" :key="alumno.id">
                  <td>{{ alumno.id }}</td>
                  <td>{{ alumno.nombre }}</td>
                  <td>{{ alumno.apellido }}</td>
                  <td>{{ alumno.carrera }}</td>
                  <td>{{ alumno.telefono }}</td>
                  <td><img :src="alumno.imagenURL" :alt="`Imagen de ${alumno.nombre}`" width="50"></td>
                  <td>
                    <button @click="eliminarAlumno(alumno.id)" class="btn btn-danger ms-2 mx-2">
                      <i class="bi bi-trash"></i>
                    </button>
                    <button @click="editarAlumno(alumno)" class="btn btn-warning">
                      <i class="bi bi-pencil"></i>
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* Estilos personalizados si los necesitas */
</style>