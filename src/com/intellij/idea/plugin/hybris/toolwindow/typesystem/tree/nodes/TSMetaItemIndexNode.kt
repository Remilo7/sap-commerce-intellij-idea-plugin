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

package com.intellij.idea.plugin.hybris.toolwindow.typesystem.tree.nodes

import com.intellij.ide.projectView.PresentationData
import com.intellij.idea.plugin.hybris.common.utils.HybrisIcons
import com.intellij.idea.plugin.hybris.type.system.meta.model.TSMetaItem.TSMetaItemIndex
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.ui.SimpleTextAttributes

class TSMetaItemIndexNode(val parent: TSMetaItemNode, val meta: TSMetaItemIndex) : TSNode(parent), Disposable {

    override fun dispose() = Unit
    override fun getName() = meta.name

    override fun update(project: Project, presentation: PresentationData) {
        presentation.addText(name, SimpleTextAttributes.REGULAR_ATTRIBUTES)
        presentation.locationString = meta.keys.joinToString()

        presentation.setIcon(HybrisIcons.INDEX)
        if (meta.isUnique) {
            presentation.setIcon(HybrisIcons.INDEX_UNIQUE)
        } else if (meta.isReplace) {
            presentation.setIcon(HybrisIcons.INDEX_FUN)
        } else if (meta.isRemove) {
            presentation.setIcon(HybrisIcons.INDEX_CLUSTER)
        }

    }

}