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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.allgofree.mysql.MySQLConnectionWrapper;
import net.allgofree.mysql.MySQLConnectData;
import net.allgofree.mysql.MySQLConnection;
import net.allgofree.mysql.MySQLConnectionFactory;
import net.allgofree.updater.Updatable;
import net.allgofree.updater.Updater;

/**
 * Manages the storage of objects.
 * @author Ryan Rule-Hoffman <ryan.rulehoffman@icloud.com>
 */
public abstract class AbstractMasterStorageManager implements Runnable, Updatable
{
    
    /**
     * The default tick rate for the master storage manager.
     */
    private static final long DEFAULT_TICK_RATE = 200;
    
    /**
     * The connection factory.
     */
    private final MySQLConnectionFactory factory;
    
    /**
     * The updater.
     */
    private final Updater updater;
    
    /**
     * The queue of requests.
     */
    private final ConcurrentLinkedQueue<Request> requestQueue;
    
    /**
     * The list of all of the storage managers.
     */
    private final List<StorageManager> storageManagers;
    
    /**
     * The map of request types to storage managers.
     */
    private final Map<Class, StorageManager> storageMap;
    
    /**
     * The connection wrapper.
     */
    private MySQLConnectionWrapper wrapper;
    
    /**
     * Creates a new DatabaseManager.
     */
    public AbstractMasterStorageManager()
    {
        updater = new Updater(DEFAULT_TICK_RATE);
        updater.addUpdate(this);
        factory = new MySQLConnectionFactory();
        requestQueue = new ConcurrentLinkedQueue();
        storageManagers = new ArrayList();
        storageMap = new HashMap();
    }
    
    /**
     * Get the MySQL connection factory.
     * @return The MySQL connection factory.
     */
    protected MySQLConnectionFactory getFactory()
    {
        return factory;
    }
    
    /**
     * Add a storage manager.
     * @param manager The storage manager to add.
     */
    protected void addStorageManager(StorageManager manager)
    {
        // Add the manager to the list
        storageManagers.add(manager);
        
        // Register all of it's data types
        for (Class next : manager.getDatatypes())
        {
            storageMap.put(next, manager);
        }
    }
    
    /**
     * Get the credentials.
     * @return The credentials.
     */
    public MySQLConnectData getCredentials()
    {
        return factory.getConnectionData();
    }
    
    /**
     * Add a request.
     * @param request The request.
     */
    public void addRequest(Request request)
    {
        requestQueue.add(request);
    }
    
    /**
     * Connect to the database.
     * @throws SQLException If an exception occurs.
     */
    private void connect() throws SQLException
    {
        if (wrapper != null)
        {
            // TODO: Disconnect
        }
        MySQLConnection connection = factory.newConnection();
        wrapper = new MySQLConnectionWrapper(connection.getConnection());
    }

    /**
     * Run the database thread.
     */
    public void run()
    {
        try
        {
            connect();
            debug("Connected to database.");
        } catch (SQLException ex)
        {
            Logger.getLogger(AbstractMasterStorageManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        while (true)
        {
            try
            {
                updater.doUpdating();
            } catch (InterruptedException ex)
            {
                Logger.getLogger(AbstractMasterStorageManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Start the database thread.
     */
    public void start()
    {
        new Thread(this).start();
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    public long[] updateTimes()
    {
        return new long[] {DEFAULT_TICK_RATE};
    }

    /**
     * {@inheritDoc}
     * @param updateIndex {@inheritDoc}
     */
    public void updateAction(int updateIndex)
    {
        while (!requestQueue.isEmpty())
        {
            Request next = requestQueue.poll();

            // Grab the class of the request
            Class<? extends Request> clazz = next.getClass();

            // Grab the manager associated with that class
            final StorageManager manager = storageMap.get(clazz);

            // Make sure the manager isn't null
            if (manager == null)
            {
                throw new RuntimeException("No storage manager registered for request type " + clazz.getCanonicalName());
            }

            // Handle the request
            manager.handleRequest(next, this);
        }
    }
    
    /**
     * Get the MySQL connection wrapper.
     * @return The wrapper.
     */
    public MySQLConnectionWrapper getWrapper()
    {
        return wrapper;
    }
    
    /**
     * Print debug text.
     * @param text The text.
     */
    public void debug(String text)
    {
        //Logger.getLogger(DatabaseManager.class.getName()).log(Level.INFO, text, "");
        System.out.println("[DatabaseManager] " + text);
    }
}
