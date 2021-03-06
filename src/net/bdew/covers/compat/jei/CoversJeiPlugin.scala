/*
 * Copyright (c) bdew 2016.
 *
 * This file is part of Simple Covers.
 *
 * Simple Covers is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Simple Covers is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Simple Covers.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.bdew.covers.compat.jei

import mezz.jei.api.ISubtypeRegistry.ISubtypeInterpreter
import mezz.jei.api._
import net.bdew.covers.Covers
import net.bdew.covers.config.Config
import net.bdew.covers.config.Config.ShowMode
import net.bdew.covers.items.ItemMicroblock
import net.bdew.covers.microblock.InternalRegistry
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary

@JEIPlugin
class CoversJeiPlugin extends BlankModPlugin {
  override def registerItemSubtypes(subtypeRegistry: ISubtypeRegistry): Unit = {
    subtypeRegistry.registerSubtypeInterpreter(ItemMicroblock, new ISubtypeInterpreter {
      override def getSubtypeInfo(itemStack: ItemStack): String =
        ItemMicroblock.getData(itemStack).map(d => "%s:%s:%d".format(d.material.getName, d.shape.name, d.size)).orNull
    })
  }

  override def register(registry: IModRegistry): Unit = {
    Covers.logInfo("Simple Covers JEI Plugin loaded")

    registry.addRecipeHandlers(MicroblockRecipeHandler)

    registry.addRecipeRegistryPlugin(CoversRegistryPlugin)

    if (Config.jeiShowMode == ShowMode.MINIMAL) {
      for {
        material <- InternalRegistry.materials.values if material.getDefaultMaterialState != Blocks.STONE.getDefaultState
        shape <- InternalRegistry.shapes.values
        size <- shape.validSizes
      } {
        registry.getJeiHelpers.getIngredientBlacklist.addIngredientToBlacklist(ItemMicroblock.makeStack(material, shape, size))
      }
    } else if (Config.jeiShowMode == ShowMode.NONE) {
      registry.getJeiHelpers.getIngredientBlacklist.addIngredientToBlacklist(new ItemStack(ItemMicroblock, 1, OreDictionary.WILDCARD_VALUE))
    }
  }

  override def onRuntimeAvailable(jeiRuntime: IJeiRuntime): Unit = {

  }
}
