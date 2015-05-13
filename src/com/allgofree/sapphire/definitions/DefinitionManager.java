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
package com.allgofree.sapphire.definitions;

import java.util.concurrent.ConcurrentHashMap;
import org.allgofree.mysql.MySQLConnectionWrapper;

/**
 * A definition manager.
 * @author Ryan Rule-Hoffman <ryan.rulehoffman@icloud.com>
 * @param <T> The type of definition to manage.
 */
public abstract class DefinitionManager<T extends Definition>
{
    
    /**
     * The map of definitions.
     */
    private final ConcurrentHashMap<Integer, T> definitions;
    
    /**
     * Have definitions been loaded.
     */
    private boolean loaded;
    
    /**
     * Creates a new DefinitionManager.
     */
    public DefinitionManager()
    {
        definitions = new ConcurrentHashMap<Integer, T>();
    }
    
    /**
     * Get the definition for the specified ID.
     * @param id The ID.
     * @return The definition.
     */
    public T get(int id)
    {
        return definitions.get(id);
    }
    
    /**
     * Add a definition.
     * @param id The ID.
     * @param definition The definition. 
     */
    protected void put(int id, T definition)
    {
        definitions.put(id, definition);
    }
    
    /**
     * Load the definitions.
     * @param wrapper The connection wrapper to the database.
     */
    public void load(MySQLConnectionWrapper wrapper)
    {
        if (loaded)
        {
            throw new RuntimeException("Definitions already loaded.");
        }
        
        loadDefinitions(wrapper);
        
        loaded = true;
    }
    
    /**
     * Load the definitions.
     * @param wrapper The connection wrapper to the database.
     */
    protected abstract void loadDefinitions(MySQLConnectionWrapper wrapper);
}
