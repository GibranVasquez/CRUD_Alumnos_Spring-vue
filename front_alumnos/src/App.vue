<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import Swal from 'sweetalert2';

const motos = ref([]);
const editando = ref(false);

const nuevaMoto = ref({
  id: null,
  numeroSerie: '',
  marca: '',
  modelo: '',
  cilindrada: '',
  color: '',
  año: '',
  imagenURL: '',
  precio: 0
});

const cargarMotos = async () => {
  try {
    const response = await axios.get('http://localhost:8080/motos/traer-motos');
    motos.value = response.data;
  } catch (error) {
    console.error('Error cargando motos:', error);
    Swal.fire({
      icon: 'error',
      title: 'Error al cargar motos',
      text: 'No se pudieron cargar los datos de las motos'
    });
  }
};

const guardarMoto = async () => {
  try {
    if (editando.value) {
      await axios.put(`http://localhost:8080/motos/editar-motos/${nuevaMoto.value.id}`, nuevaMoto.value);
      Swal.fire({
        icon: 'success',
        title: 'Moto actualizada correctamente',
        showConfirmButton: false,
        timer: 1500
      });
    } else {
      await axios.post('http://localhost:8080/motos/insertar-motos', nuevaMoto.value);
      Swal.fire({
        icon: 'success',
        title: 'Moto agregada correctamente',
        showConfirmButton: false,
        timer: 1500
      });
    }
    
    await cargarMotos();
    resetForm();
  } catch (error) {
    console.error('Error al guardar moto:', error);
    Swal.fire({
      icon: 'error',
      title: 'Error al guardar moto',
      text: 'Por favor, inténtalo de nuevo'
    });
  }
};

const editarMoto = (moto) => {
  nuevaMoto.value = { ...moto };
  editando.value = true;
};

const eliminarMoto = async (id) => {
  const result = await Swal.fire({
    title: '¿Estás seguro?',
    text: "¡No podrás revertir esta acción!",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Sí, eliminar',
    cancelButtonText: 'Cancelar'
  });

  if (result.isConfirmed) {
    try {
      await axios.delete(`http://localhost:8080/motos/eliminar-motos/${id}`);
      await cargarMotos();
      Swal.fire(
        '¡Eliminada!',
        'La moto ha sido eliminada.',
        'success'
      );
    } catch (error) {
      console.error('Error al eliminar:', error);
      Swal.fire(
        'Error',
        'No se pudo eliminar la moto',
        'error'
      );
    }
  }
};

const resetForm = () => {
  nuevaMoto.value = {
    id: null,
    numeroSerie: '',
    marca: '',
    modelo: '',
    cilindrada: '',
    color: '',
    año: '',
    imagenURL: '',
    precio: 0
  };
  editando.value = false;
};

onMounted(cargarMotos);
</script>

<template>
  <div class="container mt-4">
    <div class="row">
      <div class="col-md-12">
        <div class="card shadow p-4 mb-4">
          <h2 class="text-center mb-4">Gestión de Motos</h2>
          <form @submit.prevent="guardarMoto">
            <div class="row">
              <div class="col-md-6 mb-3">
                <label for="numeroSerie" class="form-label">Número de Serie</label>
                <input type="text" class="form-control" id="numeroSerie" v-model="nuevaMoto.numeroSerie" required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="marca" class="form-label">Marca</label>
                <input type="text" class="form-control" id="marca" v-model="nuevaMoto.marca" required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="modelo" class="form-label">Modelo</label>
                <input type="text" class="form-control" id="modelo" v-model="nuevaMoto.modelo" required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="cilindrada" class="form-label">Cilindrada</label>
                <input type="text" class="form-control" id="cilindrada" v-model="nuevaMoto.cilindrada" required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="color" class="form-label">Color</label>
                <input type="text" class="form-control" id="color" v-model="nuevaMoto.color" required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="año" class="form-label">Año</label>
                <input type="text" class="form-control" id="año" v-model="nuevaMoto.año" required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="precio" class="form-label">Precio ($)</label>
                <input type="number" class="form-control" id="precio" v-model="nuevaMoto.precio" required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="imagenURL" class="form-label">Imagen URL</label>
                <input type="text" class="form-control" id="imagenURL" v-model="nuevaMoto.imagenURL">
              </div>
            </div>
            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
              <button type="button" class="btn btn-secondary me-md-2" @click="resetForm" v-if="editando">
                Cancelar
              </button>
              <button type="submit" class="btn btn-primary">
                {{ editando ? 'Actualizar Moto' : 'Agregar Moto' }}
              </button>
            </div>
          </form>
        </div>
      </div>

      <div class="col-md-12">
        <div class="card shadow">
          <div class="card-body">
            <h3 class="card-title mb-4">Listado de Motos</h3>
            <div class="table-responsive">
              <table class="table table-striped table-hover">
                <thead class="table-dark">
                  <tr>
                    <th>ID</th>
                    <th>N° Serie</th>
                    <th>Marca</th>
                    <th>Modelo</th>
                    <th>Cilindrada</th>
                    <th>Color</th>
                    <th>Año</th>
                    <th>Precio</th>
                    <th>Imagen</th>
                    <th>Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="moto in motos" :key="moto.id">
                    <td>{{ moto.id }}</td>
                    <td>{{ moto.numeroSerie }}</td>
                    <td>{{ moto.marca }}</td>
                    <td>{{ moto.modelo }}</td>
                    <td>{{ moto.cilindrada }}</td>
                    <td>{{ moto.color }}</td>
                    <td>{{ moto.año }}</td>
                    <td>${{ moto.precio?.toLocaleString() }}</td>
                    <td>
                      <img v-if="moto.imagenURL" :src="moto.imagenURL" :alt="`${moto.marca} ${moto.modelo}`" 
                           class="img-thumbnail" width="80">
                      <span v-else class="text-muted">Sin imagen</span>
                    </td>
                    <td>
                      <button @click="editarMoto(moto)" class="btn btn-sm btn-warning me-2">
                        <i class="bi bi-pencil"></i> Editar
                      </button>
                      <button @click="eliminarMoto(moto.id)" class="btn btn-sm btn-danger">
                        <i class="bi bi-trash"></i> Eliminar
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
  </div>
</template>

<style scoped>
.img-thumbnail {
  max-height: 80px;
  object-fit: cover;
}
.card {
  border-radius: 10px;
}
.table-responsive {
  overflow-x: auto;
}
</style>