package org.dieschnittstelle.jee.esa.ejb.ejbmodule.crm;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.*;
import javax.persistence.*;


import org.dieschnittstelle.jee.esa.entities.crm.CrmProductBundle;
import org.apache.log4j.Logger;

/**
 * provides shopping cart functionality
 *
 * note that this class is, at the same time, annotated as an entity class for supporting a RESTful handling
 * of shopping cart functionality
 *
 * note that instances of this class EITHER behave as stateful ejbs OR as entities
 */
@Stateful
@Entity
public class ShoppingCartStateful implements ShoppingCartRemote, ShoppingCartLocal {

	@Id
	@GeneratedValue
	private long id;
	
	protected static Logger logger = Logger.getLogger(ShoppingCartStateful.class);

	@OneToMany(cascade = CascadeType.ALL)
	private List<CrmProductBundle> productBundles = new ArrayList<CrmProductBundle>();
	
	public ShoppingCartStateful() {
		logger.info("<constructor>: " + this);
	}
	
	public void addProductBundle(CrmProductBundle product) {
		logger.info("addProductBundle(): " + product);

		// check whether we already have a bundle for the given product
		boolean bundleUpdate = false;
		for (CrmProductBundle bundle : productBundles) {
			if (bundle.getErpProductId() == product.getErpProductId()) {
				bundle.setUnits(bundle.getUnits()+product.getUnits());
				bundleUpdate = true;
				break;
			}
		}
		if (!bundleUpdate) {
			this.productBundles.add(product);
		}
	}
	
	public List<CrmProductBundle> getProductBundles() {
		logger.info("getProductBundles()");

		return this.productBundles;
	}

	// entity: access the id

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	// lifecycle ejb logging: jboss complains about usage of default transaction attribute (REQUIRED), hence we explicitly set allowed values

	@PostConstruct
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void beginn() {
		logger.info("@PostConstruct");
	}

	@PreDestroy
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void abschluss() {
		logger.info("@PreDestroy");
	}

	@PrePassivate
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void passiviere() {
		logger.info("@PrePassivate");
	}

	@PostActivate
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void aktiviere() {
		logger.info("@PostActivate");
	}

	// lifecycle entity logging:
	@PostLoad
	public void onPostLoad() {
		logger.info("@PostLoad: " + this);
	}

	@PostPersist
	public void onPostPersist() {
		logger.info("@PostPersist: " + this);
	}

	@PostRemove
	public void onPostRemove() {
		logger.info("@PostRemove: " + this);
	}

	@PostUpdate
	public void onPostUpdate() {
		logger.info("@PostUpdate: " + this);
	}

	@PrePersist
	public void onPrePersist() {
		logger.info("@PrePersist: " + this);
	}

	@PreRemove
	public void onPreRemove() {
		logger.info("@PreRemove: " + this);
	}

	@PreUpdate
	public void onPreUpdate() {
		logger.info("@PreUpdate: " + this);
	}


}
