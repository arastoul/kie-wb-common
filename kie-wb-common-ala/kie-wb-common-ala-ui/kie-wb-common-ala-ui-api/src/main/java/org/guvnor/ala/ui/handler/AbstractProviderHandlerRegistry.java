/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.guvnor.ala.ui.handler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.enterprise.inject.Instance;

import org.guvnor.ala.ui.model.ProviderTypeKey;

/**
 * Base class for implementing a registry of ProviderHandler.
 */
public abstract class AbstractProviderHandlerRegistry<T extends ProviderHandler> {

    protected List<T> handlers = new ArrayList<>();

    protected AbstractProviderHandlerRegistry() {
    }

    protected AbstractProviderHandlerRegistry(final Instance<T> handlerInstance) {
        handlerInstance.iterator().forEachRemaining(handlers::add);
    }

    public boolean isProviderInstalled(ProviderTypeKey providerTypeKey) {
        return getProviderHandler(providerTypeKey) != null;
    }

    public T getProviderHandler(ProviderTypeKey providerTypeKey) {
        return handlers.stream()
                .filter(handler -> handler.acceptProviderType(providerTypeKey))
                .sorted(Comparator.comparingInt(ProviderHandler::getPriority))
                .findFirst().orElse(null);
    }
}