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
package com.allgofree.sapphire.storage;

/**
 * A storage manager for a datatype.
 * @author Ryan Rule-Hoffman <ryan.rulehoffman@icloud.com>
 */
public interface StorageManager
{
    
    /**
     * The result of a request.
     */
    public enum RequestResult
    {
        SUCCESSFUL, 
        FAILED
    };
    
    /**
     * Get the data types that the storage manager is responsible for.
     * @return The data types.
     */
    public Class[] getDatatypes();
    
    /**
     * Handle a storage request.
     * @param request The request.
     * @param manager The master manager.
     * @return The request response.
     */
    public RequestResult handleRequest(Request request, AbstractMasterStorageManager manager);
}
