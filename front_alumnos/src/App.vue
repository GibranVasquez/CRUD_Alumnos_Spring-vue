<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import Swal from 'sweetalert2';

const motos = ref([]);
const editando = ref(false);
const isLoading = ref(true);

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
    isLoading.value = true;
    const response = await axios.get('http://localhost:8080/motos/traer-motos');
    motos.value = response.data;
  } catch (error) {
    console.error('Error cargando motos:', error);
    Swal.fire({
      icon: 'error',
      title: 'Error al cargar motos',
      text: 'No se pudieron cargar los datos de las motos'
    });
  } finally {
    isLoading.value = false;
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
  // Scroll to form
  document.getElementById('form-section').scrollIntoView({ behavior: 'smooth' });
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

onMounted(() => {
  // Add small delay to show loading animation
  setTimeout(cargarMotos, 500);
});
</script>

<template>
  <div class="container py-4">
    <!-- Form Section -->
    <div id="form-section" class="row animate__animated animate__fadeIn">
      <div class="col-lg-8 mx-auto">
        <div class="card border-0 shadow-sm mb-5">
          <div class="card-body p-4">
            <h2 class="text-center mb-4 fw-light">Gestión de Motos</h2>
            <form @submit.prevent="guardarMoto" class="needs-validation" novalidate>
              <div class="row g-3">
                <div class="col-md-6">
                  <label for="numeroSerie" class="form-label small text-muted">Número de Serie</label>
                  <input type="text" class="form-control rounded" 
                         id="numeroSerie" v-model="nuevaMoto.numeroSerie" required>
                </div>
                <div class="col-md-6">
                  <label for="marca" class="form-label small text-muted">Marca</label>
                  <input type="text" class="form-control rounded" 
                         id="marca" v-model="nuevaMoto.marca" required>
                </div>
                <div class="col-md-6">
                  <label for="modelo" class="form-label small text-muted">Modelo</label>
                  <input type="text" class="form-control rounded" 
                         id="modelo" v-model="nuevaMoto.modelo" required>
                </div>
                <div class="col-md-6">
                  <label for="cilindrada" class="form-label small text-muted">Cilindrada</label>
                  <input type="text" class="form-control rounded" 
                         id="cilindrada" v-model="nuevaMoto.cilindrada" required>
                </div>
                <div class="col-md-6">
                  <label for="color" class="form-label small text-muted">Color</label>
                  <input type="text" class="form-control rounded" 
                         id="color" v-model="nuevaMoto.color" required>
                </div>
                <div class="col-md-6">
                  <label for="año" class="form-label small text-muted">Año</label>
                  <input type="text" class="form-control rounded" 
                         id="año" v-model="nuevaMoto.año" required>
                </div>
                <div class="col-md-6">
                  <label for="precio" class="form-label small text-muted">Precio ($)</label>
                  <input type="number" class="form-control rounded" 
                         id="precio" v-model="nuevaMoto.precio" required>
                </div>
                <div class="col-md-6">
                  <label for="imagenURL" class="form-label small text-muted">Imagen URL</label>
                  <input type="text" class="form-control rounded" 
                         id="imagenURL" v-model="nuevaMoto.imagenURL">
                </div>
              </div>
              <div class="d-flex justify-content-end mt-4">
                <button type="button" class="btn btn-outline-secondary me-2" 
                        @click="resetForm" v-if="editando">
                  Cancelar
                </button>
                <button type="submit" class="btn btn-primary px-4">
                  {{ editando ? 'Actualizar' : 'Agregar' }}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>

    <!-- List Section -->
    <div class="row animate__animated animate__fadeIn animate__delay-1s">
      <div class="col-12">
        <div class="card border-0 shadow-sm">
          <div class="card-body p-4">
            <div class="d-flex justify-content-between align-items-center mb-4">
              <h3 class="card-title mb-0 fw-light">Listado de Motos</h3>
            </div>
            
            <div v-if="isLoading" class="text-center py-5">
              <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Cargando...</span>
              </div>
              <p class="mt-2 text-muted">Cargando motos...</p>
            </div>
            
            <div v-else class="table-responsive">
              <table class="table table-hover align-middle">
                <thead>
                  <tr class="text-muted border-bottom">
                    <th class="small">ID</th>
                    <th class="small">N° Serie</th>
                    <th class="small">Marca</th>
                    <th class="small">Modelo</th>
                    <th class="small">Cilindrada</th>
                    <th class="small">Color</th>
                    <th class="small">Año</th>
                    <th class="small">Precio</th>
                    <th class="small">Imagen</th>
                    <th class="small">Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(moto, index) in motos" :key="moto.id" 
                      class="border-bottom animate__animated animate__fadeIn" 
                      :style="`animation-delay: ${index * 0.05}s`">
                    <td class="text-muted">{{ moto.id }}</td>
                    <td>{{ moto.numeroSerie }}</td>
                    <td>{{ moto.marca }}</td>
                    <td>{{ moto.modelo }}</td>
                    <td>{{ moto.cilindrada }}</td>
                    <td>{{ moto.color }}</td>
                    <td>{{ moto.año }}</td>
                    <td>${{ moto.precio?.toLocaleString() }}</td>
                    <td>
                      <img v-if="moto.imagenURL" :src="moto.imagenURL" :alt="`${moto.marca} ${moto.modelo}`" 
                           class="img-fluid rounded" width="60" style="max-height: 40px; object-fit: cover;">
                      <span v-else class="text-muted small">-</span>
                    </td>
                    <td>
                      <div class="d-flex gap-2">
                        <button @click="editarMoto(moto)" class="btn btn-sm btn-outline-primary">
                          <i class="bi bi-pencil"></i>
                        </button>
                        <button @click="eliminarMoto(moto.id)" class="btn btn-sm btn-outline-danger">
                          <i class="bi bi-trash"></i>
                        </button>
                      </div>
                    </td>
                  </tr>
                </tbody>
              </table>
              <div v-if="motos.length === 0" class="text-center py-5 text-muted">
                No hay motos registradas
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
@import url('https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css');
@import url('https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css');

.card {
  background-color: #f8f9fa;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.card:hover {
  box-shadow: 0 5px 15px rgba(0,0,0,0.1);
}

.form-control {
  border: 1px solid #dee2e6;
  padding: 0.375rem 0.75rem;
}

.form-control:focus {
  box-shadow: 0 0 0 0.25rem rgba(13, 110, 253, 0.25);
  border-color: #86b7fe;
}

.btn {
  transition: all 0.2s ease;
  border-radius: 4px;
}

.btn-outline-primary:hover {
  background-color: #0d6efd;
  color: white;
}

.btn-outline-danger:hover {
  background-color: #dc3545;
  color: white;
}

.table {
  --bs-table-bg: transparent;
}

.table-hover tbody tr:hover {
  background-color: rgba(0,0,0,0.02);
}

.img-fluid {
  transition: transform 0.3s ease;
}

.img-fluid:hover {
  transform: scale(1.5);
}
</style>