package com.bws.jdistil.project.facet;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.common.project.facet.core.IActionConfigFactory;

/**
 * Factory class used to create project configuration objects.
 * @author Bryan Snipes
 */
public class ProjectConfigurationFactory implements IActionConfigFactory {

  /**
   * Creates and returns a project configuration object.
   * @return Object Project configuration.
   */
	public Object create() throws CoreException {
		return new ProjectConfiguration();
	}

}
