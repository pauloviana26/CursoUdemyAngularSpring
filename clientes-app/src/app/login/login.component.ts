import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { Usuario } from './usuario';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  username: string;
  password: string;
  cadastrando: boolean;
  mensagemSucesso: string;
  errors: String[];

  constructor(
    private router: Router,
    private authService: AuthService
  ) { }

  onSubmit () {
    this.authService
              .tentarLogar(this.username, this.password)
              .subscribe(res => {
                const access_token = JSON.stringify(res);
                localStorage.setItem('access_token',access_token)
                this.router.navigate(['/home']);
              }, errorResponse => {
                this.errors = ['Usuário e/ou senha incorreto(s).'];
              })
  }

  preparaCadastrar(event: { preventDefault: () => void; }) {
    event.preventDefault();
    this.cadastrando = true;
    this.errors = [];
  }

  cancelaCadastro() {
    this.cadastrando = false;
  }

  cadastrar() {
    const usuario: Usuario = new Usuario();
    usuario.username = this.username;
    usuario.password = this.password;
    this.authService
        .salvar(usuario)
        .subscribe(res => {
          this.mensagemSucesso = "Cadastro realizado com sucesso! Efetue o login";
          this.cadastrando = false;
          this.username = '';
          this.password = '';
          this.errors = [];
        }, errorResponse => {
          this.mensagemSucesso = null as any;
          this.errors = errorResponse.error.errors;
        })
  }
}
