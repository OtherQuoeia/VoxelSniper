/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 The Voxel Plugineering Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.voxelplugineering.voxelsniper;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.voxelplugineering.voxelsniper.api.Gunsmith;
import com.voxelplugineering.voxelsniper.api.IBrushLoader;
import com.voxelplugineering.voxelsniper.api.IBrushManager;
import com.voxelplugineering.voxelsniper.api.IMaterialFactory;
import com.voxelplugineering.voxelsniper.api.IVoxelSniper;
import com.voxelplugineering.voxelsniper.api.IWorldFactory;
import com.voxelplugineering.voxelsniper.bukkit.BukkitMaterial;
import com.voxelplugineering.voxelsniper.bukkit.BukkitWorldFactory;
import com.voxelplugineering.voxelsniper.command.BukkitCommandRegistrar;
import com.voxelplugineering.voxelsniper.common.CommonBrushManager;
import com.voxelplugineering.voxelsniper.common.CommonMaterialFactory;
import com.voxelplugineering.voxelsniper.common.FileBrushLoader;
import com.voxelplugineering.voxelsniper.common.command.CommandHandler;
import com.voxelplugineering.voxelsniper.common.commands.BrushCommand;
import com.voxelplugineering.voxelsniper.perms.VaultPermissionProxy;

public class VoxelSniperBukkit extends JavaPlugin implements IVoxelSniper
{
    public static VoxelSniperBukkit voxelsniper;

    private SniperManagerBukkit sniperManager;
    private IBrushManager brushManager;
    private IBrushLoader brushLoader;
    private IMaterialFactory<Material> materialFactory;
    private CommandHandler commandHandler;
    private IWorldFactory worldFactory;

    @Override
    public void onEnable()
    {
        
        Gunsmith.beginInit();

        voxelsniper = this;
        Gunsmith.setPlugin(this);
        
        this.worldFactory = new BukkitWorldFactory(this.getServer());
        Gunsmith.setWorldFactory(this.worldFactory);
        
        this.sniperManager = new SniperManagerBukkit();
        this.sniperManager.init();
        Gunsmith.setSniperManager(this.sniperManager);
        
        this.brushLoader = new FileBrushLoader(new File(this.getDataFolder(), "brushes"));
        Gunsmith.setDefaultBrushLoader(this.brushLoader);
        
        this.brushManager = new CommonBrushManager();
        this.brushManager.init();
        Gunsmith.setGlobalBrushManager(this.brushManager);
        
        setupPermissions();
        
        this.materialFactory = new CommonMaterialFactory<Material>();
        Gunsmith.setMaterialFactory(this.materialFactory);
        setupMaterials();
        
        this.commandHandler = new CommandHandler();
        Gunsmith.setCommandHandler(this.commandHandler);
        Gunsmith.getCommandHandler().setRegistrar(new BukkitCommandRegistrar());
        setupCommands();
        
        Gunsmith.finish();
        
    }

    private void setupPermissions()
    {
        Plugin vault = Bukkit.getPluginManager().getPlugin("Vault");
        if (vault != null) {
            Gunsmith.setPermissionProxy(new VaultPermissionProxy());
        }
    }
    
    public void setupCommands()
    {
        Gunsmith.getCommandHandler().registerCommand(new BrushCommand());
    }
    
    public void setupMaterials()
    {
        for(Material m: Material.values())
        {
            this.materialFactory.registerMaterial(m.name(), new BukkitMaterial(m));
        }
    }

    @Override
    public void onDisable()
    {
        Gunsmith.stop();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
         
        
        return false;
    }

    @Override
    public SniperManagerBukkit getSniperManager()
    {
        return this.sniperManager;
    }

    @Override
    public IBrushManager getBrushManager()
    {
        return this.brushManager;
    }

}