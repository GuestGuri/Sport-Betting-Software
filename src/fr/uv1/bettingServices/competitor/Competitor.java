package fr.uv1.bettingServices.competitor;


/**
 */

public abstract class Competitor implements fr.uv1.bettingServices.Competitor{
	protected int idCompetitor;



	/**
	 *
	 * @return idCompetitor
	 */
	public int getIdCompetitor(){
		return idCompetitor;
	}
	/**
	 *
	 * @param id
	 */
	public void setIdCompetitor(int id){
		this.idCompetitor=id;
	}
}
