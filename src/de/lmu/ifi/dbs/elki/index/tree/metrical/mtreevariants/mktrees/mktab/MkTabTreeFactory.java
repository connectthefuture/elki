package de.lmu.ifi.dbs.elki.index.tree.metrical.mtreevariants.mktrees.mktab;

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
import de.lmu.ifi.dbs.elki.distance.distancevalue.Distance;
import de.lmu.ifi.dbs.elki.index.tree.metrical.mtreevariants.mktrees.AbstractMkTreeUnifiedFactory;
import de.lmu.ifi.dbs.elki.index.tree.metrical.mtreevariants.strategies.insert.MTreeInsert;
import de.lmu.ifi.dbs.elki.index.tree.metrical.mtreevariants.strategies.split.MTreeSplit;
import de.lmu.ifi.dbs.elki.persistent.PageFile;
import de.lmu.ifi.dbs.elki.persistent.PageFileFactory;
import de.lmu.ifi.dbs.elki.utilities.ClassGenericsUtil;

/**
 * Factory for MkTabTrees
 * 
 * @author Erich Schubert
 * 
 * @apiviz.stereotype factory
 * @apiviz.uses MkTabTreeIndex oneway - - «create»
 * 
 * @param <O> Object type
 * @param <D> Distance type
 */
public class MkTabTreeFactory<O, D extends Distance<D>> extends AbstractMkTreeUnifiedFactory<O, D, MkTabTreeNode<O, D>, MkTabEntry<D>, MkTabTreeIndex<O, D>> {
  /**
   * Constructor.
   * 
   * @param pageFileFactory Data storage
   * @param distanceFunction Distance function
   * @param splitStrategy Split strategy
   * @param k_max Maximum k
   */
  public MkTabTreeFactory(PageFileFactory<?> pageFileFactory, DistanceFunction<O, D> distanceFunction, MTreeSplit<O, D, MkTabTreeNode<O, D>, MkTabEntry<D>> splitStrategy, MTreeInsert<O, D, MkTabTreeNode<O, D>, MkTabEntry<D>> insertStrategy, int k_max) {
    super(pageFileFactory, distanceFunction, splitStrategy, insertStrategy, k_max);
  }

  @Override
  public MkTabTreeIndex<O, D> instantiate(Relation<O> relation) {
    PageFile<MkTabTreeNode<O, D>> pagefile = makePageFile(getNodeClass());
    return new MkTabTreeIndex<>(relation, pagefile, distanceFunction.instantiate(relation), splitStrategy, insertStrategy, k_max);
  }

  protected Class<MkTabTreeNode<O, D>> getNodeClass() {
    return ClassGenericsUtil.uglyCastIntoSubclass(MkTabTreeNode.class);
  }

  /**
   * Parameterization class.
   * 
   * @author Erich Schubert
   * 
   * @apiviz.exclude
   */
  public static class Parameterizer<O, D extends Distance<D>> extends AbstractMkTreeUnifiedFactory.Parameterizer<O, D, MkTabTreeNode<O, D>, MkTabEntry<D>> {
    @Override
    protected MkTabTreeFactory<O, D> makeInstance() {
      return new MkTabTreeFactory<>(pageFileFactory, distanceFunction, splitStrategy, insertStrategy, k_max);
    }
  }
}
