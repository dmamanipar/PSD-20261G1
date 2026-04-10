package pe.edu.upeu.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pe.edu.upeu.PasswordValidator;
import pe.edu.upeu.model.Usuario;
import pe.edu.upeu.repository.UsuarioRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para UsuarioServiceImp
 * Aplica conceptos de Mock y Pruebas Parametrizadas
 */
class UsuarioServiceImpTest {

    @Mock
    private PasswordValidator passwordValidator;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImp usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    // ============================================
    // PRUEBAS PARAMETRIZADAS: Validación de Email Duplicado
    // ============================================

    @ParameterizedTest(name = "Email duplicado: {0}")
    @ValueSource(strings = {
            "juan.perez@upeu.edu.pe",
            "maria.garcia@upeu.edu.pe",
            "admin@upeu.edu.pe",
            "test@upeu.edu.pe"
    })
    @DisplayName("Debe lanzar excepción cuando el email ya está registrado")
    void registrarUsuario_EmailDuplicado_DebeLanzarExcepcion(String emailDuplicado) {
        // Arrange
        Usuario usuario = Usuario.builder()
                .username("juan")
                .email(emailDuplicado)
                .password("ValidPass123!")
                .build();

        when(passwordValidator.isValid(anyString())).thenReturn(true);
        when(usuarioRepository.existByEmail(emailDuplicado)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.registrarUsuario(usuario)
        );

        assertTrue(exception.getMessage().contains("El email ya esta registrado"));
        assertTrue(exception.getMessage().contains(emailDuplicado));

        verify(passwordValidator).isValid(usuario.getPassword());
        verify(usuarioRepository).existByEmail(emailDuplicado);
        verify(usuarioRepository, never()).save(any());
    }

    // ============================================
    // PRUEBAS PARAMETRIZADAS: Validación de Password
    // ============================================

    @ParameterizedTest(name = "Password inválido: {0} - Razón: {1}")
    @CsvSource({
            "123,                    'muy corto'",
            "password,               'sin mayúsculas ni números'",
            "PASSWORD,               'sin minúsculas ni números'",
            "12345678,               'solo números'",
            "Pass,                   'muy corto'",
            "'',                     'vacío'",
            "P@ssw0rd!ExtraLargo123, 'longitud excesiva'"
    })
    @DisplayName("Debe lanzar excepción cuando el password no cumple seguridad")
    void registrarUsuario_PasswordInvalido_DebeLanzarExcepcion(String passwordInvalido, String razon) {
        // Arrange
        Usuario usuario = Usuario.builder()
                .username("Test User")
                .email("nuevo@upeu.edu.pe")
                .password(passwordInvalido)
                .build();

        when(passwordValidator.isValid(passwordInvalido)).thenReturn(false);
        when(usuarioRepository.existByEmail(anyString())).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.registrarUsuario(usuario)
        );

        assertEquals("El password no cumple con el nivel de seguridad", exception.getMessage());

        verify(passwordValidator).isValid(passwordInvalido);
        verify(usuarioRepository, never()).existByEmail(anyString());
        verify(usuarioRepository, never()).save(any());
    }

    @ParameterizedTest(name = "Password válido: {0}")
    @ValueSource(strings = {
            "SecurePass123!",
            "MyP@ssw0rd2024",
            "Complex#Pass9",
            "Admin$2023Test"
    })
    @DisplayName("Debe aceptar passwords válidos según el validador")
    void registrarUsuario_PasswordValido_DebeProcesar(String passwordValido) {
        // Arrange
        Usuario usuario = Usuario.builder()
                .username("Test User")
                .email("nuevo@upeu.edu.pe")
                .password(passwordValido)
                .build();

        when(passwordValidator.isValid(passwordValido)).thenReturn(true);
        when(usuarioRepository.existByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        Usuario resultado = usuarioService.registrarUsuario(usuario);

        // Assert
        assertNotNull(resultado);
        assertEquals(passwordValido, resultado.getPassword());
        verify(passwordValidator).isValid(passwordValido);
        verify(usuarioRepository).existByEmail(usuario.getEmail());
        verify(usuarioRepository).save(usuario);
    }

    // ============================================
    // PRUEBAS PARAMETRIZADAS: Usuario Nulo
    // ============================================

    @ParameterizedTest
    @NullSource
    @DisplayName("Debe lanzar excepción cuando el usuario es nulo")
    void registrarUsuario_UsuarioNulo_DebeLanzarExcepcion(Usuario usuarioNulo) {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.registrarUsuario(usuarioNulo)
        );

        assertEquals("El usuario no puede ser nulo", exception.getMessage());

        verifyNoInteractions(passwordValidator);
        verifyNoInteractions(usuarioRepository);
    }

    // ============================================
    // PRUEBAS ESTÁNDAR: Flujo Exitoso
    // ============================================



    // ============================================
    // PRUEBAS ESTÁNDAR: Escenarios de Error Específicos
    // ============================================

    @Test
    @DisplayName("Debe lanzar excepción específica cuando email ya existe")
    void registrarUsuario_EmailExistente_DebeLanzarExcepcionEspecifica() {
        // Arrange
        String emailExistente = "existente@upeu.edu.pe";
        Usuario usuario = Usuario.builder()
                .username("Test")
                .email(emailExistente)
                .password("ValidPass123!")
                .build();

        when(passwordValidator.isValid(anyString())).thenReturn(true);
        when(usuarioRepository.existByEmail(emailExistente)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.registrarUsuario(usuario)
        );

        assertEquals("El email ya esta registrado:" + emailExistente, exception.getMessage());
    }

    @Test
    @DisplayName("Debe verificar orden de validaciones: primero password, luego email")
    void registrarUsuario_DebeValidarEnOrdenCorrecto() {
        // Arrange - password inválido, email existente
        Usuario usuario = Usuario.builder()
                .username("Test")
                .email("cualquiera@upeu.edu.pe")
                .password("invalid")
                .build();

        when(passwordValidator.isValid("invalid")).thenReturn(false);
        // No configuramos existByEmail porque no debería llegar ahí

        // Act
        assertThrows(IllegalArgumentException.class, () -> usuarioService.registrarUsuario(usuario));

        // Assert - verificar que solo se validó el password
        verify(passwordValidator).isValid("invalid");
        verify(usuarioRepository, never()).existByEmail(anyString());
    }

    // ============================================
    // PRUEBAS PARAMETRIZADAS: Múltiples Escenarios Combinados
    // ============================================

    @ParameterizedTest(name = "Caso {index}: {0}")
    @CsvSource({
            "NULL_USER,        'null',           'N/A',        'El usuario no puede ser nulo'",
            "INVALID_PASSWORD, 'nuevo@test.com', '123',        'El password no cumple con el nivel de seguridad'",
            "DUPLICATE_EMAIL,  'dup@upeu.edu',   'ValidPass1', 'El email ya esta registrado:dup@upeu.edu'"
    })
    @DisplayName("Debe manejar múltiples escenarios de error")
    void registrarUsuario_MultiplesEscenarios_DebeLanzarExcepcionApropiada(
            String escenario,
            String email,
            String password,
            String mensajeEsperado) {

        Usuario usuario;

        if ("NULL_USER".equals(escenario)) {
            usuario = null;
        } else {
            usuario = Usuario.builder()
                    .username("Test")
                    .email(email)
                    .password(password)
                    .build();

            if ("INVALID_PASSWORD".equals(escenario)) {
                when(passwordValidator.isValid(password)).thenReturn(false);
            } else if ("DUPLICATE_EMAIL".equals(escenario)) {
                when(passwordValidator.isValid(password)).thenReturn(true);
                when(usuarioRepository.existByEmail(email)).thenReturn(true);
            }
        }

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.registrarUsuario(usuario)
        );

        assertTrue(exception.getMessage().contains(mensajeEsperado.replace(":dup@upeu.edu", "")) ||
                exception.getMessage().equals(mensajeEsperado));
    }
}