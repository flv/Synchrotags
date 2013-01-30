package fr.voltanite.noeud;

public class NoMatchableNodeException extends Exception {

	String message;
	//----------------------------------------------
	// Default constructor - initializes instance variable to unknown
	  public NoMatchableNodeException()
	  {
	    super();      
	    message = "NoMatchableNode for that request";
	  }
	  
	  public NoMatchableNodeException(String mess)
	  {
		  super();
		  message = mess;
	  }
	  
	//------------------------------------------------  
	// public method, callable by exception catcher. It returns the error message.
	  public String getMessage()
	  {
	    return message;
	  }
}





					