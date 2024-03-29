/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.lang3.event;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;

/**
 * An EventListenerSupport object can be used to manage a list of event listeners of a particular type.
 * <p/>
 * To use this class, suppose you want to support ActionEvents.  You would do:
 * <pre>
 * public class MyActionEventSource
 * {
 *   private EventListenerSupport<ActionListener> actionListeners = EventListenerSupport.create(ActionListener.class);
 * <p/>
 *   public void someMethodThatFiresAction()
 *   {
 *     ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "somethingCool");
 *     actionListeners.fire().actionPerformed(e);
 *   }
 * }
 * </pre>
 *
 * @param <L> The event listener type
 */
public class EventListenerSupport<L>
{
    private final List<L> listeners = new LinkedList<L>();
    private final L proxy;

    /**
     * Creates an EventListenerSupport object which supports the specified listener type.
     *
     * @param listenerType the listener type
     * @return an EventListenerSupport object which supports the specified listener type
     */
    public static <T> EventListenerSupport<T> create(Class<T> listenerType)
    {
        return new EventListenerSupport<T>(listenerType);
    }

    /**
     * Creates an EventListenerSupport object which supports the provided listener interface.
     *
     * @param listenerInterface the listener interface
     */
    public EventListenerSupport(Class<L> listenerInterface)
    {
        this(listenerInterface, Thread.currentThread().getContextClassLoader());
    }

    /**
     * Creates an EventListenerSupport object which supports the provided listener interface using the specified
     * class loader to create the JDK dynamic proxy.
     *
     * @param listenerInterface the listener interface
     * @param classLoader       the class loader
     */
    public EventListenerSupport(Class<L> listenerInterface, ClassLoader classLoader)
    {
        proxy = listenerInterface.cast(Proxy.newProxyInstance(classLoader, new Class[]{listenerInterface},
                new ProxyInvocationHandler()));
    }

    /**
     * Returns a proxy object which can be used to call listener methods on all of the registered event listeners.
     *
     * @return a proxy object which can be used to call listener methods on all of the registered event listeners
     */
    public L fire()
    {
        return proxy;
    }

//**********************************************************************************************************************
// Other Methods
//**********************************************************************************************************************

    /**
     * Registers an event listener.
     *
     * @param listener the event listener
     */
    public void addListener(L listener)
    {
        listeners.add(0, listener);
    }

    /**
     * Returns the number of registered listeners.
     *
     * @return the number of registered listeners
     */
    public int getListenerCount()
    {
        return listeners.size();
    }

    /**
     * Unregisters an event listener.
     *
     * @param listener the event listener
     */
    public void removeListener(L listener)
    {
        listeners.remove(listener);
    }

    /**
     * An invocation handler used to dispatch the event(s) to all the listeners.
     */
    private class ProxyInvocationHandler implements InvocationHandler
    {
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
        {
            for (int i = listeners.size() - 1; i >= 0; --i)
            {
                method.invoke(listeners.get(i), args);
            }
            return null;
        }
    }
}
