package org.raise9.kernel.events;

public interface EventDataConvert {

  <T> EventTransactionSync<T> processEventData(EventTransactionSync event);

  boolean valid(EventTransactionSync eventTransactionSync);

  <T> T objectData(EventTransactionSync e);

}