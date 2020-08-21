package it.finsiel.sbn.exception;

public class InfrastructureException extends Exception
{
  public InfrastructureException() {}
    
  public InfrastructureException(String message) { super(message); }

  public InfrastructureException(Exception e)    { super(e); }

  public InfrastructureException(Throwable t)    { super(t); }
}
