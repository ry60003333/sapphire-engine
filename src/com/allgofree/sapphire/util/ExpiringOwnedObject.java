/*
 * Copyright (C) 2015 Ryan Rule-Hoffman <ryan.rulehoffman@icloud.com>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package com.allgofree.sapphire.util;

import net.allgofree.events.EventWrapper;

/**
 * An owned object that expires when there are no owners.
 * @author Ryan Rule-Hoffman <ryan.rulehoffman@icloud.com>
 */
public abstract class ExpiringOwnedObject extends OwnedObject
{
    
    /**
     * The wrapper for the expiration event.
     */
    private EventWrapper expireEvent;
    
    /**
     * {@inheritDoc}
     * @param owner {@inheritDoc}
     */
    @Override
    public void addOwner(ObjectOwner owner)
    {
        super.addOwner(owner);
        
        // Cancel expiration, if it is scheduled
        if (expireEvent != null)
        {
            // Debugging
            debug("Cancelling expiration event.");
            
            expireEvent.terminateEvent();
        }
    }
    
    /**
     * {@inheritDoc}
     * @param owner {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean removeOwner(ObjectOwner owner)
    {
        boolean removed = super.removeOwner(owner);
        
        if (removed)
        {
            // Check if there are no owners using this object
            if (getOwners().isEmpty())
            {
                // Trigger expiration of the object
                expireEvent = getExpireWrapper(owner);
            }
        }
        
        return removed;
    }
    
    /**
     * Create a new expire event and return the wrapper for it.
     * @param responsibleOwner The owner responsible for the expiration.
     * @return The event wrapper.
     */
    protected abstract EventWrapper getExpireWrapper(ObjectOwner responsibleOwner);
}
