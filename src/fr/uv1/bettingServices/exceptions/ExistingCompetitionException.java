package fr.uv1.bettingServices.exceptions;
/**
 *
 */
public class ExistingCompetitionException extends Exception{

	public ExistingCompetitionException(){
		System.out.println("competition exists");
	}

	public ExistingCompetitionException(String message){
		//constructor whit error message
		super(message);
	}


}
