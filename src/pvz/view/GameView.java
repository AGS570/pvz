 	package view;

public interface GameView {
  // show methods
  void showGame();
  void showEndMessage();
  void showError(String message);
  void showMessage(String message);

  // get data from view
  String[] getPrompt();
}
