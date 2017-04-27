package fr.uv1.bettingServices.exceptions;
/**
 */
public class CompetitionException extends Exception{
		public CompetitionException(){
			//No message exception
			super();
		}
		public CompetitionException(String message){
			//constructor whit error message
			super(message);
		}
}
