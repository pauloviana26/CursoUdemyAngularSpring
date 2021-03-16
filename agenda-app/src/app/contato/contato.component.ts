import { Component, OnInit } from '@angular/core';
import { ContatoService } from '../contato.service';
import { Contato } from './contato';

import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';

import { ContatoDetalheComponent } from '../contato-detalhe/contato-detalhe.component';
import { PageEvent } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-contato',
  templateUrl: './contato.component.html',
  styleUrls: ['./contato.component.css']
})
export class ContatoComponent implements OnInit {

  formulario: FormGroup;
  contatos: Contato[] = [];
  colunas = ['foto', 'id', 'nome', 'email', 'favorito'];
  totalElementos = 0;
  pagina = 0;
  tamanho = 10;
  pageSizeOptions: number[] = [10];

  constructor(
    private service: ContatoService,
    private fb: FormBuilder,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.montarFormulario();
    this.listarContatos(this.pagina, this.tamanho);
  }

  montarFormulario() {
    this.formulario = this.fb.group({
      nome: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]]
    })
  }

  listarContatos( pagina, tamanho ) {
    this.service.list(pagina, tamanho).subscribe(res => {
      this.contatos = res.content;
      this.totalElementos = res.totalElements;
      this.pagina = res.number;
    })
  }

  favoritar(contato: Contato) {
    this.service.favoritar(contato).subscribe(res => {
      contato.favorito = !contato.favorito;
    })
  }

  onSubmit() {
    const formValues = this.formulario.value
    const contato = new Contato(formValues.nome, formValues.email);
    this.service.save(contato).subscribe(res => {
      this.listarContatos(0, 10);
      this.snackBar.open('Contato adicionado com sucesso!', 'Sucesso!', {
        duration: 2000
      });
      this.formulario.reset();
    })
  }

  uploadFoto(event, contato) {
    const files = event.target.files;
    if(files) {
      const foto = files[0];
      const formData: FormData = new FormData();
      formData.append("foto", foto);
      this.service.upload(contato, formData)
                  .subscribe(res => {
                    this.listarContatos(0, 10);
                  })
    }
  }

  paginar(event: PageEvent) {
    this.pagina = event.pageIndex;
    this.listarContatos(this.pagina, this.tamanho);
  }

  visualizarContato(contato: Contato) {
    this.dialog.open( ContatoDetalheComponent, {
      width: '400px',
      height: '450px',
      data: contato
    })
  }

}
