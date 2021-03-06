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

package net.bdew.covers.microblock

import mcmultipart.microblock.{BlockMicroMaterial, IMicroMaterial, MicroblockRegistry}
import net.bdew.covers.Covers
import net.bdew.covers.microblock.shape._
import net.minecraft.block.Block
import net.minecraft.init.Blocks

object InternalRegistry {
  case class Material(block: Block, meta: Int)

  var shapes = Map.empty[String, MicroblockShape]
  var materials = Map.empty[Material, IMicroMaterial]

  val defaultMaterial = registerMaterial(Blocks.STONE, 0)

  def registerShape(p: MicroblockShape): Unit = {
    MicroblockRegistry.registerMicroClass(p)
    shapes += p.name -> p
  }

  def registerMaterial(block: Block, meta: Int): IMicroMaterial = {
    val material = new BlockMicroMaterial(block.getStateFromMeta(meta))
    val actualMaterial = if (MicroblockRegistry.getMaterial(material.getName) == null) {
      MicroblockRegistry.registerMaterial(material)
      material
    } else {
      Covers.logDebug("Material already registered - skipping: %s".format(material.getName))
      MicroblockRegistry.getMaterial(material.getName)
    }
    materials += Material(block, meta) -> actualMaterial
    actualMaterial
  }

  def isValidMaterial(block: Block, meta: Int) = materials.isDefinedAt(Material(block, meta))
  def getMaterial(block: Block, meta: Int) = materials.get(Material(block, meta))

  registerShape(FaceShape)
  registerShape(EdgeShape)
  registerShape(CornerShape)
  registerShape(CenterShape)
  registerShape(HollowFaceShape)
  registerShape(GhostFaceShape)
}
