package org.core.gui;

public class GUIValidator {
  public static boolean validateNumberInput(String input, int min, int max) {
    try {
      int number = Integer.parseInt(input);
      return number < min || number > max;

    } catch (NumberFormatException e) {
      System.out.println("Invalid number: " + input);
      return true;
    }
  }
}
