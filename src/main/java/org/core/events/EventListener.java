package org.core.events;

@FunctionalInterface
public interface EventListener<T> {
  void handleEvent(T event);
}
