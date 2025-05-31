package com.yardi.ejb.model;

import org.eclipse.persistence.config.DescriptorCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.internal.identitymaps.NoIdentityMap;

/**
 * A reusable cache customizer that will disable caching for any entity that references this class in the @Customizer() tag
 */
public class NoCacheCustomizer implements DescriptorCustomizer {

	@Override
	public void customize(ClassDescriptor descriptor) throws Exception {
        // Disable 2nd level caching entirely for this entity
        descriptor.setIdentityMapClass(NoIdentityMap.class); // Disables object-level caching
        descriptor.setShouldDisableCacheHits(true);           // Forces DB reads
	}

}
