/* 
 * The MIT License
 *
 * Copyright 2012 Universidad de Montemorelos A. C.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

highlightTableRows("lista");

function buscaPagina(paginaId) {
    $('input#pagina').val(paginaId);
    $('input#tipo').val('');
    $('input#correo').val('');
    document.forms["filtraLista"].submit();
}

function imprime(tipo) {
    $('input#tipo').val(tipo);
    $('input#correo').val('');
    document.forms["filtraLista"].submit();
}

function enviaCorreo(tipo) {
    $('#enviaCorreoBtn').button('loading');
    $('input#tipo').val('');
    $('input#correo').val(tipo);
    document.forms["filtraLista"].submit();
}

function ordena(campo) {
    if ($('input#order').val() == campo && $('input#sort').val() == 'asc') {
        $('input#sort').val('desc');
    } else {
        $('input#sort').val('asc');
    }
    $('input#order').val(campo);
    $('input#tipo').val('');
    $('input#correo').val('');
    document.forms["filtraLista"].submit();
}
