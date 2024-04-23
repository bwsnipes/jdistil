package com.bws.jdistil.security.app.domain;

import com.bws.jdistil.core.process.model.SaveDataObject;
import com.bws.jdistil.security.app.configuration.FieldIds;
import com.bws.jdistil.security.configuration.AttributeNames;
import com.bws.jdistil.security.domain.Domain;
import com.bws.jdistil.security.domain.DomainManager;

/**
 * Saves a Domain using submitted data.
 */
public class SaveDomain extends SaveDataObject<Integer, Domain> {

  /**
   * Creates a new SaveDomain object.
   */
  public SaveDomain() {
    super(Domain.class, DomainManager.class, FieldIds.DOMAIN_ID, AttributeNames.DOMAIN, 
        true, ViewDomains.class, EditDomain.class, false);
  }
  
}
