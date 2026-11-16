# Mate Adrian 🐱💥

Um jogo *clicker* "invertido" feito em **Java**, com interface em **JavaFX**: em vez de juntar pontos sem fim, existe um inimigo só, o **Adrian**, com **10 trilhões de HP**, e o objetivo é zerar a vida dele.

> Projeto Final da disciplina de Programação Orientada a Objetos (Sistemas de Informação, 2º período).

## 👥 Integrantes

<!-- [TODO GRUPO] completar nome inteiro e matrícula de cada um -->

| Nome | Matrícula |
| --- | --- |
| Fellipe | |
| Rafael | |
| Maristela | |
| Adrian | |
| Gale | |

## 🎯 O problema

Fazer um clicker em que o progresso tem começo, meio e fim. Cada clique tira 1 de HP do Adrian, o que é nada perto de 10 trilhões, então o jogador precisa montar uma "equipe" de personagens que atacam sozinhos, cada um de um jeito diferente, e ir melhorando cada um deles.

O desafio de modelagem é esse: vários personagens que **compartilham** muita coisa (têm preço, são comprados, atacam de tempos em tempos, recebem melhorias) mas **se comportam** de formas bem diferentes na hora de atacar.

## 🕹️ Funcionalidades

- **Clique no Adrian**: causa dano. Todo dano causado vira **bufunfa** (o dinheiro do jogo).
- **Upgrades de clique**: *Dedo Calejado* (+1 de dano base) e *Mouse Gamer* (dobra o dano).
- **Upgrades passivos** (podem ser comprados várias vezes): *Cesar Pateta* (+1 de dano por segundo) e *Link furry* (+0,2 de bufunfa por segundo).
- **Auto clickers** (só pode ter **1 de cada**):

| Personagem | Como ataca | Melhorias próprias |
| --- | --- | --- |
| **Final Fefo** | Dano contínuo: queima o HP a cada tick. Parte do dano acumula e explode de tempos em tempos. | Brasa Forte (dano), Queima Rápida (velocidade), Pavio Curto (explode antes), Explosão Final (explosão mais forte) |
| **Furúnculos** | Carregam por vários segundos e soltam um golpe grande de uma vez. | Pus Concentrado (dano), Inflamação (carrega mais rápido), Mais Um (até 3 furúnculos) |
| **Conde Gale** | Dano base baixo, tudo baseado em crítico: chance de crítico + crítico garantido após alguns ataques sem critar. | Presas Afiadas (dano), Sede de Sangue (velocidade), Paciência Zero (garantido chega antes), Olho do Conde (+chance e +multiplicador) |
| **Mariestelar** | Ataque lento com dano enorme sorteado entre um mínimo e um máximo. | Massa Estelar (dano), Órbita Acelerada (ataca mais rápido), Caos Cósmico (máximo maior) |

- **Loja em duas abas**: "Loja" (tudo que dá pra comprar) e "Melhorias" (aparecem quando o personagem dono é comprado).
- **Arena**: os personagens comprados aparecem em volta do Adrian com barrinha de carga, status e números de dano subindo.
- **Salvar / Carregar / Novo jogo** com banco de dados SQLite.
- **Avisos na tela** para compra sem saldo, nível máximo, erro de banco e vitória.

## 🚀 Como instalar e executar

**Precisa de:** JDK 21 (ou mais novo) e internet na primeira vez (o Maven baixa o JavaFX e o driver do SQLite sozinho).

**Pelo terminal**, na pasta do projeto (onde está o `pom.xml`):

```
mvn clean javafx:run
```

**Pela IDE (IntelliJ ou Eclipse):** abrir a pasta como projeto Maven, esperar baixar as dependências e executar a classe `com.example.ac.Launcher`.

O save fica no arquivo `sample.db`, criado na pasta de onde o jogo foi executado.

<!-- [TODO GRUPO] rodar o comando acima em um PC "limpo" antes da apresentação.
     O projeto que não executar na avaliação não pontua a implementação. -->

## 🧩 Decisões de modelagem (conceitos obrigatórios)

### Hierarquia

```
Upgrade (abstract)                      tudo que aparece na loja
 ├── TesteUpgrade                       "Link furry": renda passiva
 ├── AutoDamager                        "Cesar Pateta": dano passivo
 ├── UpgradeDanoClique                  +dano base do clique
 ├── UpgradeMultiplicadorClique         multiplica o dano do clique
 ├── AutoClicker (abstract) implements Atributo     personagem que ataca sozinho
 │    ├── FinalFefo
 │    ├── Furunculo
 │    ├── CondeGale
 │    └── Maristelar
 └── MelhoriaAutoClicker (abstract)     upgrade que pertence a um AutoClicker
      ├── MelhoriaDano
      ├── MelhoriaVelocidade
      ├── MelhoriaCooldown
      └── MelhoriaEspecial

Atributo (interface)      aumentarDano / aumentarVelAtaque / diminuirCooldawn
Adrian, GameState         singletons: o inimigo e o estado global da partida
```

### Onde está cada conceito e por que foi usado

| Conceito | Onde | Por quê |
| --- | --- | --- |
| **Classes e objetos** | `Adrian` (o inimigo), `GameState` (estado da partida), `Upgrade` e filhas (itens da loja), `Renderer`/`Renderable`/`TextoFlutuante` (desenho), `SaveController`/`GameStateModel` (banco) | Cada classe tem uma responsabilidade só. Ex.: quem sabe tirar vida do Adrian é o `Adrian.receberDano()`, quem sabe cobrar é o `GameState.gastarBufunfa()`. |
| **Encapsulamento** | Atributos `private` em todas as classes. `Adrian.setVidaAtual()` trava a vida entre 0 e o máximo; `GameState.gastarBufunfa()` é o único jeito de gastar; `AutoClicker.setIntervalo()` é `private` e respeita um mínimo. | Nenhuma classe de fora consegue deixar o jogo num estado inválido (vida negativa, bufunfa negativa, ataque com intervalo zero). |
| **Coleção de objetos** | `LinkedHashMap<String, Upgrade>` e `ArrayList<AutoClicker>` na `HelloController`; `ArrayList<MelhoriaAutoClicker>` em cada `AutoClicker`; `LinkedHashMap` e `ArrayList<TextoFlutuante>` no `Renderer`; `HashMap<String, Integer>` no carregamento do save. | O mapa por id é o que permite salvar/carregar/resetar todos os upgrades com um `for`. A lista de auto clickers é percorrida pelo loop do jogo. |
| **Associação e interação** | `AutoClicker` **tem** uma lista de `MelhoriaAutoClicker`, e cada melhoria **conhece** o seu `alvo`. `Upgrade` tem um `Renderable`. `HelloController` usa `SaveController`, que usa `GameStateModel`. | A compra de uma melhoria é uma conversa entre objetos: célula da loja → `Upgrade.comprar()` → `GameState.gastarBufunfa()` → `MelhoriaDano.aplicar()` → `alvo.aumentarDano()`. |
| **Herança** | Árvore acima (3 níveis). | Um `FinalFefo` **é um** `AutoClicker`, que **é um** `Upgrade`. O que é comum sobe (preço, nível, compra em `Upgrade`; tempo, dano e intervalo em `AutoClicker`) e cada filha só escreve o que é dela. |
| **Polimorfismo** | `atacar()` (4 implementações), `diminuirCooldawn()` (padrão no `AutoClicker`, sobrescrito em `FinalFefo` e `CondeGale`), `executar()` (todas as filhas de `Upgrade`), `resetar()`, `getTextoStatus()`/`getProgresso()` usados pelo `Renderer`. | O loop faz `for (AutoClicker a : autoClickers) a.atualizar(dt)` sem saber quem é quem; a loja chama `upgrade.comprar()` igual pra 12 classes; o `Renderer` não precisa de `instanceof`. |
| **Classe abstrata / método abstrato** | `Upgrade` (`executar()`), `AutoClicker` (`atacar()`, `melhorarEspecial()`), `MelhoriaAutoClicker` (`aplicar()`, `getTipo()`). | Não existe "um upgrade genérico" nem "um auto clicker genérico" no jogo, então não faz sentido poder instanciá-los. O método abstrato obriga cada filha a definir a parte que só ela sabe. |
| **Interface** | `Atributo`, implementada por `AutoClicker` e usada pelas melhorias (`Atributo atributos = getAlvo();`). | É o contrato "sabe ser melhorado". Precisou ser interface (e não classe abstrata) porque os personagens já herdam de `Upgrade` e Java só permite herdar de uma classe. |

### Outras decisões

- **Compra centralizada em `Upgrade.comprar()`** (método `final`): confere nível máximo → cobra → sobe o nível → chama `executar()`. As filhas só implementam o efeito, sem repetir a checagem de saldo.
- **"Só 1 de cada auto clicker"** é o nível máximo 1 passado pelo construtor do `AutoClicker`. Não precisou de regra especial.
- **Um loop só** (10 passos por segundo) em vez de uma tarefa agendada por personagem. Cada `AutoClicker` soma o tempo e ataca quando passa do próprio intervalo, o que permite upgrades de velocidade.
- **Fonte única de verdade**: a vida mora só no `Adrian`; os números globais só no `GameState`.
- **Save por "replay"**: o banco guarda vida, bufunfa e o nível de cada upgrade. Ao carregar, o jogo é zerado e cada upgrade é reaplicado o número de vezes salvo (`Upgrade.restaurar()`), então nenhum atributo derivado precisa de coluna própria.

## ⭐ Recursos extras

| Recurso | Onde |
| --- | --- |
| **Tratamento de exceções** | Exceções próprias `BufunfaInsuficienteException` e `NivelMaximoException`: lançadas em `GameState`/`Upgrade`, tratadas em `UpgradeCellController` com mensagem na tela. `SQLException` do banco tratada em `HelloController.salvar()/carregar()` com `Alert`. Conexões e statements em *try-with-resources*. Sprite faltando cai num placeholder em `Renderable.carregarImagem()` em vez de derrubar o jogo. |
| **Interface gráfica** | JavaFX + FXML (`hello-view.fxml`, `listcell.fxml`), `ListView` com célula customizada, `Canvas` para a arena, *bindings* para vida/bufunfa/preços. |
| **Integração com banco de dados** | SQLite via JDBC: `SaveController` (conexão e ordem das operações) e `models/GameStateModel` (SQL). Tabelas `estado_do_jogo` e `upgrades_comprados`. |

<!-- [TODO GRUPO] Somos 5, então precisamos de pelo menos 3 recursos (até 1,0 cada).
     Os 3 acima estão funcionando. "Persistência de objetos" também é atendida pelo
     mesmo save em SQLite, mas vale perguntar pra professora se ela conta separado
     do item de banco de dados. "Integração com API" não foi feita. -->

## 🖼️ Sprites que faltam

Qualquer imagem que o código pedir e não existir aparece como um quadrado rosa e preto com "?" e o nome do arquivo sai no console (`[SPRITE FALTANDO] ...`). Para resolver é só salvar o PNG com o nome certo em `src/main/resources/com/example/ac/images/`, sem mexer em código.

| Arquivo esperado | O que é |
| --- | --- |
| `images/cliqueDanoIcone.png` | Ícone do *Dedo Calejado* na loja |
| `images/cliqueMultIcone.png` | Ícone do *Mouse Gamer* na loja |
| `images/furunculos2.png` | Dois furúnculos juntos (hoje só existe o de 1 e o de 3) |

Opcionais (o jogo funciona sem; o lugar de encaixar está marcado com `[TODO SPRITE]` no código):

- Adrian recortado sem fundo, Adrian tomando dano e Adrian derrotado (`HelloController`).
- Fundo da arena (`hello-view.fxml` / `Renderer`).
- Efeito de explosão do Final Fefo (`FinalFefo.atacar()`).
- Ícone próprio para cada melhoria (`MelhoriaAutoClicker`); hoje usam o ícone do personagem.

## 🔎 Marcações nos comentários do código

Dá pra achar tudo com a busca da IDE (Ctrl+Shift+F):

| Marcação | Significado |
| --- | --- |
| `[NOVO]` | Classe ou método que não existia na versão anterior |
| `[ATUALIZADO]` | Código que já existia e foi alterado (o comentário explica o quê e por quê) |
| `[FEITO]` | Um `TODO` antigo que foi resolvido |
| `[TODO GRUPO]` | Decisão ou tarefa que ainda está em aberto |
| `[TODO SPRITE]` | Lugar esperando uma imagem |
| `[BALANCEAMENTO]` | Números do jogo (preço, dano, tempo) pra ajustar testando |
| `[RUBRICA: ...]` | Onde um conceito da rubrica aparece |

## 🔜 Próximas ideias

- [ ] Ajustar o balanceamento jogando de verdade (os números atuais são um ponto de partida).
- [ ] Salvar automaticamente ao fechar.
- [ ] Tela de vitória.
- [ ] Sons.

---

Feito com Java, yakissoba e uma quantidade preocupante de **bufunfa**.
