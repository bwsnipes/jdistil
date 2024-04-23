package com.bws.jdistil.security.app.domain;

import com.bws.jdistil.core.process.model.DeleteDataObject;
import com.bws.jdistil.security.app.configuration.FieldIds;
import com.bws.jdistil.security.domain.Domain;
import com.bws.jdistil.security.domain.DomainManager;

/**
 * Deletes a Domain using submitted data.
 */
public class DeleteDomain extends DeleteDataObject<Integer, Domain> {

  /**
   * Creates a new DeleteDomain object.
   */
  public DeleteDomain() {
    super(DomainManager.class, FieldIds.DOMAIN_ID, ViewDomains.class);
  }

}
