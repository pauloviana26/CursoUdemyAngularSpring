package io.github.pauloviana26.clientes.exception;

public class UsuarioCadastradoException extends RuntimeException {

    public UsuarioCadastradoException(String login) {
        super("Já existe cadastro com esse nome de usuário: " + login);
    }
}
