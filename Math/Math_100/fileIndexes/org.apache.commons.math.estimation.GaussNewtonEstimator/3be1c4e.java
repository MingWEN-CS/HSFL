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

package org.apache.commons.math.estimation;

import java.io.Serializable;

import org.apache.commons.math.linear.InvalidMatrixException;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

/** 
 * This class implements a solver for estimation problems.
 *
 * <p>This class solves estimation problems using a weighted least
 * squares criterion on the measurement residuals. It uses a
 * Gauss-Newton algorithm.</p>
 *
 * @version $Id: GaussNewtonEstimator.java 1705 2006-09-17 19:57:39Z luc $
 *
 */

public class GaussNewtonEstimator
  implements Estimator, Serializable {

  /** 
   * Simple constructor.
   *
   * <p>This constructor builds an estimator and stores its convergence
   * characteristics.</p>
   *
   * <p>An estimator is considered to have converged whenever either
   * the criterion goes below a physical threshold under which
   * improvements are considered useless or when the algorithm is
   * unable to improve it (even if it is still high). The first
   * condition that is met stops the iterations.</p>
   *
   * <p>The fact an estimator has converged does not mean that the
   * model accurately fits the measurements. It only means no better
   * solution can be found, it does not mean this one is good. Such an
   * analysis is left to the caller.</p>
   *
   * <p>If neither conditions are fulfilled before a given number of
   * iterations, the algorithm is considered to have failed and an
   * {@link EstimationException} is thrown.</p>
   *
   * @param maxIterations maximum number of iterations allowed
   * @param convergence criterion threshold below which we do not need
   * to improve the criterion anymore
   * @param steadyStateThreshold steady state detection threshold, the
   * problem has converged has reached a steady state if
   * <code>Math.abs (Jn - Jn-1) < Jn * convergence</code>, where
   * <code>Jn</code> and <code>Jn-1</code> are the current and
   * preceding criterion value (square sum of the weighted residuals
   * of considered measurements).
   */
  public GaussNewtonEstimator(int maxIterations,
                               double convergence,
                               double steadyStateThreshold) {
    this.maxIterations        = maxIterations;
    this.steadyStateThreshold = steadyStateThreshold;
    this.convergence          = convergence;
  }

  /** 
   * Solve an estimation problem using a least squares criterion.
   *
   * <p>This method set the unbound parameters of the given problem
   * starting from their current values through several iterations. At
   * each step, the unbound parameters are changed in order to
   * minimize a weighted least square criterion based on the
   * measurements of the problem.</p>
   *
   * <p>The iterations are stopped either when the criterion goes
   * below a physical threshold under which improvement are considered
   * useless or when the algorithm is unable to improve it (even if it
   * is still high). The first condition that is met stops the
   * iterations. If the convergence it nos reached before the maximum
   * number of iterations, an {@link EstimationException} is
   * thrown.</p>
   *
   * @param problem estimation problem to solve
   * @exception EstimationException if the problem cannot be solved
   *
   * @see EstimationProblem
   *
   */
  public void estimate(EstimationProblem problem)
    throws EstimationException {
    int    iterations = 0;
    double previous   = 0.0;
    double current    = 0.0;

    // iterate until convergence is reached
    do {

      if (++iterations > maxIterations) {
        throw new EstimationException ("unable to converge in {0} iterations",
                                       new String[] {
                                         Integer.toString(maxIterations)
                                       });
      }

      // perform one iteration
      linearEstimate(problem);

      previous = current;
      current  = evaluateCriterion(problem);

    } while ((iterations < 2)
             || (Math.abs(previous - current) > (current * steadyStateThreshold)
                 && (Math.abs(current) > convergence)));

  }

  /** 
   * Estimate the solution of a linear least square problem.
   *
   * <p>The Gauss-Newton algorithm is iterative. Each iteration
   * consists in solving a linearized least square problem. Several
   * iterations are needed for general problems since the
   * linearization is only an approximation of the problem
   * behaviour. However, for linear problems one iteration is enough
   * to get the solution. This method is provided in the public
   * interface in order to handle more efficiently these linear
   * problems.</p>
   *
   * @param problem estimation problem to solve
   * @exception EstimationException if the problem cannot be solved
   *
   */
  public void linearEstimate(EstimationProblem problem)
    throws EstimationException {

    EstimatedParameter[]  parameters   = problem.getUnboundParameters();
    WeightedMeasurement[] measurements = problem.getMeasurements();

    // build the linear problem
    RealMatrix b              = new RealMatrixImpl(parameters.length, 1);
    RealMatrix a              = new RealMatrixImpl(parameters.length, parameters.length);
    double[] grad             = new double[parameters.length];
    RealMatrixImpl bDecrement = new RealMatrixImpl(parameters.length, 1);
    double[][] bDecrementData = bDecrement.getDataRef();
    RealMatrixImpl wGradGradT = new RealMatrixImpl(parameters.length, parameters.length);
    double[][] wggData        = wGradGradT.getDataRef();
    for (int i = 0; i < measurements.length; ++i) {
        if (! measurements [i].isIgnored()) {

            double weight   = measurements[i].getWeight();
            double residual = measurements[i].getResidual();

            // compute the normal equation
            for (int j = 0; j < parameters.length; ++j) {
                grad[j] = measurements[i].getPartial(parameters[j]);
                bDecrementData[j][0] = weight * residual * grad[j];
            }

            // build the contribution matrix for measurement i
            for (int k = 0; k < parameters.length; ++k) {
                double[] wggRow = wggData[k];
                double gk = grad[k];
                for (int l = 0; l < parameters.length; ++l) {
                    wggRow[l] =  weight * gk * grad[l];
                }
            }

            // update the matrices
            a = a.add(wGradGradT);
            b = b.add(bDecrement);

        }
    }

    try {

      // solve the linearized least squares problem
      RealMatrix dX = a.solve(b);

      // update the estimated parameters
      for (int i = 0; i < parameters.length; ++i) {
        parameters[i].setEstimate(parameters[i].getEstimate() + dX.getEntry(i, 0));
      }

    } catch(InvalidMatrixException e) {
      throw new EstimationException(e);
    }

  }

  private double evaluateCriterion(EstimationProblem problem) {
    double criterion = 0.0;
    WeightedMeasurement[] measurements = problem.getMeasurements();

    for (int i = 0; i < measurements.length; ++i) {
      double residual = measurements[i].getResidual();
      criterion      += measurements[i].getWeight() * residual * residual;
    }

    return criterion;

  }

  /** 
   * Get the Root Mean Square value.
   * Get the Root Mean Square value, i.e. the root of the arithmetic
   * mean of the square of all weighted residuals. This is related to the
   * criterion that is minimized by the estimator as follows: if
   * <em>c</em> if the criterion, and <em>n</em> is the number of
   * measurements, then the RMS is <em>sqrt (c/n)</em>.
   * @param problem estimation problem
   * @return RMS value
   */
  public double getRMS(EstimationProblem problem) {
    double criterion = evaluateCriterion(problem);
    int n = problem.getMeasurements().length;
    return Math.sqrt(criterion / n);
  }

  private int    maxIterations;
  private double steadyStateThreshold;
  private double convergence;

  private static final long serialVersionUID = -7606628156644194170L;

}
