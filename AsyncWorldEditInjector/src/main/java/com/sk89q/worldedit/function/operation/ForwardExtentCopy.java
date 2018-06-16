/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.sk89q.worldedit.function.operation;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.entity.metadata.EntityType;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.CombinedRegionFunction;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.function.RegionMaskingFilter;
import com.sk89q.worldedit.function.block.ExtentBlockCopy;
import com.sk89q.worldedit.function.entity.ExtentEntityCopy;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.Masks;
import com.sk89q.worldedit.function.visitor.EntityVisitor;
import com.sk89q.worldedit.function.visitor.RegionVisitor;
import com.sk89q.worldedit.math.transform.Identity;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.regions.Region;

import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import org.primesoft.asyncworldedit.injector.core.InjectorCore;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Makes a copy of a portion of one extent to another extent or another point.
 *
 * <p>
 * This is a forward extent copy, meaning that it iterates over the blocks in
 * the source extent, and will copy as many blocks as there are in the source.
 * Therefore, interpolation will not occur to fill in the gaps.</p>
 */
public class ForwardExtentCopy implements Operation {

    private final Extent source;
    private final Extent destination;
    private final Region region;
    private final Vector from;
    private final Vector to;
    private int repetitions = 1;
    private Mask sourceMask = Masks.alwaysTrue();
    private boolean removingEntities;
    private boolean copyingEntities = true; // default to true for backwards compatibility, sort of
    private RegionFunction sourceFunction = null;
    private Transform transform = new Identity();
    private Transform currentTransform = null;
    private RegionVisitor lastVisitor;
    private int affected;

    private boolean m_copyBiome = false;

    /**
     * Create a new copy using the region's lowest minimum point as the "from"
     * position.
     *
     * @param source the source extent
     * @param region the region to copy
     * @param destination the destination extent
     * @param to the destination position
     * @see #ForwardExtentCopy(Extent, Region, Vector, Extent, Vector) the main
     * constructor
     */
    public ForwardExtentCopy(Extent source, Region region, Extent destination, Vector to) {
        this(source, region, region.getMinimumPoint(), destination, to);
    }

    /**
     * Create a new copy.
     *
     * @param source the source extent
     * @param region the region to copy
     * @param from the source position
     * @param destination the destination extent
     * @param to the destination position
     */
    public ForwardExtentCopy(Extent source, Region region, Vector from, Extent destination, Vector to) {
        checkNotNull(source);
        checkNotNull(region);
        checkNotNull(from);
        checkNotNull(destination);
        checkNotNull(to);
        this.source = source;
        this.destination = destination;
        this.region = region;
        this.from = from;
        this.to = to;
    }

    /**
     * Get the transformation that will occur on every point.
     *
     * <p>
     * The transformation will stack with each repetition.</p>
     *
     * @return a transformation
     */
    public Transform getTransform() {
        return transform;
    }

    /**
     * Set the transformation that will occur on every point.
     *
     * @param transform a transformation
     * @see #getTransform()
     */
    public void setTransform(Transform transform) {
        checkNotNull(transform);
        this.transform = transform;
    }

    /**
     * Get the mask that gets applied to the source extent.
     *
     * <p>
     * This mask can be used to filter what will be copied from the source.</p>
     *
     * @return a source mask
     */
    public Mask getSourceMask() {
        return sourceMask;
    }

    /**
     * Set a mask that gets applied to the source extent.
     *
     * @param sourceMask a source mask
     * @see #getSourceMask()
     */
    public void setSourceMask(Mask sourceMask) {
        checkNotNull(sourceMask);
        this.sourceMask = sourceMask;
    }

    /**
     * Get the function that gets applied to all source blocks <em>after</em>
     * the copy has been made.
     *
     * @return a source function, or null if none is to be applied
     */
    public RegionFunction getSourceFunction() {
        return sourceFunction;
    }

    /**
     * Set the function that gets applied to all source blocks <em>after</em>
     * the copy has been made.
     *
     * @param function a source function, or null if none is to be applied
     */
    public void setSourceFunction(RegionFunction function) {
        this.sourceFunction = function;
    }

    /**
     * Get the number of repetitions left.
     *
     * @return the number of repetitions
     */
    public int getRepetitions() {
        return repetitions;
    }

    /**
     * Set the number of repetitions left.
     *
     * @param repetitions the number of repetitions
     */
    public void setRepetitions(int repetitions) {
        checkArgument(repetitions >= 0, "number of repetitions must be non-negative");
        this.repetitions = repetitions;
    }

    /**
     * Return whether entities should be copied along with blocks.
     *
     * @return true if copying
     */
    public boolean isCopyingEntities() {
        return copyingEntities;
    }

    /**
     * Set whether entities should be copied along with blocks.
     *
     * @param copyingEntities true if copying
     */
    public void setCopyingEntities(boolean copyingEntities) {
        this.copyingEntities = copyingEntities;
    }

    /**
     * Return whether entities that are copied should be removed.
     *
     * @return true if removing
     */
    public boolean isRemovingEntities() {
        return removingEntities;
    }

    /**
     * Set whether entities that are copied should be removed.
     *
     * @param removingEntities true if removing
     */
    public void setRemovingEntities(boolean removingEntities) {
        this.removingEntities = removingEntities;
    }

    /**
     * Set biome copy on/off
     *
     * @param status
     */
    public void setBiomeCopy(boolean status) {
        m_copyBiome = status;
    }

    /**
     * Is the biome copy on or off
     *
     * @return
     */
    public boolean isBiomeCopy() {
        return m_copyBiome;
    }

    /**
     * Get the number of affected objects.
     *
     * @return the number of affected
     */
    public int getAffected() {
        return affected;
    }

    @Override
    public Operation resume(RunContext run) throws WorldEditException {
        if (lastVisitor != null) {
            affected += lastVisitor.getAffected();
            lastVisitor = null;
        }

        if (repetitions > 0) {
            repetitions--;

            if (currentTransform == null) {
                currentTransform = transform;
            }

            RegionFunction blockCopy = new ExtentBlockCopy(source, from, destination, to, currentTransform);
            if (m_copyBiome) {
                blockCopy = InjectorCore.getInstance().getClassFactory().
                        addBiomeCopy(blockCopy, source, from, destination, to, currentTransform, true);
            }

            RegionMaskingFilter filter = new RegionMaskingFilter(sourceMask, blockCopy);
            RegionFunction function = sourceFunction != null ? new CombinedRegionFunction(filter, sourceFunction) : filter;
            RegionVisitor blockVisitor = new RegionVisitor(region, function);

            lastVisitor = blockVisitor;
            currentTransform = currentTransform.combine(transform);

            if (copyingEntities) {
                ExtentEntityCopy entityCopy = new ExtentEntityCopy(from, destination, to, currentTransform);
                entityCopy.setRemoving(removingEntities);
                List<? extends Entity> entities = source.getEntities(region);

                we619(entities);
                EntityVisitor entityVisitor = new EntityVisitor(entities.iterator(), entityCopy);
                return new DelegateOperation(this, new OperationQueue(blockVisitor, entityVisitor));
            } else {
                return new DelegateOperation(this, blockVisitor);
            }
        } else {
            return null;
        }
    }

    private Consumer<List<? extends Entity>> m_we619 = null;

    private void executeWe619NoOp(List<? extends Entity> entities) {

    }

    private void executeWe619(List<? extends Entity> entities) {
        // Switch to entities.removeIf after Java 8 cutoff.
        Iterator<? extends Entity> entityIterator = entities.iterator();
        while (entityIterator.hasNext()) {
            EntityType type = entityIterator.next().getFacet(EntityType.class);

            if (type != null && !type.isPasteable()) {
                entityIterator.remove();
            }
        }
    }

    private void we619(List<? extends Entity> entities) {
        if (m_we619 == null) {
            boolean is619 = Stream.of(EntityType.class.getDeclaredMethods()).anyMatch(
                    i -> i.isAccessible() && "isPasteable".equals(i.getName())
                    && i.getParameterCount() == 0 && boolean.class.isAssignableFrom(i.getReturnType())
            );

            m_we619 = is619 ? this::executeWe619 : this::executeWe619NoOp;
        }

        m_we619.accept(entities);
    }

    @Override
    public void cancel() {
    }

    @Override
    public void addStatusMessages(List<String> messages) {
    }

    public static Class<?> forceClassLoad() {
        return ForwardExtentCopy.class;
    }

}
