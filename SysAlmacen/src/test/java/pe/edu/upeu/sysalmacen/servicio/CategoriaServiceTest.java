package pe.edu.upeu.sysalmacen.servicio;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upeu.sysalmacen.excepciones.CustomResponse;
import pe.edu.upeu.sysalmacen.excepciones.ModelNotFoundException;
import pe.edu.upeu.sysalmacen.modelo.Categoria;
import pe.edu.upeu.sysalmacen.repositorio.ICategoriaRepository;
import pe.edu.upeu.sysalmacen.servicio.impl.CategoriaServiceImp;
import java.util.List;
import java.util.Optional;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Pruebas unitarias - CategoriaService")
class CategoriaServiceTest {

    @Mock
    private ICategoriaRepository repo;

    @InjectMocks
    private CategoriaServiceImp categoriaService;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = Categoria.builder()
                .idCategoria(1L)
                .nombre("Electrónica")
                .build();
    }

    @Order(1)
    @DisplayName("Guardar categoria - debe retornar la categoria guardada")
    @Test
    void testSaveCategoria_RetornaCategoria() {
        given(repo.save(categoria)).willReturn(categoria);
        Categoria resultado = categoriaService.save(categoria);
        Assertions.assertThat(resultado).isNotNull();

        Assertions.assertThat(resultado.getNombre()).isEqualTo("Electrónica");
        then(repo).should(times(1)).save(categoria);
    }


}
