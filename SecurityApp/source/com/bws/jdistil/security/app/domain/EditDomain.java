package com.bws.jdistil.security.app.domain;

import com.bws.jdistil.core.process.model.EditDataObject;
import com.bws.jdistil.security.app.configuration.FieldIds;
import com.bws.jdistil.security.app.configuration.PageIds;
import com.bws.jdistil.security.configuration.AttributeNames;
import com.bws.jdistil.security.domain.Domain;
import com.bws.jdistil.security.domain.DomainManager;

/**
 * Retrieves data needed to add or edit a Domain.
 */
public class EditDomain extends EditDataObject<Integer, Domain> {

  /**
   * Creates a new EditDomain object.
   */
  public EditDomain() {
    super(Domain.class, DomainManager.class, FieldIds.DOMAIN_ID, AttributeNames.DOMAIN, PageIds.DOMAIN, ViewDomains.class, false);
  }

}
