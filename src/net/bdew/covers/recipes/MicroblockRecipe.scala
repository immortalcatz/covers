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

package net.bdew.covers.recipes

import net.bdew.covers.items.{ItemMicroblock, ItemSaw}
import net.bdew.lib.crafting.RecipeMatcher
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.world.World

abstract class MicroblockRecipe extends IRecipe {

  def verifyAndCreateResult(inv: RecipeMatcher): Option[ItemStack]

  override def getRecipeSize: Int = 9

  override def matches(inv: InventoryCrafting, worldIn: World): Boolean = verifyAndCreateResult(new RecipeMatcher(inv)).isDefined

  override def getCraftingResult(inv: InventoryCrafting): ItemStack = verifyAndCreateResult(new RecipeMatcher(inv)).getOrElse(getRecipeOutput)

  override def getRemainingItems(inv: InventoryCrafting): Array[ItemStack] = {
    val res = new Array[ItemStack](inv.getSizeInventory)
    for (i <- res.indices; stack <- Option(inv.getStackInSlot(i))) {
      if (stack.getItem == ItemSaw) {
        val newStack = stack.copy()
        newStack.stackSize = 1
        res(i) = newStack
      }
    }
    res
  }

  override def getRecipeOutput: ItemStack = new ItemStack(ItemMicroblock)
}
