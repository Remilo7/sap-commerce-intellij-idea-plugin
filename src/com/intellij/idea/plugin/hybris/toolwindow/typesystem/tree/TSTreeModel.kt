/*
 * This file is part of "SAP Commerce Developers Toolset" plugin for Intellij IDEA.
 * Copyright (C) 2019 EPAM Systems <hybrisideaplugin@epam.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.intellij.idea.plugin.hybris.toolwindow.typesystem.tree

import com.intellij.idea.plugin.hybris.toolwindow.typesystem.tree.nodes.TSNode
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.ui.tree.BaseTreeModel
import com.intellij.util.concurrency.Invoker
import com.intellij.util.concurrency.InvokerSupplier
import javax.swing.tree.DefaultMutableTreeNode

class TSTreeModel(private val root: TSNode)
    : BaseTreeModel<TSTreeModel.Node>(), Disposable, InvokerSupplier {

    private val myInvoker = if (ApplicationManager.getApplication().isUnitTestMode) {
        Invoker.forEventDispatchThread(this)
    } else {
        Invoker.forBackgroundThreadWithReadAction(this)
    }

    override fun getRoot() = Node(root)

    override fun getChildren(parent: Any?): List<Node> {
        if (parent is Node) {
            if (!parent.allowsChildren) return emptyList()

            val nodeObject = parent.userObject
            if (nodeObject is TSNode) {
                return nodeObject.getChildren()
                    .onEach { it!!.update() }
                    .map { Node(it) }
            }
        }

        return emptyList();

    }

    fun reload() {
        treeNodesChanged(null, null, null)
        treeStructureChanged(null, null, null)
    }

    class Node(private val tsNode : TSNode?) : DefaultMutableTreeNode(tsNode) {

        override fun toString() = tsNode.toString()
    }

    override fun getInvoker() = myInvoker

}