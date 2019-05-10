/*
 * AsyncWorldEdit a performance improvement plugin for Minecraft WorldEdit plugin.
 * Copyright (c) 2015, SBPrime <https://github.com/SBPrime/>
 * Copyright (c) AsyncWorldEdit contributors
 *
 * All rights reserved.
 *
 * Redistribution in source, use in source and binary forms, with or without
 * modification, are permitted free of charge provided that the following 
 * conditions are met:
 *
 * 1.  Redistributions of source code must retain the above copyright notice, this
 *     list of conditions and the following disclaimer.
 * 2.  Redistributions of source code, with or without modification, in any form
 *     other then free of charge is not allowed,
 * 3.  Redistributions of source code, with tools and/or scripts used to build the 
 *     software is not allowed,
 * 4.  Redistributions of source code, with information on how to compile the software
 *     is not allowed,
 * 5.  Providing information of any sort (excluding information from the software page)
 *     on how to compile the software is not allowed,
 * 6.  You are allowed to build the software for your personal use,
 * 7.  You are allowed to build the software using a non public build server,
 * 8.  Redistributions in binary form in not allowed.
 * 9.  The original author is allowed to redistrubute the software in bnary form.
 * 10. Any derived work based on or containing parts of this software must reproduce
 *     the above copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials provided with the
 *     derived work.
 * 11. The original author of the software is allowed to change the license
 *     terms or the entire license of the software as he sees fit.
 * 12. The original author of the software is allowed to sublicense the software
 *     or its parts using any license terms he sees fit.
 * 13. By contributing to this project you agree that your contribution falls under this
 *     license.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.primesoft.asyncworldedit.directChunk;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.biome.BiomeType;
import com.sk89q.worldedit.world.biome.BiomeTypes;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import java.util.LinkedList;
import java.util.List;
import org.primesoft.asyncworldedit.api.directChunk.IBaseChunkData;

/**
 *
 * @author SBPrime
 */
public class ChangesetChunkExtent implements Extent {

    private final IBaseChunkData m_data;
    private final BlockVector3 m_minPoint;
    private final BlockVector3 m_maxPoint;

    public ChangesetChunkExtent(IBaseChunkData data) {
        if (data == null) {
            throw new IllegalArgumentException("data is null");
        }

        m_data = data;

        BlockVector2 zero = m_data.getChunkCoords();
        m_minPoint = BlockVector3.at(zero.getBlockX() << 4, 0, zero.getBlockZ() << 4);
        m_maxPoint = BlockVector3.at(15 + zero.getBlockX() << 4, 255, 15 + zero.getBlockZ() << 4);
    }

    @Override
    public BlockVector3 getMinimumPoint() {
        return m_minPoint;
    }

    @Override
    public BlockVector3 getMaximumPoint() {
        return m_maxPoint;
    }

    @Override
    public List<? extends Entity> getEntities(Region region) {
        //TODO: Implement entity get
        return new LinkedList<>();
    }

    @Override
    public List<? extends Entity> getEntities() {
        //TODO: Implement entity get
        return new LinkedList<>();
    }

    @Override
    public Entity createEntity(Location location, BaseEntity entity) {
        //TODO: Implement entity create
        return null;
    }

    @Override
    public BlockState getBlock(BlockVector3 position) {
        BlockVector3 p = position.subtract(m_minPoint);
        return m_data.getBlock(p.getBlockX(), p.getBlockY(), p.getBlockZ()).toImmutableState();
    }    

    @Override
    public BaseBlock getFullBlock(BlockVector3 position) {
        BlockVector3 p = position.subtract(m_minPoint);
        return m_data.getBlock(p.getBlockX(), p.getBlockY(), p.getBlockZ()).toBaseBlock();
    }

    @Override
    public BiomeType getBiome(BlockVector2 position) {
        //TODO: Implement
        return BiomeTypes.PLAINS;
    }

    @Override
    public boolean setBlock(BlockVector3 position, BlockStateHolder block) throws WorldEditException {
        BlockVector3 p = position.subtract(m_minPoint);
        m_data.setBlock(p.getBlockX(), p.getBlockY(), p.getBlockZ(), block);
        return true;
    }

    @Override
    public boolean setBiome(BlockVector2 position, BiomeType biome) {
        //TODO: Implement
        return false;
    }

    @Override
    public Operation commit() {
        //TODO: Implement
        return null;
    }

}
