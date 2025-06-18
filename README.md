# Aplicativo 09 (Final) - Jogo do Reflexo

Este projeto foi desenvolvido como trabalho final para a disciplina de Desenvolvimento de Aplicações para Dispositivos Móveis. O aplicativo é um jogo de reflexo com sistema de pontuação, recordes e histórico de jogadas, construído em Java para a plataforma Android.

## Funcionalidades Implementadas

* **Menu Principal:** Exibe o recorde atual, salvo localmente com `SharedPreferences`, e oferece opções para iniciar o jogo ou ver o histórico.
* **Múltiplos Modos de Jogo:**
    * **Modo Normal:** O jogador tem 60 segundos para clicar no alvo.
    * **Modo Hardcore:** O jogador tem 30 segundos e o alvo se move mais rapidamente.
* **Mecânica de Jogo:**
    * Um alvo circular aparece em posições aleatórias na tela.
    * A pontuação aumenta a cada clique correto no alvo.
    * A pontuação é exibida em tempo real durante a partida.
* **Sistema de Fim de Jogo:**
    * Ao final do tempo, um `AlertDialog` exibe a pontuação final e se um novo recorde foi estabelecido.
    * O jogador tem as opções de "Jogar Novamente" ou "Voltar ao Menu".
* **Persistência de Dados com SQLite:**
    * Cada partida finalizada é salva em um banco de dados `SQLite`.
    * Os dados salvos incluem: data da partida, pontuação, duração e se a jogada estabeleceu um novo recorde.
* **Tela de Histórico:**
    * Exibe uma lista com as últimas 10 partidas jogadas.
    * Mostra uma seção de destaque com os "Top 3 Recordes", exibindo as melhores pontuações que quebraram o recorde geral.
* **Recursos Adicionais (Extras):**
    * **Sons:** Efeitos sonoros para acertos, erros e ao final da partida.
    * **Animações:** Animações sutis para a exibição dos itens na lista de histórico.
    * **Ícones Diferenciados:** Ícones de troféu e estrela para diferenciar recordes do modo normal e hardcore no histórico.

## Tecnologias Utilizadas

* **Linguagem:** Java
* **IDE:** Android Studio
* **Principais Componentes e APIs:**
    * `RecyclerView` e `ListAdapter` para listas eficientes.
    * `SQLiteOpenHelper` para gerenciamento de banco de dados local.
    * `SharedPreferences` para persistência de dados simples (recorde).
    * `CountDownTimer` e `Handler` para controle de tempo e lógica de jogo.
    * `SoundPool` para efeitos sonoros de baixa latência.
    * `AlertDialog` para alertas interativos.
    * Animações de View e `Vector Assets`.

## Autor

* **Nome:** Gabriel Ykaro Rodrigues da Silva
* **Matrícula:** 20241012000255
* **Curso:** Análise e Desenvolvimento de Sistemas
