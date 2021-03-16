import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Contato } from './contato/contato';

import { environment } from '../environments/environment'
import { Observable } from 'rxjs';
import { PaginaContato } from './contato/paginaContato';

@Injectable({
  providedIn: 'root'
})
export class ContatoService {

  url: string = environment.apiBaseUrl;

  constructor(
    private http: HttpClient
  ) { }

  save(contato: Contato) : Observable<Contato> {
    return this.http.post<Contato>(this.url, contato);
  }

list(page, size) : Observable<PaginaContato> {
    const params = new HttpParams().set('page', page).set('size',size);
    return this.http.get<any>(`${this.url}?${params.toString()}`);
  }

  favoritar(contato: Contato) : Observable<any> {
    return this.http.patch(`${this.url}/${contato.id}/favorito`, null);
  }

  upload(contato: Contato, formData: FormData) : Observable<any> {
    return this.http.put(`${this.url}/${contato.id}/foto`, formData, { responseType: 'blob' });
  }
}