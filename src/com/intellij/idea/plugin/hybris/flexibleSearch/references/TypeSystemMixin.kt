package com.intellij.idea.plugin.hybris.flexibleSearch.references

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTableName
import com.intellij.idea.plugin.hybris.psi.references.TypeSystemReferenceBase
import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaModelAccess
import com.intellij.idea.plugin.hybris.type.system.meta.model.TSMetaItem
import com.intellij.idea.plugin.hybris.type.system.meta.model.TSMetaRelation
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.intellij.psi.ResolveResult

/**
 * @author Nosov Aleksandr <nosovae.dev@gmail.com>
 */

abstract class TypeNameMixin(astNode: ASTNode) : ASTWrapperPsiElement(astNode), FlexibleSearchTableName {

    private var myReference: TypeSystemItemRef? = null

    override fun getReferences(): Array<PsiReference> {
        if (myReference == null) {
            myReference = TypeSystemItemRef(this)
        }
        return arrayOf(myReference!!)
    }

    override fun clone(): Any {
        val result = super.clone() as TypeNameMixin
        result.myReference = null
        return result
    }
}

class TypeSystemItemRef(owner: FlexibleSearchTableName) : TypeSystemReferenceBase<FlexibleSearchTableName>(owner) {

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val lookingForName = element.text.replace("!", "")

        val items = (TSMetaModelAccess.getInstance(project).findMetaItemByName(lookingForName)
            ?.declarations
            ?.map { ItemTypeResolveResult(it) }
            ?: emptyList())

        val relations = TSMetaModelAccess.getInstance(project).findRelationByName(lookingForName)
                .distinctBy { it.name }
                .map { RelationResolveResult(it) }

        return (items + relations).toTypedArray()
    }

    private class ItemTypeResolveResult(private val localMetaItem: TSMetaItem) : ResolveResult {

        override fun getElement() = localMetaItem.retrieveDom()?.code?.xmlAttributeValue

        override fun isValidResult() = element != null
    }

    private class RelationResolveResult(private val localMetaRelation: TSMetaRelation) : ResolveResult {

        override fun getElement() = localMetaRelation.retrieveDom()?.code?.xmlAttributeValue

        override fun isValidResult() = element != null
    }

}