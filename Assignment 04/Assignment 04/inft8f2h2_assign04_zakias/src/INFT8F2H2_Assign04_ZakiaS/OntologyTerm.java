/***************************************************************************
 *Bismillahir Rahmaanir Raheem
 *Almadadh Ya Gause Radi Allahu Ta'alah Anh - Ameen
 *Student Number : 208501583
 *Name : Zakia Salod
 *Course : INFT8F2H2
 *Assignment : 04
 *Masters of Medical Science - Medical Informatics
 *Year : 2016
 ***************************************************************************/

package INFT8F2H2_Assign04_ZakiaS;

public final class OntologyTerm {
	private final String medTerm;
    private final String medCategory;

    public OntologyTerm(String medTerm, String medCategory) {
        this.medTerm  = medTerm;
        this.medCategory = medCategory;
    }//end OntologyTerm() Constructor

    public String getMedTerm() {
        return medTerm;
    }

    public String getMedCategory() {
        return medCategory;
    }
}//end OntologyTerm class
