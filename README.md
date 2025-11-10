<p align="center">
  <img src="relatorios/figuras_readme/tp2/brasao.jpg" alt="Brasão PUC Minas" width="150">
</p>

<h1 align="center">PONTIFÍCIA UNIVERSIDADE CATÓLICA DE MINAS GERAIS</h1>
<h3 align="center">Instituto de Ciências Exatas e de Informática</h3>
<h3 align="center">Curso de Ciência da Computação</h3>

<br>

# Relatório Trabalho Prático 02

## Algoritmos e Estruturas de Dados III

Este relatório descreve a segunda parte do Trabalho Prático da disciplina de AEDS III.

<br>

### Autores

  * **Bernardo Ladeira Kartabil**
       \* `bernardo.kartabil@sga.pucminas.br`
  * **Marcella Santos Belchior**
      \* `marcella.belchior@sga.pucminas.br`
  * **Thiago Henrique Gomes Feliciano**
      \* `1543790@sga.pucminas.br`
  * **Yasmin Torres Moreira dos Santos**
      \* `yasmin.santos.1484596@sga.pucminas.br`

-----

### **Resumo**

Este trabalho é a segunda fase de evolução do "PresenteFácil", um sistema de gerenciamento de listas de presentes em Java, desenvolvido para a disciplina de Algoritmos e Estruturas de Dados III.

O foco principal foi implementar um relacionamento N:N  entre as entidades Lista e Produto. Para isso, o sistema agora oferece operações CRUD completas para Produtos e para a nova entidade de associação, ListaProduto.

Para garantir um acesso eficiente aos dados, utilizamos estruturas de indexação externas:

## Sumário
1.  [INTRODUÇÃO](#1-introdução)
2.  [DESENVOLVIMENTO](#2-desenvolvimento)
    * [Estruturas de Dados e Persistência](#21-estruturas-de-dados-e-persistência)
3.  [CHECKLIST DE REQUISITOS](#3-checklist-de-requisitos)
    * [índice invertido](#31-O índice invertido com os termos dos nomes dos produts foi criado usando a classe ListaInvertida?)
    * [Busca de produtos - Manutenção de Produtos](#32-É possível buscar produtos por palavras no menu de manutenção de produtos?)
    * [Busca de produtos - Acrescentando- o à lista](#33-É possível buscar produtos por palavras na hora de acrescentá-los às listas dos usuários?)
    * [Compilação](#34-O trabalho compila corretamente?)
    * [Sistema completo e funcional](#35-o-trabalho-está-completo-e-funcionando-sem-erros-de-execução)
    * [Originalidade do trabalho](#36-o-trabalho-é-original-e-não-a-cópia-de-um-trabalho-de-outro-grupo)
4.  [CONCLUSÃO](#4-conclusão)
5.  [REFERÊNCIAS](#referências)

Árvore B+: Empregada para materializar e otimizar o relacionamento N:N, acelerando consultas cruciais como "quais produtos estão nesta lista?" e "em quais listas este produto aparece?".

