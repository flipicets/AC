
Projeto Final da disciplina de Programação Orientada a Objetos (Sistemas de Informação, 2º período).

Integrantes:
Fellipe Picetskei dos Santos 
Maristela Alves Lopes Lederer
Adrian Renan de Oliveira Amorim
Rafael Gaio Olesko
Galê Dreveniaki Venancio



Projeto:
Um jogo clicker no qual o objetico é derrotar um boss com 10 trilhoes de vida, o Adrian, cada click é -1 de vida e +1 bufunfa, a qual pode ser usada para comprar companheiros (inspirados no grupo e amigos do grupo) e melhorias. Projeto feito na linguagem java, com interface gráfica feita com JavaFX e persistência de objetos com banco de dados. 



Problema:
Fazer um clicker em que o progresso tem começo, meio e fim. Cada clique tira 1 de HP do Adrian, o que é nada perto de 10 trilhões, então o jogador precisa montar uma equipe de personagens que atacam sozinhos, cada um de um jeito diferente, e ir melhorando cada um deles.

O desafio de modelagem é esse: vários personagens com características compartilhadas(têm preço, compra, atacam de tempos em tempos, recebem melhorias) mas atacam de formas completamente diferentes.



Funcionalidades:
Clique no Adrian: causa dano. Todo dano causado vira bufunfa (o dinheiro do jogo).

Upgrades de clique (podem ser comprados várias vezes): "AAAIIAIIII IAIAIII QUE DOOOORRRRR" causa +1 de dano do clique, "Mouse Gamer" dobra do dano do clique, "Zaralha" +1 clique a cada cique, "Dedo de Midas" dobra a quantidade possuida de cliques (mouse gamer). Todos esses upgrades podem ser comprados mais de uma vez

Upgrades passivos (podem ser comprados várias vezes): "Cesar Pateta" +1 de dano por segundo  e "Link furry" +0,2 de bufunfa por clique.

Auto clickers (só pode ter 1 de cada):

"Final Fefo" dps de dano freuqnte, ataca a cada 1s (tick) e tem um ataque de esplosão a cada 15 ticks, possui 4 melhorias: aumento de dano por tick, ataca 20% mais rápido, explode com menos ticks, explosão +0.5x. Algumas dessas melhorias não possuem nível máximo e podem ser compradas ilimitadamente.

"Mariestelar": causa dano dependendo do seu atributo de sorte, podendo váriar entre 300 a 6K em seu nível base, ataca a cada 6s e possui 3 melhorias: +50% no dano mínimo e máximo, ataca 15% mais rápido, dano máximo +50% (mais sorte), apenas a melhoria referente a velocidade de ataque tem limite de nível.

"Conde Galê": é um personagem focado em dano crítico, com 15% de dar chance crítico sendo esse dano 5X seu dano base e um crítico garantido a cada 5 ataques, ataca a cada 3s. Possui 4 melhorias: aumenta em 50% do dano base, ataca 15% mais rápido, crítico garantido chega antes, 5% de chance de crítico e +1X no crítico.

"Furunculos": É um personagem formado por 3 personagens, como uma party, mas atuam como 1, com aumento de dano base a cada compra, eles atacam 1X de 60 de dano em seu ataque base, possui 3 melhorias: "Quem é esse cara?" adiciona +1 furunculo ( aplica o dano base +1X, max 3), "Agregados agora" carregam 15% mais rápido, "Oiieee" +60% de dano no golpe.

Arena: Feito usando JavaFX, faz com que os autoclikers e upgrades passivos apareçam ao redor do Adrian 

Persistência de objeto: feito com banco de dados, permite salvar o jogo, carregar o último jogo salvo, e criar novo jogo que apaga o progresso do jogo tual e cria um novo jogo.



Instalar e executar:

Precisa de: JDK 21 (ou mais novo) e internet na primeira vez (o Maven baixa o JavaFX e o driver do SQLite sozinho).

Pelo terminal, na pasta do projeto (onde está o `pom.xml`):

```
mvn clean javafx:run
```

Pela IDE (IntelliJ ou Eclipse):abrir a pasta como projeto Maven, esperar baixar as dependências e executar a classe `com.example.ac.Launcher`.

O save fica no arquivo `sample.db`, criado na pasta de onde o jogo foi executado.

<!-- [TODO GRUPO] rodar o comando acima em um PC "limpo" antes da apresentação.
     O projeto que não executar na avaliação não pontua a implementação. -->



Hierarquia:

```
Upgrade (abstract)                      tudo que aparece na loja
 ├── TesteUpgrade                       "Link furry": renda passiva
 ├── AutoDamager                        "Cesar Pateta": dano passivo
 ├── UpgradeDanoClique                  +dano base do clique
 ├── UpgradeMultiplicadorClique         multiplica o dano do clique
 ├── UpgradeZaralhaCliques               aumenta a quanidades de cliques por clique
 ├── UpgradeMultiplicadorQntdClique      dupica o MultiplicadorClique
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

Onde está cada conceito e por que foi usado

Classes e objetos: Adrian (o inimigo), GameState (estado da partida), Upgrade e filhas (itens da loja), Renderer/Renderable/TextoFlutuante (desenho), SaveController/GameStateModel (banco) Cada classe tem uma responsabilidade só. Ex.: quem sabe tirar vida do Adrian é o Adrian.receberDano(), quem sabe cobrar é o GameState.gastarBufunfa(). 


Encapsulamento: Atributos private em todas as classes. Adrian.setVidaAtual() trava a vida entre 0 e o máximo; GameState.gastarBufunfa() é o único jeito de gastar; AutoClicker.setIntervalo() é private e respeita um mínimo. Nenhuma classe de fora consegue deixar o jogo num estado inválido (vida negativa, bufunfa negativa, ataque com intervalo zero). 


Coleção de objetos: LinkedHashMap<String, Upgrade> e ArrayList<AutoClicker> na HelloController; ArrayList<MelhoriaAutoClicker> em cada AutoClicker; LinkedHashMap e ArrayList<TextoFlutuante> no Renderer; HashMap<String, Integer> no carregamento do save. O mapa por id é o que permite salvar/carregar/resetar todos os upgrades com um for. A lista de auto clickers é percorrida pelo loop do jogo. 


Associação e interação: AutoClicker tem uma lista de MelhoriaAutoClicker, e cada melhoria conhece o seu alvo. Upgrade tem um Renderable. HelloController usa SaveController, que usa GameStateModel. A compra de uma melhoria é uma conversa entre objetos: célula da loja --> Upgrade.comprar() --> GameState.gastarBufunfa() --> MelhoriaDano.aplicar() --> alvo.aumentarDano().


Herança: Árvore acima (3 níveis). Um FinalFefo é um AutoClicker, que é um Upgrade. O que é comum sobe (preço, nível, compra em Upgrade; tempo, dano e intervalo em AutoClicker) e cada filha só escreve o que é dela. 


Polimorfismo: atacar() (4 implementações), diminuirCooldawn() (padrão no AutoClicker, sobrescrito em FinalFefo e CondeGale), executar() (todas as filhas de Upgrade), resetar(), getTextoStatus()/getProgresso() usados pelo Renderer. O loop faz for (AutoClicker a : autoClickers) a.atualizar(dt) sem saber quem é quem; a loja chama upgrade.comprar() igual pra 12 classes; o Renderer não precisa de instanceof. 


Classe abstrata / método abstrato: Upgrade (executar()), AutoClicker (atacar(), melhorarEspecial()), MelhoriaAutoClicker (aplicar(), getTipo()). Não existe "um upgrade genérico" nem "um auto clicker genérico" no jogo, então não faz sentido poder instanciá-los. O método abstrato obriga cada filha a definir a parte que só ela sabe.


Interface: Atributo, implementada por AutoClicker e usada pelas melhorias (Atributo atributos = getAlvo();). É o contrato "sabe ser melhorado". Precisou ser interface (e não classe abstrata) porque os personagens já herdam de Upgrade e Java só permite herdar de uma classe.


Outras decisões:

Compra centralizada em Upgrade.comprar() (método final): confere nível máximo --> cobra --> sobe o nível → chama executar(). As filhas só implementam o efeito, sem repetir a checagem de saldo.
Só 1 de cada auto clicker: é o nível máximo 1 passado pelo construtor do AutoClicker. Não precisou de regra especial.
Um loop só (10 passos por segundo) em vez de uma tarefa agendada por personagem. Cada AutoClicker soma o tempo e ataca quando passa do próprio intervalo, o que permite upgrades de velocidade.
Fonte única de verdade: a vida mora só no Adrian; os números globais só no GameState.
Save por "replay": o banco guarda vida, bufunfa e o nível de cada upgrade. Ao carregar, o jogo é zerado e cada upgrade é reaplicado o número de vezes salvo (Upgrade.restaurar()), então nenhum atributo derivado precisa de coluna própria.

Recursos extras


Tratamento de exceções: Exceções próprias BufunfaInsuficienteException e NivelMaximoException lançadas em GameState/Upgrade, tratadas em UpgradeCellController com mensagem na tela. 

SQLException do banco tratada em HelloController.salvar()/carregar() com Alert. Conexões e statements em try-with-resources. Sprite faltando cai num placeholder em Renderable.carregarImagem() em vez de derrubar o jogo.

Interface gráfica JavaFX + FXML (hello-view.fxml, listcell.fxml), ListView com célula customizada, Canvas para a arena, bindings para vida/bufunfa/preços. 

Integração com banco de dados SQLite via JDBC: SaveController (conexão e ordem das operações) e models/GameStateModel (SQL). Tabelas estado_do_jogo e upgrades_comprados. 





Feito com Java, yakissoba e uma quantidade preocupante de bufunfa.
