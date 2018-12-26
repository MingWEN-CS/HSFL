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

package org.apache.commons.math.ode;

import java.util.Collection;

import org.apache.commons.math.ode.events.CombinedEventsManager;
import org.apache.commons.math.ode.events.EventHandler;
import org.apache.commons.math.ode.sampling.DummyStepHandler;
import org.apache.commons.math.ode.sampling.StepHandler;

/**
 * Base class managing common boilerplate for all integrators.
 * @version $Revision$ $Date$
 * @since 2.0
 */
public abstract class AbstractIntegrator implements FirstOrderIntegrator {

    /** Name of the method. */
    private final String name;

    /** Step handler. */
    protected StepHandler handler;

    /** Current step start time. */
    protected double stepStart;

    /** Current stepsize. */
    protected double stepSize;

    /** Events handlers manager. */
    protected CombinedEventsManager eventsHandlersManager;

    /** Build an instance.
     * @param name name of the method
     */
    public AbstractIntegrator(final String name) {
        this.name = name;
        handler = DummyStepHandler.getInstance();
        stepStart = Double.NaN;
        stepSize  = Double.NaN;
        eventsHandlersManager = new CombinedEventsManager();
    }

    /** {@inheritDoc} */
    public String getName() {
        return name;
    }

    /** {@inheritDoc} */
    public void setStepHandler(final StepHandler handler) {
        this.handler = handler;
    }

    /** {@inheritDoc} */
    public StepHandler getStepHandler() {
        return handler;
    }

    /** {@inheritDoc} */
    public void addEventHandler(final EventHandler function,
                                final double maxCheckInterval,
                                final double convergence,
                                final int maxIterationCount) {
        eventsHandlersManager.addEventHandler(function, maxCheckInterval,
                                              convergence, maxIterationCount);
    }

    /** {@inheritDoc} */
    public Collection<EventHandler> getEventsHandlers() {
        return eventsHandlersManager.getEventsHandlers();
    }

    /** {@inheritDoc} */
    public void clearEventsHandlers() {
        eventsHandlersManager.clearEventsHandlers();
    }

    /** {@inheritDoc} */
    public double getCurrentStepStart() {
        return stepStart;
    }

    /** {@inheritDoc} */
    public double getCurrentSignedStepsize() {
        return stepSize;
    }

    /** Perform some sanity checks on the integration parameters.
     * @param equations differential equations set
     * @param t0 start time
     * @param y0 state vector at t0
     * @param t target time for the integration
     * @param y placeholder where to put the state vector
     * @exception IntegratorException if some inconsistency is detected
     */
    protected void sanityChecks(final FirstOrderDifferentialEquations equations,
                                final double t0, final double[] y0,
                                final double t, final double[] y)
        throws IntegratorException {

        if (equations.getDimension() != y0.length) {
            throw new IntegratorException("dimensions mismatch: ODE problem has dimension {0}," +
                                          " initial state vector has dimension {1}",
                                          new Object[] {
                                              Integer.valueOf(equations.getDimension()),
                                              Integer.valueOf(y0.length)
                                          });
        }

        if (equations.getDimension() != y.length) {
            throw new IntegratorException("dimensions mismatch: ODE problem has dimension {0}," +
                                          " final state vector has dimension {1}",
                                          new Object[] {
                                              Integer.valueOf(equations.getDimension()),
                                              Integer.valueOf(y.length)
                                          });
        }

        if (Math.abs(t - t0) <= 1.0e-12 * Math.max(Math.abs(t0), Math.abs(t))) {
            throw new IntegratorException("too small integration interval: length = {0}",
                                          new Object[] {
                                              Double.valueOf(Math.abs(t - t0))
                                          });
        }

    }

}
