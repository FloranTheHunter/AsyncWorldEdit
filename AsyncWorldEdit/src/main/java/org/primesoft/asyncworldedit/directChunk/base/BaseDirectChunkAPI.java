/*
 * AsyncWorldEdit a performance improvement plugin for Minecraft WorldEdit plugin.
 * Copyright (c) 2016, SBPrime <https://github.com/SBPrime/>
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
package org.primesoft.asyncworldedit.directChunk.base;

import com.sk89q.worldedit.world.block.BlockStateHolder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.primesoft.asyncworldedit.api.IChunk;
import org.primesoft.asyncworldedit.api.inner.IBlockRelighter;
import org.primesoft.asyncworldedit.api.directChunk.IWrappedChunk;
import org.primesoft.asyncworldedit.api.inner.IBlocksHubIntegration;
import org.primesoft.asyncworldedit.api.inner.IInnerDirectChunkAPI;
import org.primesoft.asyncworldedit.api.taskdispatcher.ITaskDispatcher;
import org.primesoft.asyncworldedit.directChunk.relighter.BlockReligher;

/**
 *
 * @author SBPrime
 */
public abstract class BaseDirectChunkAPI implements IInnerDirectChunkAPI {

    /**
     * The task dispatcher
     */
    protected final ITaskDispatcher m_dispatcher;

    /**
     * The blocks hub
     */
    protected final IBlocksHubIntegration m_blocksHub;

    /**
     * Block ID to emission light
     */
    private final HashMap<Character, Byte> m_idToEmission = new LinkedHashMap<Character, Byte>();
    
    
    /**
     * Block ID to opacity
     */
    private final HashMap<Character, Short> m_idToOpacity = new LinkedHashMap<Character, Short>();
    
    /**
     * Block ID to opacity (sky light)
     */
    private final HashMap<Character, Short> m_idToOpacitySkyLight = new LinkedHashMap<Character, Short>();
    
    
    /**
     * Block ID to BaseBlock
     */
    //TODO: 1.13        
    //private final HashMap<Character, BaseBlock> m_idToBlock = new LinkedHashMap<Character, BaseBlock>();
    
    private final IBlockRelighter m_blockRelighter;

    protected BaseDirectChunkAPI(ITaskDispatcher taskDispatcher, IBlocksHubIntegration blocksHub) {
        m_dispatcher = taskDispatcher;
        m_blocksHub = blocksHub;
        
        m_blockRelighter = new BlockReligher(this, taskDispatcher);
    }

    @Override
    public byte getLightEmissionLevel(char id) {
        //TODO: 1.13
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        /*Byte level = m_idToEmission.get(id);

        if (level == null) {
            BaseBlock block = convertId(id);
            if (block == null) {
                return 0;
            }

            level = getLightEmissionLevel(block);
            m_idToEmission.put(id, level);
        }

        return level;*/
    }

    @Override
    public byte getLightEmissionLevel(BlockStateHolder block) {
        //TODO: 1.13
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    /*
        if (block == null) {
            throw new IllegalArgumentException("block is null", new NullPointerException());
        }

        return getLightEmissionLevel(block.getType(), block.getData());*/
    } 
    
    @Override   
    public short getOpacityLevel(char id) {
        //TODO: 1.13
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        /*Short level = m_idToOpacity.get(id);

        if (level == null) {
            BaseBlock block = convertId(id);
            if (block == null) {
                return 0;
            }

            level = getOpacityLevel(block);
            m_idToOpacity.put(id, level);
        }

        return level;*/
    }

    @Override
    public short getOpacityLevel(BlockStateHolder block) {
        //TODO: 1.13
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.    
        /*
        if (block == null) {
            throw new IllegalArgumentException("block is null", new NullPointerException());
        }

        return getOpacityLevel(block.getType(), block.getData());*/
    }
    
    @Override   
    public short getOpacityLevelSkyLight(char id) {
        //TODO: 1.13
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        /*Short level = m_idToOpacitySkyLight.get(id);

        if (level == null) {
            BaseBlock block = convertId(id);
            if (block == null) {
                return 0;
            }

            level = getOpacityLevelSkyLight(block);
            m_idToOpacitySkyLight.put(id, level);
        }

        return level;*/
    }

    @Override
    public short getOpacityLevelSkyLight(BlockStateHolder block) {
        //TODO: 1.13
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    /*
        if (block == null) {
            throw new IllegalArgumentException("block is null", new NullPointerException());
        }

        return getOpacityLevelSkyLight(block.getType(), block.getData());*/
    }    

    @Override
    public IBlockRelighter getBlockRelighter() {
        return m_blockRelighter;
    }  

    @Override
    public IWrappedChunk wrapChunk(IChunk chunk) {
        return wrapChunk(chunk, null);
    }

    @Override
    public char getCombinedId(BlockStateHolder m, int data) {
        //TODO: 1.13
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    /*
        return getCombinedId(m.getId(), data);*/
    }
    
    
    /**
     * Get WorldEdit base blocks
     *
     * @param type
     * @param nbt
     * @return
     */
    /*    
    @Override
    public BaseBlock getBaseBlock(char type, CompoundTag nbt) {
        BaseBlock block = convertId(type);
        if (nbt != null) {
            block.setNbtData(nbt);
        }

        return block;
    }*/
}