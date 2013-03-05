package de.lmu.ifi.dbs.elki.index.tree.metrical.mtreevariants.mktrees.mkcop;

/*
 This file is part of ELKI:
 Environment for Developing KDD-Applications Supported by Index-Structures

 Copyright (C) 2013
 Ludwig-Maximilians-Universität München
 Lehr- und Forschungseinheit für Datenbanksysteme
 ELKI Development Team

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import de.lmu.ifi.dbs.elki.database.relation.Relation;
import de.lmu.ifi.dbs.elki.distance.distancefunction.DistanceFunction;
import de.lmu.ifi.dbs.elki.distance.distancevalue.NumberDistance;
import de.lmu.ifi.dbs.elki.index.tree.metrical.mtreevariants.AbstractMTreeFactory;
import de.lmu.ifi.dbs.elki.index.tree.metrical.mtreevariants.strategies.insert.MTreeInsert;
import de.lmu.ifi.dbs.elki.index.tree.metrical.mtreevariants.strategies.split.MTreeSplit;
import de.lmu.ifi.dbs.elki.persistent.PageFile;
import de.lmu.ifi.dbs.elki.persistent.PageFileFactory;
import de.lmu.ifi.dbs.elki.utilities.ClassGenericsUtil;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.OptionID;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.constraints.GreaterConstraint;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.parameterization.Parameterization;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.parameters.IntParameter;

/**
 * Factory for a MkCoPTree-Tree
 * 
 * @author Erich Schubert
 * 
 * @apiviz.stereotype factory
 * @apiviz.uses MkCoPTreeIndex oneway - - «create»
 * 
 * @param <O> Object type
 * @param <D> Distance type
 */
public class MkCopTreeFactory<O, D extends NumberDistance<D, ?>> extends AbstractMTreeFactory<O, D, MkCoPTreeNode<O, D>, MkCoPEntry<D>, MkCoPTreeIndex<O, D>> {
  /**
   * Parameter for k
   */
  public static final OptionID K_ID = new OptionID("mkcop.k", "positive integer specifying the maximum number k of reverse k nearest neighbors to be supported.");

  /**
   * Parameter k.
   */
  int k_max;

  /**
   * Constructor.
   * 
   * @param pageFileFactory Data storage
   * @param distanceFunction Distance function
   * @param splitStrategy Split strategy
   * @param insertStrategy Insert strategy
   * @param k_max Maximum k supported
   */
  public MkCopTreeFactory(PageFileFactory<?> pageFileFactory, DistanceFunction<O, D> distanceFunction, MTreeSplit<O, D, MkCoPTreeNode<O, D>, MkCoPEntry<D>> splitStrategy, MTreeInsert<O, D, MkCoPTreeNode<O, D>, MkCoPEntry<D>> insertStrategy, int k_max) {
    super(pageFileFactory, distanceFunction, splitStrategy, insertStrategy);
    this.k_max = k_max;
  }

  @Override
  public MkCoPTreeIndex<O, D> instantiate(Relation<O> relation) {
    PageFile<MkCoPTreeNode<O, D>> pagefile = makePageFile(getNodeClass());
    return new MkCoPTreeIndex<>(relation, pagefile, distanceFunction.instantiate(relation), splitStrategy, insertStrategy, k_max);
  }

  protected Class<MkCoPTreeNode<O, D>> getNodeClass() {
    return ClassGenericsUtil.uglyCastIntoSubclass(MkCoPTreeNode.class);
  }

  /**
   * Parameterization class.
   * 
   * @author Erich Schubert
   * 
   * @apiviz.exclude
   */
  public static class Parameterizer<O, D extends NumberDistance<D, ?>> extends AbstractMTreeFactory.Parameterizer<O, D, MkCoPTreeNode<O, D>, MkCoPEntry<D>> {
    protected int k_max = 0;

    @Override
    protected void makeOptions(Parameterization config) {
      super.makeOptions(config);
      IntParameter k_maxP = new IntParameter(K_ID);
      k_maxP.addConstraint(new GreaterConstraint(0));
      if (config.grab(k_maxP)) {
        k_max = k_maxP.intValue();
      }
    }

    @Override
    protected MkCopTreeFactory<O, D> makeInstance() {
      return new MkCopTreeFactory<>(pageFileFactory, distanceFunction, splitStrategy, insertStrategy, k_max);
    }
  }
}
