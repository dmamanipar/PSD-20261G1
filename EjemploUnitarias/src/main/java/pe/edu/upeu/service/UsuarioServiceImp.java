package pe.edu.upeu.service;

import lombok.RequiredArgsConstructor;
import pe.edu.upeu.PasswordValidator;
import pe.edu.upeu.model.Usuario;
import pe.edu.upeu.repository.UsuarioRepository;

@RequiredArgsConstructor
public class UsuarioServiceImp implements UsuarioService{
    private final PasswordValidator passwordValidator;
    private final UsuarioRepository usuarioRepository;


    @Override
    public Usuario registrarUsuario(Usuario usuario) {
        if(usuario==null){throw new IllegalArgumentException("El usuario no puede ser nulo");}
        if(!passwordValidator.isValid(usuario.getPassword())){
            throw new IllegalArgumentException("El password no cumple con el nivel de seguridad");
        }
        if(usuarioRepository.existByEmail(usuario.getEmail())){
            throw new IllegalArgumentException("El email ya esta registrado:"+usuario.getEmail());
        }
        return usuarioRepository.save(usuario);
    }
}
