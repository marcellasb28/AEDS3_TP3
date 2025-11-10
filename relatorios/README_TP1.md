<p align="center">
  <img src="figuras_readme/brasao.jpg" alt="Brasão PUC Minas" width="150"/>
</p>

<div align="center">
  
# PONTIFÍCIA UNIVERSIDADE CATÓLICA DE MINAS GERAIS
### Instituto de Ciências Exatas e de Informática
### Curso de Ciência da Computação
---
</div>

# Relatório Trabalho Prático 01
## Algoritmos e Estruturas de Dados III

Este relatório descreve a primeira parte do Trabalho Prático da disciplina de AEDS III.

<br>

### Autores

* **Bernardo Ladeira Kartabil**
    * `bernardo.kartabil@sga.pucminas.br`
* **Marcella Santos Belchior**
    * `marcella.belchior@sga.pucminas.br`
* **Thiago Henrique Gomes Feliciano**
    * `1543790@sga.pucminas.br`
* **Yasmin Torres Moreira dos Santos**
    * `yasmin.santos.1484596@sga.pucminas.br`

---

### **Resumo**

O presente trabalho detalha o desenvolvimento do "PresenteFácil", um sistema de gerenciamento de listas de presentes implementado na linguagem Java. O projeto foi concebido como parte da disciplina de Algoritmos e Estruturas de Dados III, com o objetivo principal de aplicar conceitos avançados de persistência de dados e indexação em arquivos. O sistema suporta um CRUD (Create, Read, Update, Delete) completo para as entidades de Usuários e Listas de Presentes. Para garantir a eficiência no acesso aos dados, foram implementadas e utilizadas estruturas de indexação externas, notadamente a Hash Extensível e a Árvore B+. A Hash Extensível é utilizada para criar índices diretos e indiretos, permitindo buscas rápidas por chaves primárias (ID) e secundárias (e-mail, código da lista). A Árvore B+ foi empregada para materializar o relacionamento 1:N entre usuários e suas respectivas listas, otimizando a consulta de "minhas listas". A arquitetura do sistema segue o padrão Model-View-Controller (MVC), separando a lógica de dados, a interface com o usuário (via console) e as regras de negócio.

**Palavras-chave:** Java, Estrutura de Dados, Hash Extensível, Árvore B+, CRUD, Persistência de Dados.

---

## Sumário
1.  [INTRODUÇÃO](#1-introdução)
2.  [DESENVOLVIMENTO](#2-desenvolvimento)
    * [Estruturas de Dados e Persistência](#21-estruturas-de-dados-e-persistência)
3.  [CHECKLIST DE REQUISITOS](#3-checklist-de-requisitos)
    * [CRUD de usuários](#31-há-um-crud-de-usuários-que-funciona-corretamente)
    * [CRUD de listas](#32-há-um-crud-de-listas-que-funciona-corretamente)
    * [Árvore B+ para relacionamento 1:N](#33-há-uma-árvore-b-que-registre-o-relacionamento-1n-entre-usuários-e-listas)
    * [Visualização de listas por NanoID](#34-há-uma-visualização-das-listas-por-meio-de-um-código-nanoid)
    * [Sistema completo e funcional](#35-o-trabalho-está-completo-e-funcionando-sem-erros-de-execução)
    * [Originalidade do trabalho](#36-o-trabalho-é-original-e-não-a-cópia-de-um-trabalho-de-outro-grupo)
4.  [CONCLUSÃO](#4-conclusão)
5.  [REFERÊNCIAS](#referências)

---

## 1. INTRODUÇÃO
O desenvolvimento de sistemas que manipulam grandes volumes de dados de forma eficiente é um desafio central na engenharia de software. A escolha de estruturas de dados adequadas para armazenamento e recuperação de informações em memória secundária é fundamental para o desempenho e a escalabilidade de uma aplicação.

Neste contexto, o projeto "PresenteFácil" foi desenvolvido para aplicar na prática as teorias de organização de arquivos e estruturas de dados avançadas. O sistema simula uma aplicação real onde usuários podem se cadastrar, criar múltiplas listas de presentes para diferentes ocasiões e compartilhá-las com outras pessoas por meio de um código único.

O principal objetivo deste trabalho foi construir um sistema funcional que não dependesse de um banco de dados tradicional. Em vez disso, toda a persistência dos dados foi implementada diretamente em arquivos binários, gerenciados por meio de classes customizadas que controlam a alocação de espaço, o tratamento de registros de tamanho variável e a reutilização de espaços liberados por registros excluídos. A eficiência das operações de busca, inserção e remoção é garantida pelo uso de arquivos de índice baseados em Hash Extensível e Árvore B+.

## 2. DESENVOLVIMENTO
O sistema foi estruturado seguindo o padrão arquitetural Model-View-Controller (MVC) para promover a organização e a manutenibilidade do código. Além disso, foi criado um pacote específico (`aeds3`) para abrigar as estruturas de dados genéricas.

### 2.1 Estruturas de Dados e Persistência
A persistência de dados é o núcleo técnico do trabalho. Foi implementada uma solução de armazenamento baseada em arquivos de acesso aleatório (`RandomAccessFile`).

#### 2.1.1 Arquivo.java
Esta é uma classe genérica que serve como base para a persistência de qualquer registro. Dada a sua complexidade, a análise da classe será dividida em partes. A estrutura fundamental inclui os atributos de controle e o construtor que inicializa o índice direto. O método `create` lida com a inserção e reutilização de espaço.

O método de leitura utiliza o índice primário para encontrar o endereço do registro e o lê, retornando o objeto. Já o método de exclusão localiza o registro, marca o espaço com uma lápide ("*") e gerencia a lista de espaços vazios para futura reutilização.

Por fim, o método `update` controla a atualização de registros, realocando-os quando necessário.

![Estrutura e construtor da classe Arquivo.java](figuras_readme/Figura1.png)
**Figura 1**: *Estrutura e construtor da classe `Arquivo.java`. Fonte: Elaborado pelos autores.*

<br>

![Lógica de criação de registros em Arquivo.java](figuras_readme/Figura2.png)
**Figura 2**: *Lógica de criação de registros em `Arquivo.java`. Fonte: Elaborado pelos autores.*

<br>

![Método de leitura em Arquivo.java](figuras_readme/Figura3.png)
**Figura 3**: *Método de leitura em `Arquivo.java`. Fonte: Elaborado pelos autores.*

<br>

![Método de exclusão em Arquivo.java](figuras_readme/Figura4.png)
**Figura 4**: *Método de exclusão em `Arquivo.java`. Fonte: Elaborado pelos autores.*

<br>

![Lógica de atualização de registros em Arquivo.java](figuras_readme/Figura5.png)
**Figura 5**: *Lógica de atualização de registros em `Arquivo.java`. Fonte: Elaborado pelos autores.*

<br>

## 3. CHECKLIST DE REQUISITOS
A seguir, são apresentadas as respostas ao checklist proposto para a avaliação do trabalho, com cada item detalhado em uma subseção para maior clareza.

### 3.1 Há um CRUD de usuários que funciona corretamente?
**Sim.** A classe `ArquivoUsuario`, que herda da classe genérica `Arquivo`, é a responsável por gerenciar a persistência e indexação dos usuários. Sua funcionalidade será detalhada a seguir.

Primeiramente, a estrutura da classe inclui um índice indireto, uma `HashExtensivel`, para mapear o e-mail do usuário ao seu ID. O construtor inicializa este índice e o método `create` é sobrescrito para garantir que, ao criar um usuário, seu e-mail e ID sejam devidamente registrados no índice.

![Evidência em Código: Estrutura, construtor e criação em ArquivoUsuario.java](figuras_readme/Figura6.png)
**Figura 6**: *Evidência em Código: Estrutura, construtor e criação em `ArquivoUsuario.java`. Fonte: Elaborado pelos autores.*

<br>

A principal vantagem desse índice é permitir a leitura de um usuário diretamente pelo seu e-mail. O método `read(String email)` utiliza o índice para encontrar o ID correspondente ao e-mail e, em seguida, chama o método de leitura por ID da classe pai.

![Evidência em Código: Leitura de usuário por e-mail](figuras_readme/Figura7.png)
**Figura 7**: *Evidência em Código: Leitura de usuário por e-mail. Fonte: Elaborado pelos autores.*

<br>

Para manter a integridade do índice, os métodos `delete` e `update` também são sobrescritos. Eles garantem que qualquer remoção ou alteração de um usuário (principalmente do seu e-mail) seja refletida no arquivo de índice.

![Evidência em Código: Manutenção do índice nos métodos delete e update](figuras_readme/Figura8.png)
**Figura 8**: *Evidência em Código: Manutenção do índice nos métodos delete e update. Fonte: Elaborado pelos autores.*

<br>

A figura a seguir mostra a prova de execução da criação de um novo usuário através do menu do sistema no terminal.

![Prova de Execução: Tela de criação de um novo usuário](figuras_readme/Figura9.jpg)
**Figura 9**: *Prova de Execução: Tela de criação de um novo usuário. Fonte: Elaborado pelos autores.*

<br>

### 3.2 Há um CRUD de listas que funciona corretamente?
**Sim.** De forma análoga, a classe `ArquivoLista` estende `Arquivo` e implementa todas as operações de CRUD para a entidade `Lista`. A imagem abaixo demonstra a criação de uma nova lista para um usuário logado.

![Evidência em Código: Índices de ArquivoLista.java](figuras_readme/Figura10.png)
**Figura 10**: *Evidência em Código: Índices de `ArquivoLista.java`. Fonte: Elaborado pelos autores.*

<br>

![Prova de Execução: Tela de criação de uma nova lista](figuras_readme/Figura11.png)
**Figura 11**: *Prova de Execução: Tela de criação de uma nova lista. Fonte: Elaborado pelos autores.*

<br>

### 3.3 Há uma árvore B+ que registre o relacionamento 1:N entre usuários e listas?
**Sim.** A classe `ArquivoLista` utiliza uma `ArvoreBMais<ParIntInt>` para armazenar pares de `(idUsuario, idLista)`. A prova de que a árvore funciona é a funcionalidade "Minhas Listas", que consulta a árvore para exibir apenas as listas do usuário logado.

![Evidência em Código: Uso da Árvore B+ em ArquivoLista.java](figuras_readme/Figura12.png)
**Figura 12**: *Evidência em Código: Uso da Árvore B+ em `ArquivoLista.java`. Fonte: Elaborado pelos autores.*

<br>

![Prova de Execução: Tela "Minhas Listas" mostrando o resultado da consulta](figuras_readme/Figura13.png)
**Figura 13**: *Prova de Execução: Tela "Minhas Listas" mostrando o resultado da consulta. Fonte: Elaborado pelos autores.*

<br>

### 3.4 Há uma visualização das listas por meio de um código NanoID?
**Sim.** A funcionalidade "Buscar Lista" solicita um código ao usuário e utiliza o índice indireto para encontrá-la. A execução dessa busca é mostrada no terminal.

![Evidência em Código: Método de busca por código](figuras_readme/Figura14.png)
**Figura 14**: *Evidência em Código: Método de busca por código. Fonte: Elaborado pelos autores.*

<br>

![Prova de Execução: Busca de uma lista pelo seu código NanoID](figuras_readme/Figura15.jpg)
**Figura 15**: *Prova de Execução: Busca de uma lista pelo seu código NanoID. Fonte: Elaborado pelos autores.*

<br>

### 3.5 O trabalho está completo e funcionando sem erros de execução?
**Sim.** O sistema implementa todas as funcionalidades propostas. A imagem abaixo mostra o menu inicial do programa em execução, que serve como ponto de partida para todas as outras operações, demonstrando que o sistema inicia e opera corretamente.

![Prova de Execução: Menu inicial da aplicação](figuras_readme/Figura16.png)
**Figura 16**: *Prova de Execução: Menu inicial da aplicação. Fonte: Elaborado pelos autores.*

<br>

### 3.6 O trabalho é original e não a cópia de um trabalho de outro grupo?
**O trabalho é original.** Todos os integrantes do grupo trabalharam arduamente para produzir esse projeto, com o objetivo de exercitar e fixar o conteúdo aprendido na disciplina de Algoritmos e Estruturas de Dados 3.

## 4. CONCLUSÃO
O desenvolvimento do projeto "PresenteFácil" permitiu a aplicação prática de conceitos complexos de organização de arquivos e estruturas de dados e a implementação de um sistema de persistência manual, com gerenciamento de índices por meio de Hash Extensível e Árvores B+.

---

### REFERÊNCIAS
* KUTOVA, M. **Implementação da Estrutura de Dados Árvore B+**. 2021. Código fonte fornecido na disciplina de Algoritmos e Estruturas de Dados III. Citado no cabeçalho do arquivo `ArvoreBMais.java`.
* KUTOVA, M. **Implementação da Tabela Hash Extensível**. 2021. Código fonte fornecido na disciplina de Algoritmos e Estruturas de Dados III. Citado no cabeçalho do arquivo `HashExtensivel.java`.
