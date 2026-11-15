Dale GPT no README, vai cavalo cavalga ihhhiiiiriiir 🐎🐎🐎🐎
# Mate Adrian 🐱💥

Um jogo *clicker* feito em **Java**, com interface em **JavaFX**. A missão é simples: clicar, acumular bufunfa e comprar upgrades para deixar o Adrian cada vez mais danado.

> Projeto feito para experimentar JavaFX, persistência local e organização de código orientado a objetos — sem perder a zoeira.

## 🕹️ Como funciona

1. Clique para progredir no jogo.
2. Acumule **bufunfa**.
3. Compre upgrades disponíveis na loja.
4. Evolua seus atributos e acompanhe os preços dos upgrades.

## 🛠️ Tecnologias

- **Java** — lógica do jogo e orientação a objetos.
- **JavaFX + FXML** — interface gráfica e componentes da loja.
- **SQLite + JDBC** — armazenamento local do estado do jogo.

## 🧩 Organização do projeto

| Componente | Responsabilidade |
| --- | --- |
| `GameState` | Centraliza os dados compartilhados do jogo (*Singleton*). |
| `Upgrade` | Define as informações e o comportamento dos upgrades. |
| `UpgradeCellController` | Apresenta cada upgrade na `ListView` e recebe os cliques de compra. |
| Camada de persistência | Cria a estrutura SQLite e salva o estado do jogo via JDBC. |

Os upgrades podem implementar seu próprio comportamento de compra por meio de polimorfismo. O preço pode ser exposto como uma `DoubleProperty`, permitindo atualizar o `Label` automaticamente com *binding* do JavaFX.

## 💾 Salvamento

O estado do jogo é armazenado em um banco SQLite local, com informações como:

- `adrian_danado`: progresso/atributo do Adrian;
- `bufunfa`: recursos acumulados.

A tabela `estado_do_jogo` é criada caso ainda não exista. O caminho do arquivo `.db` depende da configuração da conexão JDBC.

## 🚀 Como executar

1. Tenha um **JDK compatível com a versão do JavaFX** utilizada pelo projeto.
2. Abra o projeto em uma IDE Java.
3. Configure as dependências do **JavaFX** e do driver **SQLite JDBC**, se ainda não estiverem configuradas.
4. Execute a classe principal da aplicação JavaFX.

> Os comandos exatos de compilação e execução dependem de como o projeto está configurado (Maven, Gradle ou configuração manual da IDE).

## 🔜 Próximas ideias

- [ ] Expandir a variedade de upgrades e seus efeitos.
- [ ] Adicionar progressão automática por tempo.
- [ ] Aprimorar o carregamento e o salvamento do estado.
- [ ] Refinar os sprites e a interface da loja.

---

Feito com Java, yakissoba e uma quantidade preocupante de **bufunfa**.
