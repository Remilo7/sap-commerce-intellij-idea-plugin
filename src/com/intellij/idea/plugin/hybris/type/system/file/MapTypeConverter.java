/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2016 Alexander Bartash <AlexanderBartash@gmail.com>
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

package com.intellij.idea.plugin.hybris.type.system.file;

import com.intellij.idea.plugin.hybris.type.system.meta.TSMetaModelAccess;
import com.intellij.idea.plugin.hybris.type.system.meta.model.MetaType;
import com.intellij.idea.plugin.hybris.type.system.meta.model.TSGlobalMetaMap;
import com.intellij.idea.plugin.hybris.type.system.model.MapType;
import com.intellij.psi.PsiElement;
import com.intellij.util.xml.ConvertContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapTypeConverter extends TypeSystemConverterBase<MapType> {

    public MapTypeConverter() {
        super(MapType.class);
    }

    @Override
    protected MapType searchForName(@NotNull final String name, @NotNull final ConvertContext context, final TSMetaModelAccess meta) {
        return Optional.ofNullable(meta.findMetaMapByName(name))
                       .map(TSGlobalMetaMap::retrieveAllDoms)
                       .stream()
                       .flatMap(Collection::stream)
                       .findFirst()
                       .orElse(null);
    }

    @Override
    protected Collection<? extends MapType> searchAll(@NotNull final ConvertContext context, final TSMetaModelAccess meta) {
        return meta.<TSGlobalMetaMap>getAll(MetaType.META_MAP).stream()
                   .map(TSGlobalMetaMap::retrieveAllDoms)
                   .map(Collection::stream)
                   .map(Stream::findFirst)
                   .filter(Optional::isPresent)
                   .map(Optional::get)
                   .collect(Collectors.toList());
    }

    @Nullable
    @Override
    public String toString(@Nullable final MapType t, final ConvertContext context) {
        return useAttributeValue(t, MapType::getCode);
    }

    @Nullable
    @Override
    public PsiElement getPsiElement(@Nullable final MapType resolvedValue) {
        return navigateToValue(resolvedValue, MapType::getCode);
    }
}
