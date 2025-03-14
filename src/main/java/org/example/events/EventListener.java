package org.example.events;

@FunctionalInterface
public interface EventListener<T> {
  public void handleEvent(T event);
}
