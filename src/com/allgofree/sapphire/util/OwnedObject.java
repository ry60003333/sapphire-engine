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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An object that can be owned by multiple owners.
 * @author Ryan Rule-Hoffman <ryan.rulehoffman@icloud.com>
 */
public abstract class OwnedObject
{
    
    /**
     * The list of owners of the object.
     */
    private final ArrayList<ObjectOwner> owners;
    
    /**
     * Creates a new OwnedObject.
     */
    public OwnedObject()
    {
        owners = new ArrayList<ObjectOwner>();
    }
    
    /**
     * Get the unique ID of the object.
     * @return The ID of the object.
     */
    public abstract long getId();
    
    /**
     * Add an owner of the object.
     * @param owner The owner to add.
     */
    public void addOwner(ObjectOwner owner)
    {
        // Add the owner to the list
        owners.add(owner);
    }
    
    /**
     * Remove an owner of the object.
     * @param owner The owner to remove.
     * @return If the owner was in the list.
     */
    public boolean removeOwner(ObjectOwner owner)
    {
        // Remove the owner from the list
        return owners.remove(owner);
    }
    
    /**
     * Get the list of owners of the object.
     * @return The list of owners.
     */
    protected List<ObjectOwner> getOwners()
    {
        return Collections.unmodifiableList(owners);
    }
    
    /**
     * Perform cleanup tasks before the object is removed from it's manager.
     */
    public void cleanUp()
    {
        
    }
    
    /**
     * Print debug text.
     * @param text The text to print.
     */
    public void debug(String text)
    {
        
    }
}
