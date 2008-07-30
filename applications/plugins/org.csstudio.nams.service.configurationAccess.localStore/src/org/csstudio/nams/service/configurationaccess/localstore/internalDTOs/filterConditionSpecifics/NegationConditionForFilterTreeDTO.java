package org.csstudio.nams.service.configurationaccess.localstore.internalDTOs.filterConditionSpecifics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.csstudio.nams.service.configurationaccess.localstore.Mapper;
import org.csstudio.nams.service.configurationaccess.localstore.declaration.LocalStoreConfigurationService;
import org.csstudio.nams.service.configurationaccess.localstore.declaration.exceptions.InconsistentConfigurationException;
import org.csstudio.nams.service.configurationaccess.localstore.internalDTOs.FilterConditionDTO;

/**
 * Dieses FC wird verwendet, um andere Condtions im Filter zu verneinen. Diese
 * FC wird allerdings nicht in der Auflistung der FCs im Konfigurationswerkzeug
 * angezeigt!
 * 
 * <pre>
 * create table AMSFilterNegationCond4Filter (
 *    iFilterConditionRef			NUMBER(11) NOT NULL,
 *    iNegatedFCRef                 NUMBER(11) NOT NULL
 * );
 * </pre>
 * 
 * @author gs, mz
 */
@Entity
@PrimaryKeyJoinColumn(name = "iFilterConditionRef", referencedColumnName = "iFilterConditionID")
@Table(name = "AMSFilterNegationCond4Filter")
public class NegationConditionForFilterTreeDTO extends FilterConditionDTO
		implements HasManuallyJoinedElements {

	@SuppressWarnings("unused")
	@Column(name = "iNegatedFCRef", nullable = false)
	private int iNegatedFCRef;

	@Transient
	private FilterConditionDTO negatedFilterCondition;

	public NegationConditionForFilterTreeDTO() {
		setCName("NegationConditionForFilterTreeDTO");
		setCDesc("The type NegationConditionForFilterTreeDTO is not to be used directly! It is used internally on filters.");
	}

	/**
	 * Returns the negated FilterCondition.
	 * 
	 * @return The filter condition, not null.
	 */
	public FilterConditionDTO getNegatedFilterCondition() {
		return negatedFilterCondition;
	}

	/**
	 * TO BE USED FOR MAPPING PURPOSES!
	 * 
	 * Sets the negated condition.
	 */
	public void setNegatedFilterCondition(
			FilterConditionDTO negatedFilterCondition) {
		this.negatedFilterCondition = negatedFilterCondition;
		this.iNegatedFCRef = negatedFilterCondition.getIFilterConditionID();
	}

	/**
	 * TO BE USED BY {@link LocalStoreConfigurationService} ONLY!
	 * 
	 * Returns the database id of negated condition.
	 */
	public int getINegatedFCRef() {
		return this.iNegatedFCRef;
	}

	public void deleteJoinLinkData(Mapper mapper) throws Throwable {
		if (this.negatedFilterCondition instanceof HasManuallyJoinedElements) {
			if (this.negatedFilterCondition instanceof JunctorConditionForFilterTreeDTO
					|| this.negatedFilterCondition instanceof NegationConditionForFilterTreeDTO) {
				mapper.delete(this.negatedFilterCondition);
			} else {
				((HasManuallyJoinedElements) this.negatedFilterCondition)
					.deleteJoinLinkData(mapper);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void loadJoinData(Mapper mapper) throws Throwable {
		if (this.getIFilterConditionID() == this.getINegatedFCRef())
			throw new InconsistentConfigurationException("NegationConditionForFilterTree id == iNegatedFCRef");
		
		this.negatedFilterCondition = mapper.findForId(FilterConditionDTO.class, this.getINegatedFCRef(), true);
	}

	public void storeJoinLinkData(Mapper mapper) throws Throwable {
		if (this.negatedFilterCondition instanceof HasManuallyJoinedElements) {
			if (this.negatedFilterCondition instanceof JunctorConditionForFilterTreeDTO
					|| this.negatedFilterCondition instanceof NegationConditionForFilterTreeDTO) {

				FilterConditionDTO found = mapper.findForId(FilterConditionDTO.class, this.negatedFilterCondition.getIFilterConditionID(), false);

				if (found == null) {
					mapper.save(this.negatedFilterCondition);
				} else {
					if (found instanceof JunctorConditionForFilterTreeDTO) {
						JunctorConditionForFilterTreeDTO oldJCFFT = (JunctorConditionForFilterTreeDTO) found;
						JunctorConditionForFilterTreeDTO newJCFFT = (JunctorConditionForFilterTreeDTO) this.negatedFilterCondition;
						oldJCFFT.setCDesc(newJCFFT.getCDesc());
						oldJCFFT.setCName(newJCFFT.getCName());
						oldJCFFT.setIGroupRef(newJCFFT.getIGroupRef());
						oldJCFFT.setOperands(newJCFFT.getOperands());
						oldJCFFT.setOperator(newJCFFT.getOperator());
						mapper.save(oldJCFFT);
					} else {
						NegationConditionForFilterTreeDTO oldNot = (NegationConditionForFilterTreeDTO) found;
						NegationConditionForFilterTreeDTO newNot = (NegationConditionForFilterTreeDTO) this.negatedFilterCondition;

						oldNot.setCDesc(newNot.getCDesc());
						oldNot.setCName(newNot.getCName());
						oldNot.setIGroupRef(newNot.getIGroupRef());
						oldNot.setNegatedFilterCondition(newNot
								.getNegatedFilterCondition());
						mapper.save(oldNot);
					}
				}
				this.iNegatedFCRef = this.negatedFilterCondition
						.getIFilterConditionID();
			} else {

				((HasManuallyJoinedElements) this.negatedFilterCondition)
						.storeJoinLinkData(mapper);
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + iNegatedFCRef;
		result = prime
				* result
				+ ((negatedFilterCondition == null) ? 0
						: negatedFilterCondition.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof NegationConditionForFilterTreeDTO))
			return false;
		final NegationConditionForFilterTreeDTO other = (NegationConditionForFilterTreeDTO) obj;
		if (iNegatedFCRef != other.iNegatedFCRef)
			return false;
		if (negatedFilterCondition == null) {
			if (other.negatedFilterCondition != null)
				return false;
		} else if (!negatedFilterCondition.equals(other.negatedFilterCondition))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": iFilterConditionRef="
				+ this.iNegatedFCRef + ", assigned FC by mapping: "
				+ this.negatedFilterCondition;
	}
}
