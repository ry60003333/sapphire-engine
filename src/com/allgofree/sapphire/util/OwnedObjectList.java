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
 * A list of objects that are owned by a parent.
 * @author Ryan Rule-Hoffman <ryan.rulehoffman@icloud.com>
 * @param <T> The class of OwnedObject to place in the list.
 */
public class OwnedObjectList<T extends OwnedObject>
{
    
    /**
     * The owner of the objects in the list.
     */
    private final ObjectOwner owner;
    
    /**
     * The underlying list of objects.
     */
    private final ArrayList<T> list;
    
    /**
     * Creates a new OwnedObjectList.
     * @param owner The owner of the objects in the list.
     */
    public OwnedObjectList(ObjectOwner owner)
    {
        this.owner = owner;
        list = new ArrayList<T>();
    }
    
    /**
     * Get an object by it's ID.
     * @param id The ID of the object.
     * @return The object.
     */
    public T getById(long id)
    {
        for (T next : getList())
        {
            if (next.getId() == id)
            {
                return next;
            }
        }
        return null;
    }
    
    /**
     * Add an object to the list.
     * @param object The object to add.
     */
    public void add(T object)
    {
        if (list.contains(object))
        {
            throw new IllegalArgumentException("Object is already in the list");
        }
        
        object.addOwner(owner);
        list.add(object);
    }
    
    /**
     * Remove an object from the list.
     * @param object The object to remove.
     * @return If the object was in the list.
     */
    public boolean remove(T object)
    {
        boolean removed = list.remove(object);
        
        if (removed)
        {
            object.removeOwner(owner);
        }
        
        return removed;
    }
    
    /**
     * Clear the list.
     */
    public void clear()
    {
        for (OwnedObject next : list)
        {
            next.removeOwner(owner);
        }
        list.clear();
    }
    
    /**
     * Get the list of objects.
     * @return The list of objects.
     */
    public List<T> getList()
    {
        return Collections.unmodifiableList(list);
    }
}
